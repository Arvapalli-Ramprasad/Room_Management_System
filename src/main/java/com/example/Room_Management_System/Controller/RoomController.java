package com.example.Room_Management_System.Controller;

import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;
    @PostMapping("/add")
    public ResponseEntity addRoom(@RequestBody Room room){
        try{
            Room response = roomService.addRoom(room);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getRoom")
    public ResponseEntity getRoom(@RequestParam("id") String roomId ){

        try {
            Room response  = roomService.getRoom(roomId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("no Room Exist",HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("/deleteRoom")
    public ResponseEntity deleteRoom(@RequestParam("id") String roomId ){
        try{
            Room response  = roomService.deleteRoom(roomId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("No room in the db with the provided ID",HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/updateRoom")
    public ResponseEntity updateRoom(@RequestBody Room room, @RequestParam("id") String roomId){
        try{
            Room response = roomService.updateRoom(room, roomId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("cannot update the field", HttpStatus.BAD_REQUEST);
        }
    }
}
