package com.tracker.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private long currency;

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(long currency) {
		super();
		this.currency = currency;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getcurrency() {
		return currency;
	}

	public void setcurrency(long currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Admin [id=" + id + ", currency=" + currency + "]";
	}

}
