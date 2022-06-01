package com.tracker.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class User {

	@Id
	private String id = IdGenerator.Id();

	@ManyToOne(cascade = CascadeType.ALL)
	private Role role;

	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "userwalletId")
	private UserWallet userWallet;

	private String name;

	@Column(unique = true)
	private String email;
	private String password;

	private boolean isActive;

	public User() {
		super();
	}

	public User(Role role, UserWallet userWallet, String name, String email, String password, boolean isActive) {
		super();
		this.role = role;
		this.userWallet = userWallet;
		this.name = name;
		this.email = email;
		this.password = password;
		this.isActive = isActive;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public UserWallet getUserWallet() {
		return userWallet;
	}

	public void setUserWallet(UserWallet userWallet) {
		this.userWallet = userWallet;
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

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", role=" + role + ", userWallet=" + userWallet + ", name=" + name + ", email="
				+ email + ", password=" + password + ", isActive=" + isActive + "]";
	}

}