package com.tracker.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Role {

	@Id
	private String id = IdGenerator.Id();

	private String UserRole;

	public Role() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Role(String userRole) {
		super();
		UserRole = userRole;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserRole() {
		return UserRole;
	}

	public void setUserRole(String userRole) {
		UserRole = userRole;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", UserRole=" + UserRole + "]";
	}

}