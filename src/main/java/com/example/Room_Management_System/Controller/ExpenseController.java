package com.example.Room_Management_System.Controller;
import com.example.Room_Management_System.Models.Expense;
import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("expences")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping("addExpense")
    public ResponseEntity addExpense(
            @RequestBody Expense expense,
            @RequestParam("userId") String userId
    ) {
        try {
            Expense savedExpense = expenseService.addExpense(expense,userId);
            return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getExpense(@PathVariable String id) {
        try {
            Expense expense = expenseService.getExpense(id);
            return new ResponseEntity<>(expense, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllExpences")
    public ResponseEntity getAllexpences(
            @RequestParam(required = false, defaultValue = "50") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset)
    {

        try {
            Page<Expense> expenses = expenseService.getAllExpences(limit, offset);
            if (expenses.isEmpty()) {
                return ResponseEntity.ok(Page.empty());
            }
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching users: " + e.getMessage());
        }

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getUserExpenses(@PathVariable String userId) {
        try {
            List<Expense> expenses = expenseService.getUserExpenses(userId);
            return new ResponseEntity<>(expenses, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
//
//    @GetMapping("/room/{roomId}")
//    public ResponseEntity<List<Expense>> getRoomExpenses(@PathVariable String roomId) {
//        try {
//            List<Expense> expenses = expenseService.getRoomExpenses(roomId);
//            return new ResponseEntity<>(expenses, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @GetMapping("/category/{category}")
//    public ResponseEntity<List<Expense>> getExpensesByCategory(@PathVariable String category) {
//        try {
//            List<Expense> expenses = expenseService.getExpensesByCategory(category);
//            return new ResponseEntity<>(expenses, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }
//
    @PutMapping("/{id}")
    public ResponseEntity updateExpense(@PathVariable String id, @RequestBody Expense expense) {
        try {
            Expense updatedExpense = expenseService.updateExpense(expense, id);
            return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteExpense(@PathVariable String id) {
        try {
            String deletedExpense =  expenseService.deleteExpense(id);
            return new ResponseEntity<>(deletedExpense,HttpStatus.GONE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteAllExpenses")
    public ResponseEntity deleteAllExpense() {
        try {
            String deletedExpense =  expenseService.deleteAllExpense();
            return new ResponseEntity<>(deletedExpense,HttpStatus.GONE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/search/{text}")
    public List<Expense> search(@PathVariable String text) {
        return expenseService.searchByText(text);
    }
//
//    @GetMapping("/stats/{roomId}")
//    public ResponseEntity<ExpenseStats> getExpenseStats(@PathVariable String roomId) {
//        try {
//            ExpenseStats stats = expenseService.getExpenseStats(roomId);
//            return new ResponseEntity<>(stats, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }

}
