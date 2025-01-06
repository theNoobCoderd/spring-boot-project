package com.fastslug.customer;

public record CustomerUpdateRequest(String name, String email, Integer age) {
}
