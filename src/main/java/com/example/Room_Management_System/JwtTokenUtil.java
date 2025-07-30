package com.example.Room_Management_System;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }
}