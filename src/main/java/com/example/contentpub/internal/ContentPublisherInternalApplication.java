package com.example.contentpub.internal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class ContentPublisherInternalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContentPublisherInternalApplication.class, args);
	}

}
