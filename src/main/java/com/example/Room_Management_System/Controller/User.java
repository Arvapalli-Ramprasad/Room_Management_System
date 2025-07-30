package com.example.Room_Management_System.Controller;

import com.example.Room_Management_System.entity.AuthRequest;
import com.example.Room_Management_System.entity.UserInfo;
import com.example.Room_Management_System.service.JwtService;
import com.example.Room_Management_System.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class User {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity getUser(@RequestParam(required = false, defaultValue = "50") Integer limit, @RequestParam(required = false, defaultValue = "0") Integer offset){

        try {
            Page<UserInfo> users = service.getAllUsers(limit, offset);
            if (users.isEmpty()) {
                return ResponseEntity.ok(Page.empty());
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error fetching users: " + e.getMessage());
        }

    }

    @GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String userProfile() {
        return "Welcome to User Profile";
    }

    @GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        if (authentication.isAuthenticated()) {
            // Get the authenticated user (Spring Security UserDetails)
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Fetch user from DB using username
            UserInfo user = service.findByUsername(authRequest.getUsername());

            // Assuming one role; if you have multiple roles, handle accordingly
            List<String> roles = userDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());


            return jwtService.generateToken(authRequest.getUsername(), roles, user.getId());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }


}

