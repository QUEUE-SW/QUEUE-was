package com.queuewas;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest
public abstract class TestDatabaseConfig {

	private static final String MYSQL_CONTAINER_IMAGE = "mysql:8.0.35";
	private static final MySQLContainer MYSQL_CONTAINER;

	static {
		MYSQL_CONTAINER = new MySQLContainer(DockerImageName.parse(MYSQL_CONTAINER_IMAGE));
		MYSQL_CONTAINER.start();
	}

	@DynamicPropertySource
	public static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
		registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
	}
}
