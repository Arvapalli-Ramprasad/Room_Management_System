package com.example.Room_Management_System.Repository;

import com.example.Room_Management_System.Models.Expense;
import com.example.Room_Management_System.Models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseRepository extends MongoRepository<Expense,String> {
    Page<Expense> findAll(Pageable pageable);

}
