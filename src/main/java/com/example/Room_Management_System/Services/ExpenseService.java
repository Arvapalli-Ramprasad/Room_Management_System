package com.example.Room_Management_System.Services;

import com.example.Room_Management_System.Models.Expense;
import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Models.User;
import com.example.Room_Management_System.Repository.ExpenseRepository;
import com.example.Room_Management_System.Repository.RoomRepository;
import com.example.Room_Management_System.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class ExpenseService {
    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Expense addExpense(Expense expense, String userId) {
        // 1. Validate mandatory fields

        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }

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
        if (expense.getDate() == null) {
            missingFields.append("date, ");
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
        if (expense.getRoomId() == null) {
            missingFields.append("roomId, ");
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
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> expenseIds = user.getExpenseIds();
            if (expenseIds == null) {
                expenseIds = new ArrayList<>();
            }
            expenseIds.add(expense.getId());
            user.setExpenseIds(expenseIds);
            userRepository.save(user);
        }
    }

    public Expense getExpense(String expenseId) {
        Optional<Expense> expenseOpt = expenseRepository.findById(expenseId);
        if (!expenseOpt.isPresent()) {
            throw new RuntimeException("Expense not found");
        }
        return expenseOpt.get();
    }

    public Expense updateExpense(Expense expense, String expenseId) {
        Optional<Expense> existingExpenseOpt = expenseRepository.findById(expenseId);

        if (!existingExpenseOpt.isPresent()) {
            throw new RuntimeException("Expense not found");
        }

        Expense existingExpense = existingExpenseOpt.get();
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

    public String deleteExpense(String expenseId) {
        Optional<Expense> expenseOpt = expenseRepository.findById(expenseId);
        if (!expenseOpt.isPresent()) {
            throw new RuntimeException("Expense not found with the provided expenseId");
        }

        Expense expense = expenseOpt.get();
        // Remove from user's expense list
        Optional<User> userOpt = userRepository.findById(expense.getUserId());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            List<String> expenseIds = user.getExpenseIds();
            if (expenseIds != null) {
                expenseIds.remove(expenseId);
                user.setExpenseIds(expenseIds);
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

}
