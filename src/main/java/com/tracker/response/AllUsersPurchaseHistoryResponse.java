package com.tracker.response;

import java.time.LocalDateTime;

public class AllUsersPurchaseHistoryResponse {

    private String id;
    private String productId;
    private int totalprice;
    private String productName;
    private String userId;
    private String UserName;
    private LocalDateTime purchasedDate;
    private int productPrice;
    private int quantity;

    public AllUsersPurchaseHistoryResponse() {
    }

    public AllUsersPurchaseHistoryResponse(String id, String productId, int totalprice, String productName, String userId, LocalDateTime purchasedDate, int productPrice, int quantity, String userName) {
        this.id = id;
        this.productId = productId;
        this.totalprice = totalprice;
        this.productName = productName;
        this.userId = userId;
        this.purchasedDate = purchasedDate;
        this.productPrice = productPrice;
        this.quantity = quantity;
        UserName = userName;
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

    public int getTotalprice() {
        return totalprice;
    }

    public void setTotalprice(int totalprice) {
        this.totalprice = totalprice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getPurchasedDate() {
        return purchasedDate;
    }

    public void setPurchasedDate(LocalDateTime purchasedDate) {
        this.purchasedDate = purchasedDate;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    @Override
    public String toString() {
        return "AllUsersPurchaseHistoryResponse{" +
                "id='" + id + '\'' +
                ", productId=" + productId +
                ", totalprice=" + totalprice +
                ", productName='" + productName + '\'' +
                ", userId=" + userId +
                ", purchasedDate=" + purchasedDate +
                ", productPrice=" + productPrice +
                ", quantity=" + quantity +
                ", UserName='" + UserName + '\'' +
                '}';
    }
}
