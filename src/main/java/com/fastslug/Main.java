package com.fastslug;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
public class Main {

	private static List<Customer> customers;

	static {
		customers = new ArrayList<>();
		Customer alex = new Customer(1, "alex", 21, "alex@email.com");
		Customer jasmine = new Customer(2, "jasmine", 28, "jasmine@email.com");

		customers.add(alex);
		customers.add(jasmine);
	}

	public static void main(String[] args) {
		System.out.println(customers);
		SpringApplication.run(Main.class, args);
	}

	static class Customer {
		private Integer Id;

		private Integer age;
		private String email;
		private String name;

		public Customer(Integer id, String name, Integer age, String email) {
			Id = id;
			this.name = name;
			this.age = age;
			this.email = email;
		}


		public Integer getId() {
			return Id;
		}

		public void setId(Integer id) {
			Id = id;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			Customer customer = (Customer) o;
			return Objects.equals(Id, customer.Id) && Objects.equals(age, customer.age) && Objects.equals(email, customer.email) && Objects.equals(name, customer.name);
		}

		@Override
		public int hashCode() {
			return Objects.hash(Id, age, email, name);
		}

		@Override
		public String toString() {
			return "Customer{" +
					"Id=" + Id +
					", age=" + age +
					", email='" + email + '\'' +
					", name='" + name + '\'' +
					'}';
		}
	}

}
