package com.example.Room_Management_System.Requests;

public class PasswordDto {
    private String password;

    public PasswordDto() {
    }

    public PasswordDto(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
