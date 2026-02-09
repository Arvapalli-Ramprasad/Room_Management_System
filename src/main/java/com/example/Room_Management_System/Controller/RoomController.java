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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("/getRoom")
    public ResponseEntity getRoom(
            @RequestParam("id") String roomId,
            @RequestHeader("Authorization") String authHeader
    ) {
        try {
            String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
            String createdBy = jwtService.extractUserId(token);    // extracts userId from token
            List<String> roles = jwtService.extractRoles(token);

            System.out.println("***************"+ roles);

            Room response  = roomService.getRoom(roomId,createdBy,roles);
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
    @PutMapping("/updateRoom/{Id}")
    public ResponseEntity updateRoom(
            @PathVariable("Id") String roomId,
            @RequestBody Room room,
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




        // ✅ Upload one or multiple photos
        @PreAuthorize("hasAuthority('ROLE_ADMIN')")
        @PostMapping("/{roomId}/photos")
        public ResponseEntity<?> uploadRoomPhotos(
                @PathVariable String roomId,
                @RequestParam("files") List<MultipartFile> files) throws IOException {

//            Room room = roomRepository.findById(roomId)
//                    .orElseThrow(() -> new RuntimeException("Room not found"));

//            List<String> photoUrls = room.getPhotos();

            List<String> photoUrls = roomService.uploadRoomPhotos(roomId, files);

//            for (MultipartFile file : files) {
//                String url = fileStorageService.storeFile(file);
//                photoUrls.add(url);
//            }
//
//            room.setPhotos(photoUrls);
//            room.setUpdatedAt(LocalDateTime.now());
//
//            roomRepository.save(room);

            return ResponseEntity.ok(photoUrls);
        }

        // ✅ Get photos
        @GetMapping("/{roomId}/photos")
        public List<String> getRoomPhotos(@PathVariable String roomId) {

            List<String> photoUrls = roomService.getRoomPhotos(roomId);

            return photoUrls;

        }

        // ✅ Delete a photo
//        @DeleteMapping("/{roomId}/photos")
//        public ResponseEntity<?> deleteRoomPhoto(
//                @PathVariable String roomId,
//                @RequestParam String photoUrl) {
//
//            Room room = roomRepository.findById(roomId)
//                    .orElseThrow(() -> new RuntimeException("Room not found"));
//
//            room.getPhotos().remove(photoUrl);
//            roomRepository.save(room);
//
//            return ResponseEntity.ok("Photo removed");
//        }


}
