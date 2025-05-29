package com.example.Room_Management_System.Repository;

import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {
    Page<Room> findAll(Pageable pageable);


    @Query("{'$or': [ " +
            "{'name':   { $regex: ?0, $options: 'i' } }, " +
            "{'email':  { $regex: ?0, $options: 'i' } }, " +
            "{'phoneNumber':   { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<Room> searchByText(String text);
}
