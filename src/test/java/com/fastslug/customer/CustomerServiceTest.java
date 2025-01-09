package com.fastslug.customer;

import com.fastslug.exception.DuplicateResourceException;
import com.fastslug.exception.RequestValidationException;
import com.fastslug.exception.ResourceNotFound;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

	@Mock
	private CustomerDao customerDaoMock;

	private CustomerService underTest;
	private AutoCloseable autoCloseable;

	@BeforeEach
	void setUp() {
		autoCloseable = MockitoAnnotations.openMocks(this);
		underTest = new CustomerService(customerDaoMock);
	}

	@AfterEach
	void tearDown() throws Exception {
		autoCloseable.close();
	}

	@Test
	void getAllCustomers() {
		// when
		underTest.getAllCustomers();

		// then
		verify(customerDaoMock)
				.selectAllCustomers();
	}

	@Test
	void getCustomerById() {
		// given
		Long id = 1L;
		Customer customer = new Customer(
				id,
				"customer",
				19,
				"test@email.com"
		);

		when(customerDaoMock.selectCustomerById(id.intValue())).thenReturn(Optional.of(customer));

		// when
		Customer customerById = underTest.getCustomerById(id.intValue());

		// then
		assertThat(customerById).isEqualTo(customer);
		verify(customerDaoMock)
				.selectCustomerById(id.intValue());
	}

	@Test
	void willThrowWhenGetCustomerByIdReturnsEmptyOptional() {
		// given
		long id = 1L;

		when(customerDaoMock.selectCustomerById((int) id)).thenReturn(Optional.empty());

		// when
		// then
		assertThatThrownBy(() -> underTest.getCustomerById((int) id))
				.isInstanceOf(ResourceNotFound.class)
				.hasMessage("Customer with id [%s] not found".formatted(id));
	}

	@Test
	void addCustomer() {
		// given
		String name = "name";
		String email = "email@email.com";
		Integer age = 23;
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

		when(customerDaoMock.checkCustomerEmailExists(email)).thenReturn(false);

		// when
		underTest.addCustomer(request);

		// then
		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
				Customer.class
		);

		verify(customerDaoMock)
				.insertCustomer(customerArgumentCaptor.capture());

		assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(name);
		assertThat(customerArgumentCaptor.getValue().getEmail()).isEqualTo(email);
		assertThat(customerArgumentCaptor.getValue().getAge()).isEqualTo(age);
	}

	@Test
	void WillThrowEmailExistsAddCustomer() {
		// given
		String name = "name";
		String email = "email@email.com";
		Integer age = 23;
		CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

		when(customerDaoMock.checkCustomerEmailExists(email)).thenReturn(true);

		// when
		// then
		assertThatThrownBy(() -> underTest.addCustomer(request))
				.isInstanceOf(DuplicateResourceException.class)
				.hasMessage("Customer with email [%s] already exists".formatted(email));

		verify(customerDaoMock, never()).insertCustomer(any());

	}

	@Test
	void deleteCustomerById() {
		// given
		long id = 1L;
		when(customerDaoMock.checkCustomerIdExists((int) id)).thenReturn(true);

		// when
		underTest.deleteCustomerById((int) id);

		// then
		verify(customerDaoMock).deleteCustomerById((int) id);
	}

	@Test
	void willThrowResourceNotFoundDeleteCustomerById() {
		// given
		long id = 1L;
		when(customerDaoMock.checkCustomerIdExists((int) id)).thenReturn(false);

		// when
		// then
		assertThatThrownBy(() -> underTest.deleteCustomerById((int) id))
				.isInstanceOf(ResourceNotFound.class)
				.hasMessage("Customer with id [%s] not found".formatted(id));

		verify(customerDaoMock, never()).deleteCustomerById(any());
	}

	@Test
	void willThrowValidationExceptionUpdateCustomer() {
		// given
		String name = "name";
		String email = "email@email.com";
		Integer age = 23;
		long customerId = 1L;
		CustomerUpdateRequest request = new CustomerUpdateRequest(name, email, age);

		Customer customer = new Customer(
				customerId,
				name,
				age,
				email
		);

		when(customerDaoMock.selectCustomerById((int) customerId)).thenReturn(Optional.of(customer));

		// when
		// then
		assertThatThrownBy(() -> underTest.updateCustomer(request, (int) customerId))
				.isInstanceOf(RequestValidationException.class)
				.hasMessage("no changes found");

	}

	@Test
	void updateCustomer() {
		// given
		String name = "name";
		String email = "email@email.com";
		Integer age = 23;
		long customerId = 1L;
		CustomerUpdateRequest request = new CustomerUpdateRequest(name, email, age);

		Customer currentCustomer = new Customer(
				customerId,
				"alex",
				21,
				"email@test.com"
		);

		when(customerDaoMock.selectCustomerById((int) customerId)).thenReturn(Optional.of(currentCustomer));

		// when
		underTest.updateCustomer(request, (int) customerId);

		// then
		ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
				Customer.class
		);

		verify(customerDaoMock).updateCustomer(customerArgumentCaptor.capture());
		assertThat(customerArgumentCaptor.getValue().getEmail()).isEqualTo(request.email());
		assertThat(customerArgumentCaptor.getValue().getName()).isEqualTo(request.name());
		assertThat(customerArgumentCaptor.getValue().getAge()).isEqualTo(request.age());
	}
}