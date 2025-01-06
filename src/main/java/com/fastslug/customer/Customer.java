package com.fastslug.customer;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
		name = "customer",
		uniqueConstraints = {
				@UniqueConstraint(
						name = "customer_email_unique",
						columnNames = "email"
				)
		}
)
public class Customer {

	@Id
	@SequenceGenerator(
			name = "customer_id_seq",
			sequenceName = "customer_id_seq",
			allocationSize = 1
	)
	@GeneratedValue(
			strategy = GenerationType.SEQUENCE,
			generator = "customer_id_seq"
	)
	private Long id;

	@Column(
			nullable = false
	)
	private Integer age;

	@Column(
			nullable = false
	)
	private String email;

	@Column(
			nullable = false
	)
	private String name;

	public Customer(Long id, String name, Integer age, String email) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.email = email;
	}

	public Customer(String name, Integer age, String email) {
		this.name = name;
		this.age = age;
		this.email = email;
	}

	public Customer() {

	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return Objects.equals(id, customer.id) && Objects.equals(age, customer.age) && Objects.equals(email, customer.email) && Objects.equals(name, customer.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, age, email, name);
	}

	@Override
	public String toString() {
		return "Customer{" +
				"id=" + id +
				", age=" + age +
				", email='" + email + '\'' +
				", name='" + name + '\'' +
				'}';
	}
}
