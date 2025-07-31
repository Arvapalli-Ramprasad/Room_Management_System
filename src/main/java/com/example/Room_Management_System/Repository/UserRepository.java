package com.example.Room_Management_System.Repository;

import com.example.Room_Management_System.Models.Room;
import com.example.Room_Management_System.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {
    Page<User> findAll(Pageable pageable);

    Page<User> findByAddedUser(String addedUserId, Pageable pageable);

    void deleteAllByAddedUser(String addedUserId);

    @Query("{'addedUser': ?1, '$or': [ " +
            "{'name':   { $regex: ?0, $options: 'i' } }, " +
            "{'email':  { $regex: ?0, $options: 'i' } }, " +
            "{'phoneNumber':   { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<User> searchByText(String text, String addedUser);
}
