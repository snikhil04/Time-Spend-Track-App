package com.tracker.request;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class AdminUpdatingUserRequestDto {

	@Size(min = 5, max = 30, message = "name should contains min 5 characters..")
	private String name;

	@Email(regexp = "^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$", message = "only @gmail is accepted!")
	private String email;

	@Size(min = 8, max = 20, message = "name should contains min 5 characters..")
	private String password;

	@Column(nullable = true)
	private String Role;

	@Column(nullable = true)
	private boolean isActive;

	@Column(nullable = true)
	private long currency;

	public AdminUpdatingUserRequestDto() {
		super();
	}

	public AdminUpdatingUserRequestDto(@Size(min = 5, max = 30, message = "name should contains min 5 characters..") String name,
									   @Email(regexp = "^[a-z0-9](\\.?[a-z0-9]){5,}@g(oogle)?mail\\.com$", message = "only @gmail is accepted!") String email,
									   @Size(min = 8, max = 20, message = "name should contains min 5 characters..") String password, String role,
									   boolean isActive, long currency) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		Role = role;
		this.isActive = isActive;
		this.currency = currency;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return Role;
	}

	public void setRole(String role) {
		Role = role;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "userDto [name=" + name + ", email=" + email + ", password=" + password + "]";
	}

}