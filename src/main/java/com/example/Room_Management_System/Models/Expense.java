package com.example.Room_Management_System.Models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "expenses")
public class Expense {
    @Id
    private String id;
    // Basic expense details
    private Double amount;
    private String description;
    private LocalDate date;
    private String category; // e.g., "FOOD", "UTILITIES", "MAINTENANCE"
    private String paymentMethod; // e.g., "CASH", "CARD", "ONLINE"

    // User reference
    private String userId; // Reference to User
    private String userName; // For quick display without joining

    // Room reference
    private String roomId; // Reference to Room
    private String roomNumber; // For quick display without joining

    // Split details (if expense is shared)
    private List<Split> splits; // List of users and their shares

    // Payment status
    private Boolean isPaid;
    private String paymentReceiptUrl; // URL to payment receipt image/document

    // Audit fields
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Notes and attachments
    private String notes;
    private List<String> attachmentUrls; // URLs to expense-related documents/images

    public Expense() {
    }

    public Expense(String id, Double amount, String description, LocalDate date, String category, String paymentMethod, String userId, String userName, String roomId, String roomNumber, List<Split> splits, Boolean isPaid, String paymentReceiptUrl, LocalDateTime createdAt, LocalDateTime updatedAt, String notes, List<String> attachmentUrls) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.userId = userId;
        this.userName = userName;
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.splits = splits;
        this.isPaid = isPaid;
        this.paymentReceiptUrl = paymentReceiptUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.notes = notes;
        this.attachmentUrls = attachmentUrls;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public List<Split> getSplits() {
        return splits;
    }

    public void setSplits(List<Split> splits) {
        this.splits = splits;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public String getPaymentReceiptUrl() {
        return paymentReceiptUrl;
    }

    public void setPaymentReceiptUrl(String paymentReceiptUrl) {
        this.paymentReceiptUrl = paymentReceiptUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }
}

class Split{
    private String userId;
    private String userName;
    private Double shareAmount;
    private Boolean isPaid;
    private String paymentReceiptUrl;

    public Split() {
    }

    public Split(String userId, String userName, Double shareAmount, Boolean isPaid, String paymentReceiptUrl) {
        this.userId = userId;
        this.userName = userName;
        this.shareAmount = shareAmount;
        this.isPaid = isPaid;
        this.paymentReceiptUrl = paymentReceiptUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Double getShareAmount() {
        return shareAmount;
    }

    public void setShareAmount(Double shareAmount) {
        this.shareAmount = shareAmount;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public String getPaymentReceiptUrl() {
        return paymentReceiptUrl;
    }

    public void setPaymentReceiptUrl(String paymentReceiptUrl) {
        this.paymentReceiptUrl = paymentReceiptUrl;
    }
}
