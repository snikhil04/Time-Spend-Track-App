package com.tracker.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserWallet {

	@Id
	private String id = IdGenerator.Id();;

	private long currency;

	public UserWallet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserWallet(long currency) {
		super();
		this.currency = currency;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "UserWallet [id=" + id + ", currency=" + currency + "]";
	}

}
