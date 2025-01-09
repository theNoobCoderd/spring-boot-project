package com.fastslug.customer;

import com.fastslug.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainer {

	@Autowired
	private CustomerRepository underTest;

	@Autowired
	private ApplicationContext applicationContext;

	@BeforeEach
	void setUp() {
		underTest.deleteAll();
	}

	@Test
	void existsCustomerByEmail() {
		// given
		String name = FAKER.name().fullName();
		String email = FAKER.internet().emailAddress() + UUID.randomUUID();
		Customer customer = new Customer(
				name,
				20,
				email
		);
		underTest.save(customer);

		// when
		boolean result = underTest.existsCustomerByEmail(email);

		// then
		assertThat(result).isTrue();
	}

	@Test
	void existsCustomerById() {
		// given
		String email = FAKER.internet().emailAddress() + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				20,
				email
		);

		underTest.save(customer);

		Long id = underTest.findAll()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		// when
		var actualCustomer = underTest.existsCustomerById(id.intValue());

		// then
		assertThat(actualCustomer).isTrue();
	}
}