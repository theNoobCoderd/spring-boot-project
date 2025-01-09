package com.fastslug.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

	@Test
	void mapRow() throws SQLException {
		// given
		CustomerRowMapper underTest = new CustomerRowMapper();

		ResultSet resultSet = mock(ResultSet.class);
		when(resultSet.getLong("id")).thenReturn(1L);
		when(resultSet.getString("name")).thenReturn("name");
		when(resultSet.getString("email")).thenReturn("email@email.com");
		when(resultSet.getInt("age")).thenReturn(23);

		// when
		Customer customer = underTest.mapRow(resultSet, 1);

		// then
		Customer expected = new Customer(
				1L, "name",23, "email@email.com"
		);

		assertEquals(expected, customer);
	}
}