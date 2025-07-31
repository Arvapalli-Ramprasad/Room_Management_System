package com.example.Room_Management_System.Repository;

import com.example.Room_Management_System.Models.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    Page<Room> findAll(Pageable pageable);

    Optional<Room> findByIdAndCreatedBy(String id, String createdBy);

    void deleteByCreatedBy(String createdBy);


    Page<Room> findByCreatedBy(String createdBy, Pageable pageable);
    @Query("{ '$and': [ " +
            "{ 'userId': ?1 }, " +
            "{ '$or': [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'email': { $regex: ?0, $options: 'i' } }, " +
            "{ 'phoneNumber': { $regex: ?0, $options: 'i' } } " +
            "] } " +
            "] }")
    List<Room> searchByText(String text, String createdBy);

}
