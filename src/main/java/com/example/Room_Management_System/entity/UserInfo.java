package com.example.Room_Management_System.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "userInfo")
public class UserInfo {

    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private String mobileNumber;
    private List<String> expenseIds; // List of Expense IDs
    private Double totalExpences;
    private String roles;


    public UserInfo() {

    }

    public UserInfo(String id, String name, String email, String password, String mobileNumber, List<String> expenseIds,Double totalExpences, String roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.expenseIds = expenseIds;
        this.totalExpences = totalExpences;
        this.roles = roles;
    }

    public Double getTotalExpences() {
        return totalExpences;
    }

    public void setTotalExpences(Double totalExpences) {
        this.totalExpences = totalExpences;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<String> getExpenseIds() {
        return expenseIds;
    }

    public void setExpenseIds(List<String> expenseIds) {
        this.expenseIds = expenseIds;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }
}
