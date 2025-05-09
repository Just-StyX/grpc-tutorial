package jsl.group.checkout_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false"}, classes = CheckoutSecurityTestConfiguration.class)
class CheckoutServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
