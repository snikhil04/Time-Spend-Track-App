package com.tracker.dao;

import java.util.List;
import java.util.Optional;

import com.tracker.entities.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tracker.entities.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query(value = "select id from product_category where category=?", nativeQuery = true)
    List<String> getProductIdByCategory(String category);

    @Query(value = "select * from product where product_category_id=?", nativeQuery = true)
    Product getProductByProductCategoryId(String productCategoryid);

    @Query(value = "select * from product where id =?", nativeQuery = true)
    Optional<Product> findById(String id);

    @Query(value = "select * from product_category where category=?", nativeQuery = true)
    List<ProductCategory> CheckProductExist(String category);
}