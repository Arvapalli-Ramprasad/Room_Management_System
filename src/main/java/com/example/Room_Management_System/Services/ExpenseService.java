package com.example.Room_Management_System.Services;

import com.example.Room_Management_System.Models.Expense;
import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Models.User;
import com.example.Room_Management_System.Repository.ExpenseRepository;
import com.example.Room_Management_System.Repository.RoomRepository;
import com.example.Room_Management_System.Repository.UserInfoRepository;
import com.example.Room_Management_System.Repository.UserRepository;
import com.example.Room_Management_System.Requests.UpdateExpenseDTO;
import com.example.Room_Management_System.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;


@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    public Expense addExpense(Expense expense, String userId) {
        // 1. Validate mandatory fields
        String uuid = UUID.randomUUID().toString();
        expense.setId(uuid);
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<UserInfo> userInfo = userInfoRepository.findById(userId);

        if (!userOpt.isPresent() && !userInfo.isPresent()) {
            throw new RuntimeException("User not found");
        }
        else if(userOpt.isPresent()){
            expense.setUserId(userId);
            expense.setUserName(userOpt.get().getName());
            expense.setDate(LocalDate.now());

            expense.setRoomId(userOpt.get().getRoomId());
            String validationError = validateExpense(expense);
            if (validationError != null) {
                throw new RuntimeException("Mandatory fields are missing: " + validationError);
            }

            // 3. Generate ID if not provided
            if (expense.getId() == null) {
                expense.setId(UUID.randomUUID().toString());
            }

            // 4. Set timestamps
            if (expense.getCreatedAt() == null) {
                expense.setCreatedAt(LocalDateTime.now());
            }
            expense.setUpdatedAt(LocalDateTime.now());

            Room room = roomRepository.findById(userOpt.get().getRoomId()).get();

            paymentCalculation(room,expense);


            // 5. Save the expense
            Expense savedExpense = expenseRepository.save(expense);

            // 6. Update user's expense list
            updateUserExpenseList(savedExpense);

            return savedExpense;

        }else if (userInfo.isPresent()){

            expense.setUserId(userId);
            expense.setUserName(userInfo.get().getName());
            expense.setDate(LocalDate.now());

            String validationError = validateExpense(expense);
            if (validationError != null) {
                throw new RuntimeException("Mandatory fields are missing: " + validationError);
            }

            // 3. Generate ID if not provided
            if (expense.getId() == null) {
                expense.setId(UUID.randomUUID().toString());
            }

            // 4. Set timestamps
            if (expense.getCreatedAt() == null) {
                expense.setCreatedAt(LocalDateTime.now());
            }
            expense.setUpdatedAt(LocalDateTime.now());
            // 5. Save the expense
            Expense savedExpense = expenseRepository.save(expense);
            // 6. Update user's expense list
            updateUserExpenseList(savedExpense);

            return savedExpense;

        }

        return null;
    }

    private void paymentCalculation(Room room, Expense expense) {
        room.setBalance(room.getBalance()-expense.getAmount());
        expense.setRoomNumber(room.getRoomNumber());
        roomRepository.save(room);
    }

    private String validateExpense(Expense expense) {
        StringBuilder missingFields = new StringBuilder();

        // Required fields
        if (expense.getAmount() == null || expense.getAmount() <= 0) {
            missingFields.append("amount, ");
        }
        if (expense.getDescription() == null || expense.getDescription().trim().isEmpty()) {
            missingFields.append("description, ");
        }

        if (expense.getCategory() == null || expense.getCategory().trim().isEmpty()) {
            missingFields.append("category, ");
        }
        if (expense.getPaymentMethod() == null || expense.getPaymentMethod().trim().isEmpty()) {
            missingFields.append("paymentMethod, ");
        }
        if (expense.getUserId() == null) {
            missingFields.append("userId, ");
        }
        if (expense.getPaid() == null) {
            missingFields.append("isPaid, ");
        }

        // Remove trailing comma and space
        if (missingFields.length() > 0) {
            return missingFields.substring(0, missingFields.length() - 2);
        }

        return null;
    }

    private void updateUserExpenseList(Expense expense) {
        // Add expense ID to user's expense list
        Optional<User> userOpt = userRepository.findById(expense.getUserId());
        Optional<UserInfo> userInfo = userInfoRepository.findById(expense.getUserId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> expenseIds = user.getExpenseIds();
            if (expenseIds == null) {
                expenseIds = new ArrayList<>();
            }
            expenseIds.add(expense.getId());
            if(user.getTotalExpences()==null){
                user.setTotalExpences(expense.getAmount());
            }else{
                user.setTotalExpences(user.getTotalExpences()+expense.getAmount());
            }
            user.setExpenseIds(expenseIds);
            userRepository.save(user);
        }else if(userInfo.isPresent()){
            UserInfo userInfo1 = userInfo.get();
            List<String> expenseIds = userInfo1.getExpenseIds();
            if (expenseIds == null) {
                expenseIds = new ArrayList<>();
            }
            expenseIds.add(expense.getId());
            if(userInfo1.getTotalExpences()==null){
                userInfo1.setTotalExpences(expense.getAmount());
            }else{
                userInfo1.setTotalExpences(userInfo1.getTotalExpences()+expense.getAmount());
            }
            userInfo1.setExpenseIds(expenseIds);
            userInfoRepository.save(userInfo1);
        }
    }

    public Expense getExpense(String expenseId) {
        Optional<Expense> expenseOpt = expenseRepository.findById(expenseId);
        if (!expenseOpt.isPresent()) {
            throw new RuntimeException("Expense not found");
        }
        return expenseOpt.get();
    }

    public Expense updateExpense(Expense expense, String expenseId, String userId) {
        Optional<Expense> existingExpenseOpt = expenseRepository.findById(expenseId);

        if (!existingExpenseOpt.isPresent()) {
            throw new RuntimeException("Expense not found");
        }

        Expense existingExpense = existingExpenseOpt.get();

        if(!existingExpense.getUserId().equals(userId)){
            throw  new RuntimeException("You can only update your own expense");
        }
        existingExpense.setAmount(expense.getAmount());
        existingExpense.setDescription(expense.getDescription());
        existingExpense.setCategory(expense.getCategory());
        existingExpense.setSplits(expense.getSplits());
        existingExpense.setPaid(expense.getPaid());
        existingExpense.setPaymentReceiptUrl(expense.getPaymentReceiptUrl());
        existingExpense.setNotes(expense.getNotes());
        existingExpense.setAttachmentUrls(expense.getAttachmentUrls());

        // Update timestamps
        existingExpense.setUpdatedAt(LocalDateTime.now());

        return expenseRepository.save(existingExpense);
    }

    public Expense patchExpense(String id, UpdateExpenseDTO dto) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();

        if (dto.getAmount() != null) update.set("amount", dto.getAmount());
        if (dto.getDescription() != null) update.set("description", dto.getDescription());
        if (dto.getDate() != null) update.set("date", dto.getDate());
        if (dto.getCategory() != null) update.set("category", dto.getCategory());
        if (dto.getPaymentMethod() != null) update.set("paymentMethod", dto.getPaymentMethod());
        if (dto.getPaid() != null) update.set("paid", dto.getPaid());
        if (dto.getPaymentReceiptUrl() != null) update.set("paymentReceiptUrl", dto.getPaymentReceiptUrl());
        if (dto.getNotes() != null) update.set("notes", dto.getNotes());
        if (dto.getAttachmentUrls() != null) update.set("attachmentUrls", dto.getAttachmentUrls());

        update.set("updatedAt", LocalDateTime.now());

        mongoTemplate.updateFirst(query, update, Expense.class);

        return mongoTemplate.findById(id, Expense.class);
    }



    public String deleteExpense(String expenseId, String userId) {
        Optional<Expense> expenseOpt = expenseRepository.findById(expenseId);
        if (!expenseOpt.isPresent()) {
            throw new RuntimeException("Expense not found with the provided expenseId");
        }

        Expense expense = expenseOpt.get();

        //validate the user
        if(!expense.getUserId().equals(userId)){
            throw new RuntimeException("You can only update your own expense");
        }


        // Remove from user's expense list
        Optional<User> userOpt = userRepository.findById(expense.getUserId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> expenseIds = user.getExpenseIds();
            if (expenseIds != null) {
                expenseIds.remove(expenseId);
                user.setExpenseIds(expenseIds);
                user.setTotalExpences(user.getTotalExpences()-expense.getAmount());
                userRepository.save(user);
            }
        }

        expenseRepository.deleteById(expenseId);
        return "Expense has benn deleted Successfully with id: "+expenseId;
    }


    public String deleteAllExpense() {
        expenseRepository.deleteAll();
        return "All Expense has been deleted Successfully";
    }



    public List<Expense> getUserExpenses(String userId) {
        if(userId==null || userId.trim().isEmpty()){
            throw new IllegalArgumentException("User ID cannot be null or empty");
        }
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found with ID: "+userId);
        }
        User user = userOpt.get();
        if(user.getExpenseIds()==null || user.getExpenseIds().size()==0){
            throw new RuntimeException("Empty Expences on the userId: "+ userId);
        }

        List<Expense> expenses = expenseRepository.findAllById(user.getExpenseIds());
        return expenses;
    }

    public Page<Expense> getAllExpences(Integer limit, Integer offset){

        Pageable pageable = PageRequest.of(
                offset,
                limit
        );
        return expenseRepository.findAll(pageable);

    }

    public Page<Expense> getExpensesByRoomId(String roomId, Pageable pageable) {
        return expenseRepository.findByRoomId(roomId, pageable);
    }


    public List<Expense> searchByText(String text, String roomId) {
        Pattern regex = Pattern.compile(Pattern.quote(text), Pattern.CASE_INSENSITIVE);

        List<Criteria> orCriteria = new ArrayList<>();

        // String fields (regex search)
        orCriteria.add(Criteria.where("description").regex(regex));
        orCriteria.add(Criteria.where("category").regex(regex));
        orCriteria.add(Criteria.where("paymentMethod").regex(regex));

        // If text is a number → search amount
        if (text.matches("\\d+(\\.\\d+)?")) {
            orCriteria.add(Criteria.where("amount").is(Double.parseDouble(text)));
        }

        Criteria finalCriteria = new Criteria().andOperator(
                Criteria.where("roomId").is(roomId),
                new Criteria().orOperator(orCriteria.toArray(new Criteria[0]))
        );

        Query query = new Query(finalCriteria);
        return mongoTemplate.find(query, Expense.class);
    }

}
