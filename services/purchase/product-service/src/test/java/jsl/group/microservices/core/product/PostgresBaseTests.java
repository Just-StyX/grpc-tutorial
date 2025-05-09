package jsl.group.microservices.core.product;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresBaseTests {
    private static final PostgreSQLContainer database = new PostgreSQLContainer<>("postgres").withConnectTimeoutSeconds(300);

    static { database.start(); }

    @DynamicPropertySource
    public static void dataSource(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", database::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.password", database::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.username", database::getUsername);
    }
}
