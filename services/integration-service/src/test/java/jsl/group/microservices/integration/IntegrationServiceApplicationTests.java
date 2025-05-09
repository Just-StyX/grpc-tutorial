package jsl.group.microservices.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false"}, classes = IntegrationSecurityTest.class)
class IntegrationServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
