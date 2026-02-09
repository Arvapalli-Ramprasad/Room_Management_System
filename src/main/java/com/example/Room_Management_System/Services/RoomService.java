package com.example.Room_Management_System.Services;

import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Models.User;
import com.example.Room_Management_System.Repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private S3Service s3Service;

    @Value("${aws.s3.bucket}")
    private String bucketName;


    public Room addRoom(String createdBy,Room room) {
        String uuid = UUID.randomUUID().toString();
        room.setId(uuid);
        room.setCreatedBy(createdBy);
        String isValid = validateRoom(room);
        if (isValid == null) {
            if (room.getCreatedAt() == null) {
                room.setCreatedAt(LocalDateTime.now());
            }
            room.setRentDueDate(LocalDate.now().withDayOfMonth(5).atStartOfDay());
            room.setUpdatedAt(LocalDateTime.now());
            room.setTotalAmountToBeCollected(room.getStudentIds().size()*room.getMonthlyRent());
            return roomRepository.save(room);
        } else throw new RuntimeException("Mandatory fields are missing: "+  isValid);
    }

    public Room getRoom(String roomId,String createdBy, List<String> roles) {

        Optional<Room> roomOpt = roomRepository.findById(roomId);
        System.out.println(roomOpt.get());
        if (!roomOpt.isPresent()) {
            throw new RuntimeException("Room not found");
        }
        Room room = roomOpt.get();

        if(roles.contains("ROLE_ADMIN")){
            if(!room.getCreatedBy().equals(createdBy)){
                throw new RuntimeException("Access Denied for this room (Admin Mismatch)");
            }
        }
        else if(roles.contains("ROLE_USER")){
            if(!room.getStudentIds().contains(createdBy)){
                throw new RuntimeException("Access Denied : User not in the Room");
            }
        }
        return room;
    }

    public Room getRoom(String roomId,String createdBy) {

        Optional<Room> roomOpt = roomRepository.findByIdAndCreatedBy(roomId,createdBy);
        if (!roomOpt.isPresent()) {
            throw new RuntimeException("Room not found");
        }
        Room room = roomOpt.get();
        return room;
    }

    public Page<Room> getAllRooms(String createdBy, Integer limit, Integer offset){

        Pageable pageable = PageRequest.of(
                offset,
                limit
        );
        return roomRepository.findByCreatedBy(createdBy, pageable);

    }

    public Room deleteRoom(String roomId,String createdBy) {
        Room room = getRoom(roomId,createdBy);

        if (room != null) {
            roomRepository.deleteById(roomId);
            return room;
        }
        return room;
    }

    public Room updateRoom(Room room, String roomId, String createdBy) {
        Room oldDetails = getRoom(roomId,createdBy);

        if (room != null) {
            oldDetails.setRoomNumber(room.getRoomNumber());
            oldDetails.setUpdatedAt(LocalDateTime.now());
            return addRoom(createdBy,oldDetails);
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


    public String deleteAllRoom(String createdBy) {
         roomRepository.deleteByCreatedBy(createdBy);
         return "All rooms deleted Successfully";
    }

    public List<Room> searchByText(String text, String createdBy){
        return roomRepository.searchByText(text,createdBy);
    }

//    public List<String> uploadRoomPhotos(String roomId, List<MultipartFile> files) {
//
//        Room room = roomRepository.findById(roomId)
//                .orElseThrow(() -> new RuntimeException("Room not found"));
//
//        List<String> photoUrls = room.getPhotos();
//
//        for (MultipartFile file : files) {
//            try {
//                String url = fileStorageService.storeFile(file);
//                photoUrls.add(url);
//            } catch (IOException e) {
//                throw new RuntimeException("Failed to upload photo", e);
//            }
//        }
//
//        room.setPhotos(photoUrls);
//        room.setUpdatedAt(LocalDateTime.now());
//
//        roomRepository.save(room);
//
//        return photoUrls;
//    }

    public List<String> uploadRoomPhotos(String roomId, List<MultipartFile> files) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        List<String> photoUrls = room.getPhotos() != null ? new ArrayList<>(room.getPhotos()) : new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                // Upload to S3 in a specific folder, e.g., "rooms/{roomId}"
                String s3Key = s3Service.uploadFile(file, "rooms/" + roomId);

                // Optionally, build a public URL (if your bucket allows public access)
                String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + s3Key;

                photoUrls.add(fileUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload photo", e);
            }
        }

        room.setPhotos(photoUrls);
        room.setUpdatedAt(LocalDateTime.now());

        roomRepository.save(room);

        return photoUrls;
    }


    public List<String> getRoomPhotos(String roomId){
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return room.getPhotos();
    }
}
