package com.example.Room_Management_System.Controller;

import com.example.Room_Management_System.Models.User;
import com.example.Room_Management_System.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody User user, @RequestParam("roomId") String roomId){
        try{
            User response = userService.addUser(user,roomId);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getUser")
    public ResponseEntity getUser(@RequestParam("id") String userId ){

        try {
            User response  = userService.getUser(userId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("no User Exist",HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAllUsers")
    public ResponseEntity getUser(@RequestParam(required = false, defaultValue = "50") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset){

        try {
            Page<User> users = userService.getAllUsers(limit, offset);
            if (users.isEmpty()) {
                return ResponseEntity.ok(Page.empty());
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching users: " + e.getMessage());
        }

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteUser")
    public ResponseEntity deleteUser(@RequestParam("id") String userId ){
        try{
            User response  = userService.deleteUser(userId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("No user in the db with the provided ID",HttpStatus.BAD_REQUEST);
        }

    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/updateUser")
    public ResponseEntity updateUser(@RequestBody User user, @RequestParam("id") String userId){
        try{
            User response = userService.updateUser(user, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("cannot update the field", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteAllUser")
    public ResponseEntity deleteAllUsers(){
        try{
            String response  = userService.deleteAllUsers();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/search/{text}")
    public List<User> search(@PathVariable String text) {
        return userService.searchByText(text);
    }
}
