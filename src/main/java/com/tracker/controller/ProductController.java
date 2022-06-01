package com.tracker.controller;

import java.util.List;
import java.util.Optional;

import com.tracker.dao.UserRepo;
import com.tracker.entities.PurchaseHistory;
import com.tracker.entities.User;
import com.tracker.exceptionhandler.ValidationException;
import com.tracker.response.AllUsersPurchaseHistoryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tracker.request.AddProductRequestDto;
import com.tracker.entities.Product;
import com.tracker.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private AdminService adminService;

    // GETTING THE LIST OF ALL PRODUCTS
    @GetMapping("/get")
    @Operation(summary = "Getting All Of Products")
    public List<Product> ListOfProducts() {
        return this.adminService.getAllProducts();
    }

    // GETTING PRODUCTS BASED ON PRODUCT CATEGORY
    @Operation(summary = "Getting Products Based on Category")
    @GetMapping("/get/{category}")
    public List<Product> ProductsByCategory(@PathVariable("category") String category) {
        return this.adminService.getProductsByCategory(category);
    }

    // ADDING PRODUCT IN DATABASE
    @PostMapping("/add")
    @Operation(summary = "Adding Product")
    public Product AddProduct(@RequestBody AddProductRequestDto addProductRequestDto) {
        return adminService.AddProduct(addProductRequestDto);
    }

    // UPDATING PRODUCT
    @PatchMapping("/update/{id}")
    @Operation(summary = "Updating Product")
    public Product UpdateProduct(@RequestBody AddProductRequestDto productdto,@PathVariable("id") String id) {
        return this.adminService.UpdateProduct(productdto, id);
    }

    // DELETING A PARTICULAR PRODUCT
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deleting Product")
    public Optional<Product> deleteProduct(@PathVariable("id") String id) {
        return this.adminService.DeleteProduct(id);
    }
}