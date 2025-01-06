package com.fastslug.customer;

import com.fastslug.exception.DuplicateResourceException;
import com.fastslug.exception.RequestValidationException;
import com.fastslug.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

	private final CustomerDao customerDao;

	public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
		this.customerDao = customerDao;
	}

	public List<Customer> getAllCustomers() {
		return customerDao.selectAllCustomers();
	}

	public Customer getCustomerById(Integer id) {
		return customerDao.selectCustomerById(id)
				.orElseThrow(() -> new ResourceNotFound("Customer with id [%s] not found".formatted(id)));
	}

	public void addCustomer(CustomerRegistrationRequest registrationRequest) {
		String customerEmail = registrationRequest.email();
		checkEmailExists(customerEmail);

		customerDao.insertCustomer(new Customer(registrationRequest.name(), registrationRequest.age() ,registrationRequest.email()));
	}

	public void deleteCustomerById(Integer customerId) {
		if (!customerDao.checkCustomerIdExists(customerId)) {
			throw new ResourceNotFound("Customer with id [%s] not found".formatted(customerId));
		}

		customerDao.deleteCustomerById(customerId);
	}

	public void updateCustomer(CustomerUpdateRequest updateRequest, Integer customerId) {
		Customer customer = getCustomerById(customerId);

		boolean changes = false;

		if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
			customer.setName(updateRequest.name());
			changes = true;
		}

		if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
			customer.setAge(updateRequest.age());
			changes = true;
		}

		if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
			checkEmailExists(updateRequest.email());

			customer.setEmail(updateRequest.email());
			changes = true;
		}

		if (!changes) {
			throw new RequestValidationException("no changes found");
		}

		customerDao.updateCustomer(customer);
	}

	private void checkEmailExists(String email) {
		if (customerDao.checkCustomerEmailExists(email)) {
			throw new DuplicateResourceException("Customer with email [%s] already exists".formatted(email));
		}
	}

}