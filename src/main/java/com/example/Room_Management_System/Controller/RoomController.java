package com.example.Room_Management_System.Controller;

import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Models.User;
import com.example.Room_Management_System.Services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @GetMapping("/getAllUser")
    public ResponseEntity getUser(@RequestParam(required = false, defaultValue = "50") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset){

        try {
            Page<Room> rooms = roomService.getAllRooms(limit, offset);
            if (rooms.isEmpty()) {
                return ResponseEntity.ok(Page.empty());
            }
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching users: " + e.getMessage());
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

    @DeleteMapping("/deleteAllRooms")
    public ResponseEntity deleteAllRooms(){
        try{
            String response = roomService.deleteAllRoom();
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
