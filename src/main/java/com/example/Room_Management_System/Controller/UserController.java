package com.example.Room_Management_System.Controller;

import com.example.Room_Management_System.JwtTokenUtil;
import com.example.Room_Management_System.Models.InvitationToken;
import com.example.Room_Management_System.Models.User;
import com.example.Room_Management_System.Repository.InvitationTokenRepository;
import com.example.Room_Management_System.Repository.UserInfoRepository;
import com.example.Room_Management_System.Requests.PasswordDto;
import com.example.Room_Management_System.Services.UserService;
import com.example.Room_Management_System.entity.UserInfo;
import com.example.Room_Management_System.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtService jwtService;

    @Autowired
    InvitationTokenRepository invitationTokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    UserInfoRepository userInfoRepository;


    @PostMapping("/set-password")
    public ResponseEntity<?> setPassword(
            @RequestParam String token,
            @RequestBody PasswordDto dto
    ) {
        InvitationToken invitation = invitationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (invitation.isUsed() || invitation.getExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired or already used.");
        }

        // Step 1: Create Login UserInfo for auth
        UserInfo userInfo = new UserInfo();
        userInfo.setId(invitation.getId());
        userInfo.setName(invitation.getUserName());
        userInfo.setEmail(invitation.getEmail());
        userInfo.setMobileNumber(invitation.getMobileNumber());
        userInfo.setPassword(encoder.encode(dto.getPassword()));
        userInfo.setRoles("ROLE_USER"); // default role
        userInfoRepository.save(userInfo);

        // Step 2: Mark token as used
        invitation.setUsed(true);
        invitationTokenRepository.save(invitation);

        return ResponseEntity.ok("Password set successfully. You can now log in.");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/add")
    public ResponseEntity addUser(
            @RequestBody User user,
            @RequestParam("roomId") String roomId,
            @RequestHeader("Authorization") String authHeader
    ){
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token

        try{
            User response = userService.addUser(user ,roomId, createdBy);
            return new ResponseEntity(response, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_USER')")
    @GetMapping("/getUser")
    public ResponseEntity getUser(
            @RequestParam("id") String userId,
            @RequestHeader("Authorization") String authHeader
    ){
        try {
            String token = jwtTokenUtil.extractTokenFromHeader(authHeader);
            String tokenUserId = jwtService.extractUserId(token);
            List<String> roles = jwtService.extractRoles(token);

            // Allow access if admin or if user is accessing their own info
            if (!roles.contains("ROLE_ADMIN") && !tokenUserId.equals(userId)) {
                return new ResponseEntity<>("Unauthorized Role", HttpStatus.FORBIDDEN);
            }

            User response  = userService.getUser(userId);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("no User Exist",HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/getAllUsers")
    public ResponseEntity getUser(
            @RequestParam(required = false, defaultValue = "50") Integer limit,
            @RequestParam(required = false, defaultValue = "0") Integer offset,
            @RequestHeader("Authorization") String authHeader
    ){

        try {
            // Extract JWT token from Authorization header
            String token = jwtTokenUtil.extractTokenFromHeader(authHeader);
            // Extract admin user ID from token
            String adminUserId = jwtService.extractUserId(token);


            Page<User> users = userService.getAllUsers(adminUserId, limit, offset);
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
    public ResponseEntity deleteUser(
            @RequestParam("id") String userId,
            @RequestHeader("Authorization") String authHeader
    ){
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token
        try{
            User response  = userService.deleteUser(userId,createdBy);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>("No user in the db with the provided ID",HttpStatus.BAD_REQUEST);
        }

    }
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_USER')")
    @PutMapping("/updateUser")
    public ResponseEntity updateUser(
            @RequestBody User user,
            @RequestParam("id") String userId,
            @RequestHeader("Authorization") String authHeader
    ){
        try{

            String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
            String createdBy = jwtService.extractUserId(token);    // extracts userId from token
            List<String> roles = jwtService.extractRoles(token);

            // Allow access if admin or if user is accessing their own info
            if (!roles.contains("ROLE_ADMIN") && !createdBy.equals(userId)) {
                return new ResponseEntity<>("Unauthorized Role", HttpStatus.FORBIDDEN);

            }
            User response = userService.updateUser(user, userId, createdBy);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("cannot update the field", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/deleteAllUser")
    public ResponseEntity deleteAllUsers(
            @RequestHeader("Authorization") String authHeader
    ){
        try{
            String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
            String createdBy = jwtService.extractUserId(token);    // extracts userId from token
            List<String> roles = jwtService.extractRoles(token);

            String response  = userService.deleteAllUsers(createdBy);
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }

    }
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/search/{text}")
    public List<User> search(
            @PathVariable String text,
            @RequestHeader("Authorization") String authHeader

    ) {
        String token = jwtTokenUtil.extractTokenFromHeader(authHeader); // removes "Bearer "
        String createdBy = jwtService.extractUserId(token);    // extracts userId from token
        List<String> roles = jwtService.extractRoles(token);

        return userService.searchByText(text, createdBy);
    }
}
