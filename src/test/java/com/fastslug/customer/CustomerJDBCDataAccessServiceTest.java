package com.fastslug.customer;

import com.fastslug.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
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
		List<Customer> actualCustomers = underTest.selectAllCustomers();

		// then
		assertThat(actualCustomers).isNotEmpty();
	}

	@Test
	void selectCustomerById() {
		// given
		String email = FAKER.internet().emailAddress() + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				20,
				email
		);

		underTest.insertCustomer(customer);

		Long id = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		// when
		Optional<Customer> actualCustomer = underTest.selectCustomerById(id.intValue());

		// then
		assertThat(actualCustomer).isPresent().hasValueSatisfying(c -> {
			assertThat(c.getId()).isEqualTo(id);
			assertThat(c.getEmail()).isEqualTo(customer.getEmail());
			assertThat(c.getName()).isEqualTo(customer.getName());
			assertThat(c.getAge()).isEqualTo(customer.getAge());
		});
	}

	@Test
	void willReturnEmptyWhenSelectCustomerById() {
		// given
		int id = -1;

		// when
		var actualCustomer = underTest.selectCustomerById(id);

		// then
		assertThat(actualCustomer).isEmpty();
	}

	@Test
	void checkCustomerEmailExists() {
		// given
		String name = FAKER.name().fullName();
		String email = FAKER.internet().emailAddress() + UUID.randomUUID();
		Customer customer = new Customer(
				name,
				20,
				email
		);
		underTest.insertCustomer(customer);

		// when
		boolean result = underTest.checkCustomerEmailExists(email);

		// then
		assertThat(result).isTrue();
	}

	@Test
	void checkCustomerEmailDoesNotExist() {
		// given
		String email = FAKER.internet().emailAddress() + UUID.randomUUID();

		// when
		boolean result = underTest.checkCustomerEmailExists(email);

		// then
		assertThat(result).isFalse();
	}

	@Test
	void checkCustomerIdExists() {
		// given
		String name = FAKER.name().fullName();
		String email = FAKER.internet().emailAddress() + UUID.randomUUID();
		int age = 20;
		Customer customer = new Customer(
				name,
				age,
				email
		);
		underTest.insertCustomer(customer);

		var actualId = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		// when
		boolean result = underTest.checkCustomerIdExists(actualId.intValue());


		// then
		assertThat(result).isTrue();
	}

	@Test
	void checkCustomerIdDoesNotExist() {
		// given
		int id = -1;

		// when
		boolean result = underTest.checkCustomerIdExists(id);

		// then
		assertThat(result).isFalse();
	}

	@Test
	void deleteCustomerById() {
		// given
		String email = FAKER.internet().emailAddress() + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				20,
				email);

		underTest.insertCustomer(customer);

		var actualId = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		// when
		underTest.deleteCustomerById(actualId.intValue());

		// then
		var actualCustomer = underTest.selectCustomerById(actualId.intValue());
		assertThat(actualCustomer).isNotPresent();
	}

	@Test
	void updateCustomer() {
		// given
		String email = FAKER.internet().emailAddress() + UUID.randomUUID();
		Customer customer = new Customer(
				FAKER.name().fullName(),
				20,
				email);

		underTest.insertCustomer(customer);

		var actualId = underTest.selectAllCustomers()
				.stream()
				.filter(c -> c.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow();

		Customer modifiedCustomer = new Customer();
		int modifiedAge = 21;
		String modifiedName = "Anne";
		modifiedCustomer.setId(actualId);
		modifiedCustomer.setAge(modifiedAge);
		modifiedCustomer.setName(modifiedName);

		// when
		underTest.updateCustomer(modifiedCustomer);

		// then

		var actualCustomer = underTest.selectCustomerById(actualId.intValue());

		assertThat(actualCustomer).isPresent().hasValueSatisfying(c -> {
			assertThat(c.getAge()).isEqualTo(modifiedAge);
			assertThat(c.getName()).isEqualTo(modifiedName);
		});

	}
}