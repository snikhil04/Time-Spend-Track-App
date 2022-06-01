package com.tracker.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Product {

    @Id
    private String id =IdGenerator.Id();

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productCategory_id")
    private ProductCategory productCategory;

    private String Name;
    private String Description;
    private int price;

    private int quantity;

    public Product() {
        super();
        // TODO Auto-generated constructor stub
    }

    public Product(ProductCategory productCategory, String name, String description, int price, int quantity) {
        super();
        this.productCategory = productCategory;
        Name = name;
        Description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", productCategory=" + productCategory + ", Name=" + Name + ", Description="
                + Description + ", price=" + price + ", quantity=" + quantity + "]";
    }

}