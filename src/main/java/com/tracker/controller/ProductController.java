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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.request.AddProductRequestDto;
import com.tracker.entities.Product;
import com.tracker.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/admin/product")
public class ProductController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepo userrepo;

    // GETTING THE LIST OF ALL PRODUCTS

    @GetMapping("/get")
    @Operation(summary = "Getting All Of Products")
    public ResponseEntity<List<Product>> ListOfProducts() {
        List<Product> products = this.adminService.getAllProducts();
        if (products.size() == 0) {
            throw new ValidationException(404,"Product Not Found");
        } else {
            return ResponseEntity.ok(products);
        }
    }

    // GETTING PRODUCTS BASED ON PRODUCT CATEGORY

    @Operation(summary = "Getting Products Based on Category")
    @GetMapping("/get/{category}")
    public ResponseEntity<List<Product>> ProductsByCategory(@PathVariable("category") String category) {
        List<Product> products = this.adminService.getProductsByCategory(category);
        if (products.size()>0) {
            return ResponseEntity.ok(products);

        } else {
            throw new ValidationException(404,"Product Not Found");
        }
    }

    // ADDING PRODUCT IN DATABASE

    @PostMapping("/add/{category}")
    @Operation(summary = "Adding Product")
    public ResponseEntity<Product> AddProduct(@RequestBody AddProductRequestDto addProductRequestDto,
                                              @PathVariable("category") String category) {
        Product p = adminService.AddProduct(addProductRequestDto, category);
        if (p != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(p);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(p);
        }
    }

    // UPDATING PRODUCT IN DATABASE

    @PutMapping("/update/{id}")
    @Operation(summary = "Updating Product")
    public ResponseEntity<Product> UpdateProduct(@RequestBody AddProductRequestDto productdto,
                                                           @PathVariable("id") String id) {
        Product p = this.adminService.UpdateProduct(productdto, id);
        if (p!=null) {
            return ResponseEntity.ok(p);
        } else {
            throw new ValidationException(404,"Product Not Found");
        }
    }

    // DELETING A PARTICULAR PRODUCT

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Deleting Product")
    public ResponseEntity<Optional<Product>> deleteProduct(@PathVariable("id") String id) {

        Optional<Product> product = this.adminService.DeleteProduct(id);
        if (product != null) {
            return ResponseEntity.ok(product);
        } else {
            throw new ValidationException(404,"Product Not Found");
        }
    }

}