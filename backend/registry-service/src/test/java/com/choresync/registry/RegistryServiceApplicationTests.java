package com.choresync.registry;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = { "EUREKA_USERNAME=test-user", "EUREKA_PASSWORD=test-pass" })
class RegistryServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
