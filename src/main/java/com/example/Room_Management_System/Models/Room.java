package com.example.Room_Management_System.Models;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "rooms")
public class Room {
    @Id
    private String id;
    private String roomNumber;
    private String floorNumber;
    private String buildingName;
    private Integer totalCapacity;
    private Integer currentOccupancy;
    private Double monthlyRent;
    private Double totalAmountToBeCollected; // Total expected from all students
    private Double totalAmountCollected;     //amount collected
    private Double balance;                  // Current balance after expenses
    private Double securityDeposit;
    private String address;
    private List<String> amenities;
    private List<String> studentIds;
    private String notes;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String maintenanceStatus;
    private String roomType;
    private Boolean sharedBathroom;
    private String furnishingStatus;
    private String ownerContact;
    private Integer rentDueDate;
    private LocalDate lastInspectionDate;
    private List<String> photos;

    public Room() {
    }

    public Room(String id, String roomNumber, String floorNumber, String buildingName, Integer totalCapacity, Integer currentOccupancy, Double monthlyRent, Double totalAmountToBeCollected, Double totalAmountCollected, Double balance, Double securityDeposit, String address, List<String> amenities, List<String> studentIds, String notes, Boolean active, LocalDateTime createdAt, LocalDateTime updatedAt, String maintenanceStatus, String roomType, Boolean sharedBathroom, String furnishingStatus, String ownerContact, Integer rentDueDate, LocalDate lastInspectionDate, List<String> photos) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.buildingName = buildingName;
        this.totalCapacity = totalCapacity;
        this.currentOccupancy = currentOccupancy;
        this.monthlyRent = monthlyRent;
        this.totalAmountToBeCollected = totalAmountToBeCollected;
        this.totalAmountCollected = totalAmountCollected;
        this.balance = balance;
        this.securityDeposit = securityDeposit;
        this.address = address;
        this.amenities = amenities;
        this.studentIds = studentIds;
        this.notes = notes;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.maintenanceStatus = maintenanceStatus;
        this.roomType = roomType;
        this.sharedBathroom = sharedBathroom;
        this.furnishingStatus = furnishingStatus;
        this.ownerContact = ownerContact;
        this.rentDueDate = rentDueDate;
        this.lastInspectionDate = lastInspectionDate;
        this.photos = photos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(String floorNumber) {
        this.floorNumber = floorNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public Integer getCurrentOccupancy() {
        return currentOccupancy;
    }

    public void setCurrentOccupancy(Integer currentOccupancy) {
        this.currentOccupancy = currentOccupancy;
    }

    public Double getMonthlyRent() {
        return monthlyRent;
    }

    public void setMonthlyRent(Double monthlyRent) {
        this.monthlyRent = monthlyRent;
    }

    public Double getTotalAmountToBeCollected() {
        return totalAmountToBeCollected;
    }

    public void setTotalAmountToBeCollected(Double totalAmountToBeCollected) {
        this.totalAmountToBeCollected = totalAmountToBeCollected;
    }

    public Double getTotalAmountCollected() {
        return totalAmountCollected;
    }

    public void setTotalAmountCollected(Double totalAmountCollected) {
        this.totalAmountCollected = totalAmountCollected;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(Double securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public List<String> getStudentIds() {
        return studentIds;
    }

    public void setStudentIds(List<String> studentIds) {
        this.studentIds = studentIds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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

    public String getMaintenanceStatus() {
        return maintenanceStatus;
    }

    public void setMaintenanceStatus(String maintenanceStatus) {
        this.maintenanceStatus = maintenanceStatus;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Boolean getSharedBathroom() {
        return sharedBathroom;
    }

    public void setSharedBathroom(Boolean sharedBathroom) {
        this.sharedBathroom = sharedBathroom;
    }

    public String getFurnishingStatus() {
        return furnishingStatus;
    }

    public void setFurnishingStatus(String furnishingStatus) {
        this.furnishingStatus = furnishingStatus;
    }

    public String getOwnerContact() {
        return ownerContact;
    }

    public void setOwnerContact(String ownerContact) {
        this.ownerContact = ownerContact;
    }

    public Integer getRentDueDate() {
        return rentDueDate;
    }

    public void setRentDueDate(Integer rentDueDate) {
        this.rentDueDate = rentDueDate;
    }

    public LocalDate getLastInspectionDate() {
        return lastInspectionDate;
    }

    public void setLastInspectionDate(LocalDate lastInspectionDate) {
        this.lastInspectionDate = lastInspectionDate;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
