package com.example.demo;

import com.example.demo.kafka.KafkaContentConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
		new KafkaContentConsumer().start();
	}

}
