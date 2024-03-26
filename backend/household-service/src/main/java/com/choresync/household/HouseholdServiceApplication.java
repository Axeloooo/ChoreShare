package com.choresync.household;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HouseholdServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(HouseholdServiceApplication.class, args);
	}
}
