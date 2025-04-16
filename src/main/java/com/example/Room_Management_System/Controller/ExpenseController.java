package com.example.Room_Management_System.Controller;
import com.example.Room_Management_System.Models.Expense;
import com.example.Room_Management_System.Services.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("expences")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @PostMapping("addExpense")
    public ResponseEntity addExpense(
            @RequestBody Expense expense,
            @RequestParam("userId") String userId,
            @RequestParam("roomId") String roomId
    ) {
        try {
            Expense savedExpense = expenseService.addExpense(expense,userId,roomId);
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
//
//    @GetMapping("/user/{userId}")
//    public ResponseEntity<List<Expense>> getUserExpenses(@PathVariable String userId) {
//        try {
//            List<Expense> expenses = expenseService.getUserExpenses(userId);
//            return new ResponseEntity<>(expenses, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }
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
//    @PutMapping("/{id}")
//    public ResponseEntity<Expense> updateExpense(@PathVariable String id, @RequestBody Expense expense) {
//        try {
//            Expense updatedExpense = expenseService.updateExpense(expense, id);
//            return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteExpense(@PathVariable String id) {
//        try {
//            expenseService.deleteExpense(id);
//            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//        } catch (RuntimeException e) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//    }
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
