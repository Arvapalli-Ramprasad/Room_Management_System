package com.example.Room_Management_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
public class RoomManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomManagementSystemApplication.class, args);
	}

}
