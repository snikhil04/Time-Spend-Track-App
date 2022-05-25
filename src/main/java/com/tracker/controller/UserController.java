package com.tracker.controller;

import java.util.List;

import com.tracker.dao.ProductRepo;
import com.tracker.response.PurchaseHistoryUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tracker.dao.UserRepo;
import com.tracker.request.UserUpdatingDetailsRequestDto;
import com.tracker.response.ProductListUserResponse;
import com.tracker.dto.ProductPurchaseUserRequestDto;
import com.tracker.entities.User;
import com.tracker.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userservice;

    @Autowired
    private UserRepo userrepo;

    @Autowired
    private ProductRepo productRepo;

    // INDEX PAGE FOR USER
    @Operation(summary = "Index Page For User")
    @GetMapping("/index")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome User");
    }

    // UPDATING USER PROFILLE
    @PatchMapping("/update/{email}")
    @Operation(summary = "Updating User Profile")
    public ResponseEntity<UserUpdatingDetailsRequestDto> UpdateProfile(@PathVariable("email") String email,
                                                                       @RequestBody UserUpdatingDetailsRequestDto userdto) {
        User user = this.userrepo.findByEmail(email);

        if (user != null) {
            UserUpdatingDetailsRequestDto updatedUserResponse = this.userservice.updateUser(userdto, user);
            if (updatedUserResponse != null) {
                return ResponseEntity.ok(updatedUserResponse);
            } else {
                return ResponseEntity.badRequest().build();
            }

        } else {
            return ResponseEntity.notFound().build();
        }

    }

    // GETTING USER WALLET BALANCE
    @GetMapping("/wallet/{email}")
    @Operation(summary = "Getting User Wallet Balance")
    public ResponseEntity<String> UserWallet(@PathVariable("email") String email) {
        User user = this.userrepo.findByEmail(email);
        if (user != null) {
            long currency = user.getUserWallet().getCurrency();
            return ResponseEntity.ok("Your Wallet Balance is : " + currency);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User Not Found!!");
        }
    }

    // GETTING PRODUCTS BASED ON PRODUCT CATEGORY
    @Operation(summary = "Getting Products Based on Category")
    @GetMapping("/product/get/{category}")
    public ResponseEntity<List<ProductListUserResponse>> ProductsByCategory(@PathVariable("category") String category) {
        if (!(this.productRepo.CheckProductExist(category).isEmpty())) {

            List<ProductListUserResponse> products = this.userservice.getProductsByCategory(category);
            if (products != null) {
                return ResponseEntity.ok(products);

            } else {
                return ResponseEntity.notFound().build();
            }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    // PURCHASING PRODUCT
    @GetMapping("/{email}/product/purchase")
    @Operation(summary = "Purchasing product")
    public ResponseEntity<PurchaseHistoryUserResponse> PurchaseProduct(@PathVariable("email") String email,
                                                                       @RequestBody ProductPurchaseUserRequestDto productPurchaseUserRequestDto) throws Exception{
        User user = this.userrepo.findByEmail(email);

        if (user != null) {
            return ResponseEntity.ok(this.userservice.PurchaseProduct(user, productPurchaseUserRequestDto));

        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //SHOWING PURCHASE HISTORY
    @GetMapping("/{email}/product/purchase/history")
    @Operation(summary = "Showing the purchase history")
    public ResponseEntity<List<PurchaseHistoryUserResponse>> PurchaseHistory(@PathVariable("email") String email) {
        List<PurchaseHistoryUserResponse> purchaseHistoryUserResponseList = this.userservice.PurchaseHistory(email);
        if (!(purchaseHistoryUserResponseList.isEmpty())){
            return ResponseEntity.ok(purchaseHistoryUserResponseList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}