package com.example.portfolio.access.service;

public class UsernameAlreadyExistsException extends RuntimeException {
	public UsernameAlreadyExistsException(String username) {
		super("User with username '" + username + "' already exists.");
	}
}