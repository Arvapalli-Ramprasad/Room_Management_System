package com.example.Room_Management_System.Requests;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateExpenseDTO {
    private Double amount;
    private String description;
    private LocalDate date;
    private String category;
    private String paymentMethod;
    private Boolean paid;
    private String paymentReceiptUrl;
    private String notes;
    private List<String> attachmentUrls;

    public UpdateExpenseDTO(Double amount, String description, LocalDate date, String category, String paymentMethod, Boolean paid, String paymentReceiptUrl, String notes, List<String> attachmentUrls) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.paid = paid;
        this.paymentReceiptUrl = paymentReceiptUrl;
        this.notes = notes;
        this.attachmentUrls = attachmentUrls;
    }

    public UpdateExpenseDTO() {
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

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public String getPaymentReceiptUrl() {
        return paymentReceiptUrl;
    }

    public void setPaymentReceiptUrl(String paymentReceiptUrl) {
        this.paymentReceiptUrl = paymentReceiptUrl;
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
