package com.tracker.entities;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "purchase_history")
public class PurchaseHistory {

    @Id
    private String id;

    private String productId;

    private int totalPrice;
    private String productName;
    private String userId;
    private LocalDateTime purchaseDate;
    private int productPrice;
    private int quantity;

    public PurchaseHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

    public PurchaseHistory(String id, String productId, int totalPrice, String productName, String userId, LocalDateTime purchaseDate, int productPrice, int quantity) {
        this.id = id;
        this.productId = productId;
        this.totalPrice = totalPrice;
        this.productName = productName;
        this.userId = userId;
        this.purchaseDate = purchaseDate;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "PurchaseHistory{" +
                "id='" + id + '\'' +
                ", productId=" + productId +
                ", totalPrice=" + totalPrice +
                ", productName='" + productName + '\'' +
                ", userId=" + userId +
                ", purchaseDate=" + purchaseDate +
                ", productPrice=" + productPrice +
                ", quantity=" + quantity +
                '}';
    }
}