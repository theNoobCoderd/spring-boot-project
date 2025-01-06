package com.fastslug.customer;

import com.fastslug.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainer {

	private CustomerJDBCDataAccessService underTest;
	private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

	@BeforeEach
	void setUp() {
		underTest = new CustomerJDBCDataAccessService(
				getJdbcTemplate(),
				customerRowMapper
		);
	}

	@Test
	void selectAllCustomers() {
		// given
		Customer customer = new Customer(
				FAKER.name().fullName(),
				20,
				FAKER.internet().emailAddress() + UUID.randomUUID()
		);
		underTest.insertCustomer(customer);

		// when
		List<Customer> customers = underTest.selectAllCustomers();

		// then
		assertThat(customers).isNotEmpty();
	}

	@Test
	void selectCustomerById() {
	}

	@Test
	void insertCustomer() {
	}

	@Test
	void checkCustomerEmailExists() {
	}

	@Test
	void checkCustomerIdExists() {
	}

	@Test
	void deleteCustomerById() {
	}

	@Test
	void updateCustomer() {
	}
}