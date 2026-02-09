package com.example.Room_Management_System.Models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {
    @Id
    private String id;
    private String userId;          // candidate's User ID
    private String title;           // short activity title
    private String description;     // detailed notes
    private String activityType;    // Interview, Assessment, Meeting, etc.
    private LocalDate scheduledDate;
    private String scheduledTime;   // optional
    private String status;          // Pending, Completed, Cancelled, Rescheduled
    private String priority;        // Low, Medium, High
    private String location;        // physical or online link
    private String comments;        // feedback or notes
    private List<String> relatedDocuments; // links or document IDs
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}