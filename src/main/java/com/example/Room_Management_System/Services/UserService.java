package com.example.Room_Management_System.Services;

import com.example.Room_Management_System.Models.InvitationToken;
import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Models.User;
import com.example.Room_Management_System.Repository.InvitationTokenRepository;
import com.example.Room_Management_System.Repository.RoomRepository;
import com.example.Room_Management_System.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private InvitationTokenRepository invitationTokenRepository;

    public User addUser(User user, String roomId, String createdbY) {
        Room room = roomService.getRoom(roomId, createdbY);

        //Validate room
        if(room.getStudentIds().size()>=room.getTotalCapacity()){
            throw new RuntimeException("Room already filled");
        }


        // 2. Generate and set user ID

        String userId = UUID.randomUUID().toString();
        user.setId(userId);
        user.setAddedUser(createdbY);

        // 3. Set the user's roomId field
        user.setRoomId(roomId);

        String validationError = validateUser(user);
        if (validationError != null) {
            throw new RuntimeException("Mandatory fields are missing: " + validationError);

        }

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        // Always set updatedAt
        user.setUpdatedAt(LocalDateTime.now());

        boolean isPaymentCalculated = paymentCalculation(user,room);

        // Generate token
        String token = UUID.randomUUID().toString();

        InvitationToken invitation = new InvitationToken();
        invitation.setId(userId);
        invitation.setMobileNumber(user.getPhoneNumber());
        invitation.setUserName(user.getName());
        invitation.setEmail(user.getEmail());
        invitation.setToken(token);
        invitation.setCreatedAt(LocalDateTime.now());
        invitation.setExpiresAt(LocalDateTime.now().plusHours(24));
        invitation.setUsed(false);
        invitationTokenRepository.save(invitation);

        // Send email with setup link
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setReplyTo("springbootofficial@gmail.com");
        message.setSubject("Complete Your Room Allocation - Set Password");

        String frontendUrl = "http://localhost:4200/#/setup-password?token=" + token + "&email=" + user.getEmail();
        message.setText(
                "Hi " + user.getName() + ",\n\n" +
                        "You've been invited to join Room " + room.getRoomNumber() + ".\n" +
                        "Please click the link below to set your password and complete your registration:\n" +
                        frontendUrl + "\n\n" +
                        "This link is valid for 24 hours.\n\n" +
                        "Thanks,\nRoom Management Team"
        );
        javaMailSender.send(message);

        // 4. Save the user
        User savedUser = userRepository.save(user);

        // 5. Add user ID to room's studentIds list
        List<String> studentIds = room.getStudentIds();
        if (studentIds == null) {
            studentIds = new ArrayList<>();
        }

        studentIds.add(savedUser.getId());
        room.setStudentIds(studentIds);
        room.setCurrentOccupancy(room.getStudentIds().size());
        room.setTotalAmountToBeCollected(room.getMonthlyRent()*room.getStudentIds().size());
        roomRepository.save(room);

        return savedUser;

    }

    private boolean paymentCalculation(User user, Room room){
        if(user.getMonthlyPayment()>user.getTotalPaid()){
            user.setPendingAmount(user.getMonthlyPayment()-user.getTotalPaid());
            room.setTotalAmountCollected(room.getTotalAmountCollected()+user.getTotalPaid());
            room.setBalance(room.getTotalAmountCollected());
        } else if (user.getMonthlyPayment().equals(user.getTotalPaid())) {
            room.setTotalAmountCollected(room.getTotalAmountCollected()+user.getTotalPaid());
            room.setBalance(room.getTotalAmountCollected());
        }
        return true;
    }

    public User getUser(String userId){

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        return user;
    }

    public Page<User> getAllUsers(String addedUserId, Integer limit, Integer offset){

        Pageable pageable = PageRequest.of(
                offset,
                limit
        );
        return userRepository.findByAddedUser(addedUserId,pageable);

    }

    public User deleteUser(String userId, String addedUser){

        User user = getUser(userId);
        Room room = roomService.getRoom(user.getRoomId(),addedUser);

        // Confirm that the user is in the room's student list
        if (!room.getStudentIds().contains(userId)) {
            throw new RuntimeException("User is not assigned to this room");
        }

        room.getStudentIds().remove(userId);
        roomRepository.save(room);
        userRepository.deleteById(userId);
        return user;
    }

    public User updateUser(User user, String userId, String createdBy){
        User oldDetails = getUser(userId);

        if (user!=null){
            oldDetails.setName(user.getName());
            oldDetails.setUpdatedAt(LocalDateTime.now());
            return addUser(oldDetails, oldDetails.getRoomId(),createdBy);
        }
        return oldDetails;
    }

    private String validateUser(User user) {
        StringBuilder missingFields = new StringBuilder();

        // Required fields
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            missingFields.append("name, ");
        }
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            missingFields.append("email, ");
        }
        if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) {
            missingFields.append("phoneNumber, ");
        }
        if (user.getGender() == null || user.getGender().trim().isEmpty()) {
            missingFields.append("gender, ");
        }
        if (user.getDateOfBirth() == null) {
            missingFields.append("dateOfBirth, ");
        }
        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            missingFields.append("address, ");
        }
        if (user.getEmergencyContact() == null || user.getEmergencyContact().trim().isEmpty()) {
            missingFields.append("emergencyContact, ");
        }
        if (user.getGuardianName() == null || user.getGuardianName().trim().isEmpty()) {
            missingFields.append("guardianName, ");
        }
        if (user.getGuardianContact() == null || user.getGuardianContact().trim().isEmpty()) {
            missingFields.append("guardianContact, ");
        }
        if (user.getMonthlyPayment() == null || user.getMonthlyPayment() <= 0) {
            missingFields.append("monthlyPayment, ");
        }
        if (user.getActive() == null) {
            missingFields.append("active, ");
        }

        // Remove trailing comma and space if present
        if (missingFields.length() > 0) {
            return missingFields.substring(0, missingFields.length() - 2);
        }

        return null;
    }

    public String deleteAllUsers(String addedUser) {
        userRepository.deleteAllByAddedUser(addedUser);
        return "All Users Deleted Successfully";
    }

    public List<User> searchByText(String text, String addedUser){
        return userRepository.searchByText(text, addedUser);
    }

}
