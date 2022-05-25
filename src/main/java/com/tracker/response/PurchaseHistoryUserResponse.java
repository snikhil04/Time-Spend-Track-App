package com.tracker.response;

import java.time.LocalDateTime;

public class PurchaseHistoryUserResponse {

    private String ProductName;
    private int Price;
    private int Quantity;
    private int totalPrice;

    private LocalDateTime PurchaseDate;

    public PurchaseHistoryUserResponse() {
    }

    public PurchaseHistoryUserResponse(String productName, int price, int quantity, int totalPrice, LocalDateTime purchaseDate) {
        ProductName = productName;
        Price = price;
        Quantity = quantity;
        this.totalPrice = totalPrice;
        PurchaseDate = purchaseDate;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    @Override
    public String toString() {
        return "PurchaseHistoryResponse{" +
                "ProductName='" + ProductName + '\'' +
                ", Price=" + Price +
                ", Quantity=" + Quantity +
                ", totalPrice=" + totalPrice +
                ", PurchaseDate=" + PurchaseDate +
                '}';
    }
}
