package com.example.Room_Management_System.Controller;

import com.example.Room_Management_System.Models.User;
import com.example.Room_Management_System.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody User user, @RequestParam("roomId") String roomId){
        try{
            User response = userService.addUser(user,roomId);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity getUser(@RequestParam("id") String userId ){

        try {
            User response  = userService.getUser(userId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("no User Exist",HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity deleteUser(@RequestParam("id") String userId ){
        try{
            User response  = userService.deleteUser(userId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("No user in the db with the provided ID",HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/updateUser")
    public ResponseEntity updateUser(@RequestBody User user, @RequestParam("id") String userId){
        try{
            User response = userService.updateUser(user, userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("cannot update the field", HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteAllUser")
    public ResponseEntity deleteAllUsers(){
        try{
            String response  = userService.deleteAllUsers();
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
}
