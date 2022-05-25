package com.tracker.request;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

public class AddProductRequestDto {

	@Size(min = 5, max = 15, message = "Product name must be min 5 characters!")
	@Column(nullable = true)
	private String name;


	@Column(nullable = true)
	@Min(value = 1)
	private int price;


	@Column(nullable = true)
	@Size(max = 50, message = "description of the product can be max 50 characters")
	private String Description;


	@Column(nullable = true)
	@Size(min = 3, max = 15, message = "product category must be min 3 characters")
	private String category;

	@Min(value = 1)
	@Column(nullable = true)
	private int quantity;

	public AddProductRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AddProductRequestDto(@Size(min = 5, max = 15, message = "Product name must be min 5 characters!") String name,
								@Min(1) int price,
								@Size(max = 50, message = "description of the product can be max 50 characters") String description,
								@Size(min = 3, max = 15, message = "product category must be min 3 characters") String category,
								int quantity) {
		super();
		this.name = name;
		this.price = price;
		Description = description;
		this.category = category;
		this.quantity = quantity;
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
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "ProductDto [name=" + name + ", price=" + price + ", Description=" + Description + ", category="
				+ category + ", quantity=" + quantity + "]";
	}

}
