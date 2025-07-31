package com.example.Room_Management_System.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "invitationTokens")
public class InvitationToken {
    @Id
    private String id;
    private String email;
    private String userName;

    private String mobileNumber;
    private String token;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private boolean used;

    public InvitationToken() {
    }

    public InvitationToken(String id, String email, String userName, String mobileNumber, String token, LocalDateTime createdAt, LocalDateTime expiresAt, boolean used) {
        this.id = id;
        this.email = email;
        this.userName = userName;
        this.mobileNumber = mobileNumber;
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.used = used;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
