package com.fastslug;

import com.fastslug.customer.Customer;
import com.fastslug.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository) {
		return args -> {
			Customer customer1 = new Customer("Doe", 54, "test@email.com");
			Customer customer2 = new Customer("Alex", 25, "alex@email.com");
//			customerRepository.saveAll(List.of(customer1, customer2));
		};
	}

}
