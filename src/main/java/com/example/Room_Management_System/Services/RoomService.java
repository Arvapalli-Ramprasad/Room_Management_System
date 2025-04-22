package com.example.Room_Management_System.Services;

import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public Room addRoom(Room room) {
        String uuid = UUID.randomUUID().toString();
        Optional<Room> existingRoom = roomRepository.findById(uuid);
        if (existingRoom.isPresent()) {
            throw new RuntimeException("Room with this ID already exists");
        }
        room.setId(uuid);
        String isValid = validateRoom(room);
        if (isValid == null) {
            if (room.getCreatedAt() == null) {
                room.setCreatedAt(LocalDateTime.now());
            }
            room.setUpdatedAt(LocalDateTime.now());
            room.setTotalAmountToBeCollected(room.getStudentIds().size()*room.getMonthlyRent());
            return roomRepository.save(room);
        } else throw new RuntimeException("Mandatory fields are missing: "+  isValid);
    }

    public Room getRoom(String roomId) {

        Optional<Room> roomOpt = roomRepository.findById(roomId);
        if (!roomOpt.isPresent()) {
            throw new RuntimeException("Room not found");
        }
        Room room = roomOpt.get();
        return room;
    }

    public Room deleteRoom(String roomId) {
        Room room = getRoom(roomId);

        if (room != null) {
            roomRepository.deleteById(roomId);
            return room;
        }
        return room;
    }

    public Room updateRoom(Room room, String roomId) {
        Room oldDetails = getRoom(roomId);

        if (room != null) {
            oldDetails.setRoomNumber(room.getRoomNumber());
            oldDetails.setUpdatedAt(LocalDateTime.now());
            return addRoom(oldDetails);
        }
        return oldDetails;
    }

    private String validateRoom(Room room) {
        StringBuilder missingFields = new StringBuilder();

        if (room.getRoomNumber() == null || room.getRoomNumber().trim().isEmpty())
            missingFields.append("roomNumber, ");
        if (room.getBuildingName() == null || room.getBuildingName().trim().isEmpty())
            missingFields.append("buildingName, ");
        if (room.getFloorNumber() == null || room.getFloorNumber().trim().isEmpty())
            missingFields.append("floorNumber, ");
        if (room.getTotalCapacity() == null || room.getTotalCapacity() <= 0)
            missingFields.append("totalCapacity, ");
        if (room.getMonthlyRent() == null || room.getMonthlyRent() <= 0)
            missingFields.append("monthlyRent, ");
        if (room.getAddress() == null || room.getAddress().trim().isEmpty())
            missingFields.append("address, ");
        if (room.getActive() == null)
            missingFields.append("active, ");
        if (missingFields.length() > 0) {
            return missingFields.substring(0, missingFields.length() - 2);
        }
        if(room.getCurrentOccupancy()>0 || room.getStudentIds().size()>0){
            throw new RuntimeException("Initially room occupancy and the number of persons in the room should be empty");
        }
        return null;
    }


    public String deleteAllRoom() {
         roomRepository.deleteAll();
         return "All rooms deleted Successfully";
    }


}
