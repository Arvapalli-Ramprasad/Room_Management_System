package com.example.Room_Management_System.Repository;

import com.example.Room_Management_System.Models.InvitationToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InvitationTokenRepository extends MongoRepository<InvitationToken,String> {
    Optional<InvitationToken> findByToken(String token);
}
