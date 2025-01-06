package com.fastslug.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
	static List<Customer> customers;

	static {
		customers = new ArrayList<>();
		Customer alex = new Customer(1L, "alex", 21, "alex@email.com");
		Customer jasmine = new Customer(2L, "jasmine", 28, "jasmine@email.com");

		customers.add(alex);
		customers.add(jasmine);
	}

	@Override
	public List<Customer> selectAllCustomers() {
		return customers;
	}

	@Override
	public Optional<Customer> selectCustomerById(Integer customerId) {
		return customers.stream()
				.filter(customer -> customer.getId().equals(customerId))
				.findFirst();
	}

	@Override
	public void insertCustomer(Customer customer) {
		customers.add(customer);
	}

	@Override
	public boolean checkCustomerEmailExists(String email) {
		return customers.stream().anyMatch(customer -> customer.getEmail().equals(email));
	}

	@Override
	public boolean checkCustomerIdExists(Integer id) {
		return customers.stream().anyMatch(customer -> customer.getId().equals(id));
	}

	@Override
	public void deleteCustomerById(Integer id) {
		customers.removeIf(customer -> customer.getId().equals(id));
	}

	@Override
	public void updateCustomer(Customer customer) {
		customers.add(customer);
	}
}
