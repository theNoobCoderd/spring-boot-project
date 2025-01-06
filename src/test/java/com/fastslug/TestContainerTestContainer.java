package com.fastslug;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class TestContainerTestContainer extends AbstractTestContainer {

	@Test
	void canStartPostgresDB() {
		Assertions.assertThat(postgresContainer.isRunning()).isTrue();
		Assertions.assertThat(postgresContainer.isCreated()).isTrue();
	}
}
