package com.fastslug.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {

	private CustomerJPADataAccessService underTest;
	private AutoCloseable autoCloseable;

	@Mock
	private CustomerRepository customerRepository;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		underTest = new CustomerJPADataAccessService(customerRepository);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void selectAllCustomers() {
		// when
		underTest.selectAllCustomers();

		// then
		verify(customerRepository)
				.findAll();
	}

	@Test
	void selectCustomerById() {
		// given
		var id = 1;

		// when
		underTest.selectCustomerById(id);

		// then
		verify(customerRepository)
				.findById(id);
	}

	@Test
	void insertCustomer() {
		// given
		String name = "Fullname";
		String email = "email@email.com";
		Customer customer = new Customer(
				name,
				20,
				email
		);

		// when
		underTest.insertCustomer(customer);

		// then
		verify(customerRepository)
				.save(customer);
	}

	@Test
	void checkCustomerEmailExists() {
		// given
		String email = "email@email.com";

		// when
		underTest.checkCustomerEmailExists(email);

		// then
		verify(customerRepository)
				.existsCustomerByEmail(email);
	}

	@Test
	void checkCustomerIdExists() {
		// given
		Integer id = 1;

		// when
		underTest.checkCustomerIdExists(id);

		// then
		verify(customerRepository)
				.existsCustomerById(id);
	}

	@Test
	void deleteCustomerById() {
		// given
		Integer id = 1;

		// when
		underTest.deleteCustomerById(id);

		// then
		verify(customerRepository)
				.deleteById(id);
	}

	@Test
	void updateCustomer() {
		// given
		String name = "name";
		String email = "email@email.com";
		Customer customer = new Customer(
				name,
				20,
				email
		);

		// when
		underTest.updateCustomer(customer);

		// then
		verify(customerRepository)
				.save(customer);
	}
}