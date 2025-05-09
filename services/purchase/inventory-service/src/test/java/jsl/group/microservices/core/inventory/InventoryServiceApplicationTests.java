package jsl.group.microservices.core.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.cloud.config.enabled=false"})
class InventoryServiceApplicationTests extends PostgresBaseTests {

	@Test
	void contextLoads() {
	}

}
