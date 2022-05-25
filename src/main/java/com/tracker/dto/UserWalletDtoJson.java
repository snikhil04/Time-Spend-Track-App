package com.tracker.dto;

//USING IT FOR UPDATING PARTICULAR USER WALLET
public class UserWalletDtoJson {

	private long currency;

	public UserWalletDtoJson() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserWalletDtoJson(long currency) {
		super();
		this.currency = currency;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "UserWalletJson [currency=" + currency + "]";
	}

}