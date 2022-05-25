package com.tracker.dto;

public class ProductPurchaseUserRequestDto {

	private String productid;
	private int productquantity;

	public ProductPurchaseUserRequestDto() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProductPurchaseUserRequestDto(String productid, int productquantity) {
		super();
		this.productid = productid;
		this.productquantity = productquantity;
	}

	public String getProductid() {
		return productid;
	}

	public void setProductid(String productid) {
		this.productid = productid;
	}

	public int getProductquantity() {
		return productquantity;
	}

	public void setProductquantity(int productquantity) {
		this.productquantity = productquantity;
	}

	@Override
	public String toString() {
		return "UserPurchaseDtoRequest [productid=" + productid + ", productquantity=" + productquantity + "]";
	}

}
