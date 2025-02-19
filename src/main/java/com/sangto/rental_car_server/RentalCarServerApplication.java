package com.sangto.rental_car_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RentalCarServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalCarServerApplication.class, args);
		System.out.println("http://localhost:8080/swagger-ui/index.html");
	}

}
