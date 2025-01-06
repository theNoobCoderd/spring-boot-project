package com.fastslug;

import com.fastslug.customer.CustomerJDBCDataAccessService;
import com.github.javafaker.Faker;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestContainer {

	@BeforeAll
	static void setup() {
		Flyway flyway = Flyway
				.configure()
				.dataSource(
						postgresContainer.getJdbcUrl(),
						postgresContainer.getUsername(),
						postgresContainer.getPassword()
				).load();

		flyway.migrate();
	}

	@Container
	protected static final PostgreSQLContainer<?> postgresContainer =
			new PostgreSQLContainer<>("postgres:16")
					.withDatabaseName("fastslug-dao-unit-test")
					.withUsername("fastslug")
					.withPassword("password");

	@DynamicPropertySource
	private static void registerDataSourceProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
		registry.add("spring.datasource.password", postgresContainer::getPassword);
		registry.add("spring.datasource.username", postgresContainer::getUsername);
	}

	private static DataSource getDataSource() {
		return DataSourceBuilder.create()
				.driverClassName(postgresContainer.getDriverClassName())
				.url(postgresContainer.getJdbcUrl())
				.username(postgresContainer.getUsername())
				.password(postgresContainer.getPassword())
				.build();
	}

	protected static JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(getDataSource());
	}

	protected static final Faker FAKER = new Faker();
}
