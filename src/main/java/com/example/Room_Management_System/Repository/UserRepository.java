package com.example.Room_Management_System.Repository;

import com.example.Room_Management_System.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    Page<User> findAll(Pageable pageable);

}
