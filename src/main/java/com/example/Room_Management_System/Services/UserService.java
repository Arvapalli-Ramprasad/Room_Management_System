package com.example.Room_Management_System.Services;

import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Models.User;
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

    public User addUser(User user, String roomId) {
        Room room = roomService.getRoom(roomId);

        //Validate room
        if(room.getStudentIds().size()>=room.getTotalCapacity()){
            throw new RuntimeException("Room already filled");
        }


        // 2. Generate and set user ID
            user.setId(UUID.randomUUID().toString());

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

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setReplyTo("springbootofficial@gmail.com");
        simpleMailMessage.setSubject("Welcome to Room Number: " + room.getRoomNumber());

        simpleMailMessage.setText(
                "Dear " + user.getName() + ",\n\n" +
                        "Welcome to our Room Management System!\n\n" +
                        "We're excited to let you know that you’ve been successfully allocated to Room Number: " + room.getRoomNumber() + ".\n" +
                        "Your room is fully furnished and includes all the amenities you need for a comfortable stay.\n\n" +
                        "As a welcome offer, we’re providing you a **20% discount** on your first month's rent!\n\n" +
                        "If you have any questions or need assistance, feel free to reach out to our support team.\n\n" +
                        "Thank you for choosing us.\n\n" +
                        "Best regards,\n" +
                        "Room Management Team"
        );

        javaMailSender.send(simpleMailMessage);

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

    public Page<User> getAllUsers(Integer limit, Integer offset){

        Pageable pageable = PageRequest.of(
                offset,
                limit
        );
        return userRepository.findAll(pageable);

    }

    public User deleteUser(String userId){

        User user = getUser(userId);
        Room room = roomService.getRoom(user.getRoomId());
        room.getStudentIds().remove(userId);
        roomRepository.save(room);
        userRepository.deleteById(userId);
        return user;
    }

    public User updateUser(User user, String userId){
        User oldDetails = getUser(userId);

        if (user!=null){
            oldDetails.setName(user.getName());
            oldDetails.setUpdatedAt(LocalDateTime.now());
            return addUser(oldDetails, oldDetails.getRoomId());
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

    public String deleteAllUsers() {
        userRepository.deleteAll();
        return "All Users Deleted Successfully";
    }

    public List<User> searchByText(String text){
        return userRepository.searchByText(text);
    }

}
