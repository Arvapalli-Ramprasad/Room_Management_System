package com.example.Room_Management_System.Controller;
import com.example.Room_Management_System.JwtTokenUtil;
import com.example.Room_Management_System.Models.Expense;
import com.example.Room_Management_System.Models.User;
import com.example.Room_Management_System.Requests.UpdateExpenseDTO;
import com.example.Room_Management_System.Services.ExpenseService;
import com.example.Room_Management_System.Services.UserService;
import com.example.Room_Management_System.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("expences")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtService  jwtService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("addExpense")
    public ResponseEntity addExpense(
            @RequestBody Expense expense,
//            @RequestParam("userId") String userId,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeader(authHeader);
            String userId = jwtService.extractUserId(token);

            Expense savedExpense = expenseService.addExpense(expense,userId);
            return new ResponseEntity<>(savedExpense, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity getExpense(
            @PathVariable String id
    ) {
        try {
            Expense expense = expenseService.getExpense(id);
            return new ResponseEntity<>(expense, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

//    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
//    @GetMapping("/getAllExpences")
//    public ResponseEntity getAllexpences(
//            @RequestParam(required = false, defaultValue = "50") Integer limit,
//            @RequestParam(required = false, defaultValue = "0") Integer offset)
//
//    {
//
//        try {
//            Page<Expense> expenses = expenseService.getAllExpences(limit, offset);
//            if (expenses.isEmpty()) {
//                return ResponseEntity.ok(Page.empty());
//            }
//            return ResponseEntity.ok(expenses);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error fetching users: " + e.getMessage());
//        }
//
//    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/getAllExpensesByRoomId")
    public ResponseEntity<?> getAllExpensesOfRoom(
            @RequestParam(required = false, defaultValue = "50") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeader(authHeader);
            String userId = jwtService.extractUserId(token);

            User user = userService.getUser(userId);

            // Only allow access to the room assigned to the user
            String userRoomId = user.getRoomId();
            if (userRoomId == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not assigned to any room.");
            }

            Pageable pageable = PageRequest.of(offset, limit);
            Page<Expense> expenses = expenseService.getExpensesByRoomId(userRoomId, pageable);

            return ResponseEntity.ok(expenses);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching expenses: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity getUserExpenses(
            @RequestHeader("Authorization") String autheHeader
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeader(autheHeader);
            String userId = jwtService.extractUserId(token);

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
    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PutMapping("/{id}")
    public ResponseEntity updateExpense(
            @PathVariable String id,
            @RequestBody Expense expense,
            @RequestHeader("Authorization") String autheHeader
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeader(autheHeader);
            String userId = jwtService.extractUserId(token);

            Expense updatedExpense = expenseService.updateExpense(expense, id, userId);
            return new ResponseEntity<>(updatedExpense, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<Expense> partilaUpdateExpense(
            @PathVariable String id,
            @RequestBody UpdateExpenseDTO expenseDTO
    ){
        Expense updatedExpense = expenseService.patchExpense(id, expenseDTO);
        return ResponseEntity.ok(updatedExpense);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteExpense(
            @PathVariable String id,
            @RequestHeader("Authorization") String autheHeader
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeader(autheHeader);
            String userId = jwtService.extractUserId(token);

            String deletedExpense =  expenseService.deleteExpense(id,userId);
            return new ResponseEntity<>(deletedExpense,HttpStatus.GONE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    @DeleteMapping("/deleteAllExpenses")
    public ResponseEntity deleteAllExpense() {
        try {
            String deletedExpense =  expenseService.deleteAllExpense();
            return new ResponseEntity<>(deletedExpense,HttpStatus.GONE);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
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
