package jsl.group.microservices.cloud.gateway_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false"}, classes = GatewayTestConfiguration.class)
class GatewayServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
