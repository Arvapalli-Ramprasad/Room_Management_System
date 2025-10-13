package com.example.Room_Management_System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class RoomManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RoomManagementSystemApplication.class, args);
        //http://localhost:8080/swagger-ui/index.html
		//http://localhost:8080/room-management-service/swagger-ui/index.html

		//local mongodb connection
		//mongodb://localhost:27017/Room_Management_System

		//addedd
		//nijnfhefhure
	}
}
