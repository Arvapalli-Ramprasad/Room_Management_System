package com.example.Room_Management_System.Controller;

import com.example.Room_Management_System.JwtTokenUtil;
import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Services.RoomService;
import com.example.Room_Management_System.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rooms")

public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity addRoom(
            @RequestBody Room room,
            @RequestHeader("Authorization") String authHeader
    ){
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token
        try{
            Room response = roomService.addRoom(createdBy,room);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getRoom")
    public ResponseEntity getRoom(
            @RequestParam("id") String roomId,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token
        try {
            Room response  = roomService.getRoom(roomId,createdBy);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("no Room Exist",HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAllRooms")
    public ResponseEntity getUser(
            @RequestParam(required = false, defaultValue = "50") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestHeader("Authorization") String authHeader
    ){
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token
        try {
            Page<Room> rooms = roomService.getAllRooms(createdBy, limit, offset);
            if (rooms.isEmpty()) {
                return ResponseEntity.ok(Page.empty());
            }
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching users: " + e.getMessage());
        }

    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteRoom")
    public ResponseEntity deleteRoom(
            @RequestParam("id") String roomId,
            @RequestHeader("Authorization") String authHeader
    ){
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token
        try{
            Room response  = roomService.deleteRoom(roomId,createdBy);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("No room in the db with the provided ID",HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/updateRoom")
    public ResponseEntity updateRoom(
            @RequestBody Room room,
            @RequestParam("id") String roomId,
            @RequestHeader("Authorization") String authHeader

    ){
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token
        try{
            Room response = roomService.updateRoom(room, roomId, createdBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("cannot update the field", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteAllRooms")
    public ResponseEntity deleteAllRooms(
            @RequestHeader("Authorization") String authHeader
    ){
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token
        try{
            String response = roomService.deleteAllRoom(createdBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/search/{text}")
    public List<Room> search(
            @PathVariable String text,
            @RequestHeader("Authorization") String authHeader

    ) {
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token
        return roomService.searchByText(text,createdBy);
    }

}
