package com.fastslug.customer;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

	private final CustomerService customerService;

	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@GetMapping
	public List<Customer> getCustomers() {
		return customerService.getAllCustomers();
	}

	@GetMapping("{customerId}")
	public Customer getCustomerById(@PathVariable("customerId") Integer customerId) {
		return customerService.getCustomerById(customerId);
	}

	@PostMapping()
	public void registerCustomer(@RequestBody CustomerRegistrationRequest request) {
		customerService.addCustomer(request);
	}

	@DeleteMapping("{customerId}")
	public void deleteCustomer(@PathVariable("customerId") Integer customerId) {
		customerService.deleteCustomerById(customerId);
	}

	@PutMapping("{customerId}")
	public void updateCustomer(@RequestBody CustomerUpdateRequest request, @PathVariable Integer customerId) {
		customerService.updateCustomer(request, customerId);
	}
}
