package com.example.Room_Management_System.Repository;

import com.example.Room_Management_System.entity.UserInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends MongoRepository<UserInfo, Integer> {

    Page<UserInfo> findAll(Pageable pageable);

    Optional<UserInfo> findByName(String username);
}
