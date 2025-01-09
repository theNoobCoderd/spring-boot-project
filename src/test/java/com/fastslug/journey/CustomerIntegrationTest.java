package com.fastslug.journey;

import com.fastslug.customer.Customer;
import com.fastslug.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CustomerIntegrationTest {

	@Autowired
	private WebTestClient webClient;

	private static final String CUSTOMER_URI = "/api/v1/customers";

	@Test
	void canRegisterACustomer() {
		Faker faker = new Faker();
		String name = faker.name().firstName();
		String email = faker.internet().emailAddress() + UUID.randomUUID();
		int age = faker.number().numberBetween(18, 120);


		CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

		webClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus().isOk();

		List<Customer> allCustomers = webClient.get()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(new ParameterizedTypeReference<Customer>() {})
				.returnResult()
				.getResponseBody();

		Customer expectedCustomer = new Customer(
				name, age, email
		);

		Assertions.assertThat(allCustomers)
				.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
				.contains(expectedCustomer);

		var id = allCustomers.stream()
				.filter(customer -> customer.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		expectedCustomer.setId(id);

		webClient.get()
				.uri(CUSTOMER_URI + "/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBody(new ParameterizedTypeReference<Customer>() {})
				.isEqualTo(expectedCustomer);
	}

	@Test
	void canDeleteCustomer() {
		Faker faker = new Faker();
		String name = faker.name().firstName();
		String email = faker.internet().emailAddress() + UUID.randomUUID();
		int age = faker.number().numberBetween(18, 120);


		CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);

		webClient.post()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(request), CustomerRegistrationRequest.class)
				.exchange()
				.expectStatus().isOk();

		List<Customer> allCustomers = webClient.get()
				.uri(CUSTOMER_URI)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk()
				.expectBodyList(new ParameterizedTypeReference<Customer>() {})
				.returnResult()
				.getResponseBody();

		var id = allCustomers.stream()
				.filter(customer -> customer.getEmail().equals(email))
				.map(Customer::getId)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		webClient.delete()
				.uri(CUSTOMER_URI + "/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isOk();


		webClient.get()
				.uri(CUSTOMER_URI + "/{id}", id)
				.accept(MediaType.APPLICATION_JSON)
				.exchange()
				.expectStatus().isNotFound();
	}
}
