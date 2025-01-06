package com.fastslug.customer;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface CustomerDao {
	List<Customer> selectAllCustomers();

	Optional<Customer> selectCustomerById(Integer id);

	void insertCustomer(Customer customer);

	boolean checkCustomerEmailExists(String email);

	boolean checkCustomerIdExists(Integer id);

	void deleteCustomerById(Integer id);

	void updateCustomer(Customer customer);
}
