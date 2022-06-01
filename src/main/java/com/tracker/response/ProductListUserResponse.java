package com.tracker.response;

// USING IT FOR SHOWING THE PRODUCT DETAILS TO THE USER
public class ProductListUserResponse {

	private String id;
	private String name;
	private int price;
	private String description;

	public ProductListUserResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductListUserResponse(String id, String name, int price, String description) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UserProductViewJson [id=" + id + ", name=" + name + ", price=" + price + ", description=" + description
				+ "]";
	}

}
