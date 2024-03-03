package com.choresync.announcement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AnnouncementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnnouncementServiceApplication.class, args);
	}

}
