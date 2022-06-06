package com.tracker.controller;

import java.security.Principal;
import java.util.List;

import com.tracker.request.UserLogin;
import com.tracker.request.UserRegisterRequestDto;
import com.tracker.response.PurchaseHistoryUserResponse;
import com.tracker.response.UpdatedUserResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tracker.request.UserUpdatingDetailsRequestDto;
import com.tracker.response.ProductListUserResponse;
import com.tracker.dto.ProductPurchaseUserRequestDto;
import com.tracker.entities.User;
import com.tracker.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userservice;

    //REGISTERING A USER
    @PostMapping("/register")
    @Operation(summary = "Registering User")
    public User RegisterUser(@Valid @RequestBody UserRegisterRequestDto userdto) {
        return this.userservice.RegisterUser(userdto);
    }

    //Authenticating And Logging In User
    @PostMapping("/login")
    @Operation(summary = "Authenticating And Logging In User")
    public String UserLogin(@Valid @RequestBody UserLogin userLogin) {
        return this.userservice.UserLogin(userLogin);
    }

    // LOGGING OUT USER
    @GetMapping(path = "/logout")
    @Operation(summary = "Logging-Out")
    public String LogOut() {
        return this.userservice.Logout();
    }


    // INDEX PAGE FOR USER
    @Operation(summary = "Index Page For User")
    @GetMapping("/index")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome To The Index Page");
    }


    // UPDATING USER PROFILE
    @PatchMapping("/update")
    @Operation(summary = "Updating User Profile")
    public UpdatedUserResponse UpdateProfile(
            @RequestBody UserUpdatingDetailsRequestDto userdto, Principal principal) {
        return this.userservice.updateUser(userdto, principal);
    }

    // GETTING USER WALLET BALANCE
    @GetMapping("/wallet")
    @Operation(summary = "Getting User Wallet Balance")
    public String UserWallet(Principal principal) {
        return this.userservice.UserWallet(principal);
    }

    // GETTING ALL PRODUCTS
    @Operation(summary = "Getting All Products")
    @GetMapping("/product/get")
    public List<ProductListUserResponse> ProductsByCategory() {
        return this.userservice.getAllProducts();
    }

    // GETTING PRODUCTS BASED ON PRODUCT CATEGORY
    @Operation(summary = "Getting Products Based on Category")
    @GetMapping("/product/get/{category}")
    public List<ProductListUserResponse> ProductsByCategory(@PathVariable("category") String category) {
        return this.userservice.getProductsByCategory(category);
    }

    // PURCHASING PRODUCT
    @GetMapping("/product/purchase")
    @Operation(summary = "Purchasing product")
    public PurchaseHistoryUserResponse PurchaseProduct(
            @RequestBody ProductPurchaseUserRequestDto productPurchaseUserRequestDto, Principal principal) {
        return this.userservice.PurchaseProduct(principal, productPurchaseUserRequestDto);
    }

    //SHOWING PURCHASE HISTORY
    @GetMapping("/product/purchase/history")
    @Operation(summary = "Showing the purchase history")
    public List<PurchaseHistoryUserResponse> PurchaseHistory(Principal principal) {
        return this.userservice.PurchaseHistory(principal);
    }

    //REQUESTING ADMIN TO INCREASE WALLET LIMIT
    @PatchMapping("/request/{amount}")
    @Operation(summary = "Sending The Request To Admin To Increase User's Wallet Limit")
    public String RequestAdmin(@PathVariable("amount") long amount, Principal principal) {
        return this.userservice.RequestAdminForWallet(principal, amount);
    }
}