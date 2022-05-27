package com.tracker.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import com.tracker.dao.ProductRepo;
import com.tracker.dao.UserActivityRepo;
import com.tracker.dao.UserWalletRequestsRepo;
import com.tracker.entities.UserActivity;
import com.tracker.entities.UserRequests;
import com.tracker.exceptionhandler.ValidationException;
import com.tracker.request.UserLogin;
import com.tracker.request.UserRegisterRequestDto;
import com.tracker.response.PurchaseHistoryUserResponse;
import com.tracker.response.UpdatedUserResponse;
import com.tracker.securityconfig.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.tracker.dao.UserRepo;
import com.tracker.request.UserUpdatingDetailsRequestDto;
import com.tracker.response.ProductListUserResponse;
import com.tracker.dto.ProductPurchaseUserRequestDto;
import com.tracker.entities.User;
import com.tracker.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userservice;

    @Autowired
    private UserRepo userrepo;

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    AuthenticationManager authenticationmanager;

    @Autowired
    private JwtUtil jwtutil;

    @Autowired
    private UserActivityRepo userActivityRepo;

    @Autowired
    private UserWalletRequestsRepo userWalletRequestsRepo;

    //REGISTERING A USER
    @PostMapping("/register")
    @Operation(summary = "Registering User")
    public ResponseEntity<String> RegisterUser(@Valid @RequestBody UserRegisterRequestDto userdto) {
        User user = this.userservice.RegisterUser(userdto);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User Successfully Registered..");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    //Authenticating And Logging In User
    @PostMapping("/login")
    @Operation(summary = "Authenticating And Logging In User")
    public String UserLogin(@Valid @RequestBody UserLogin userLogin) {

        try {
            authenticationmanager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword())
            );
        } catch (UsernameNotFoundException ex) {
            throw new UsernameNotFoundException("Invalid UserName & Password");
        }

        User user = this.userrepo.findByEmail(userLogin.getEmail());

        if (user.isActive() == false) {
            throw new ValidationException(202, "You Are Blocked By System Admin!");
        }
        UserActivity activity = new UserActivity();
        activity.setUserId(user.getId());
        activity.setUserEmail(user.getEmail());
        activity.setLoginTime(LocalDateTime.now());
        activity.setActivity("LOGIN");
        this.userActivityRepo.save(activity);

        return jwtutil.generateToken(userLogin.getEmail(), user);
    }

    // LOG OUT USER METHOD
    @GetMapping(path = "/logout")
    @Operation(summary = "Logging-Out")
    public ResponseEntity LogOut() {

        User user = this.userrepo.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        UserActivity activity = new UserActivity();
        activity.setUserId(user.getId());
        activity.setUserEmail(user.getEmail());
        activity.setLoginTime(LocalDateTime.now());
        activity.setActivity("LOGOUT");
        this.userActivityRepo.save(activity);
        return ResponseEntity.ok("Logged Out");
    }


    // INDEX PAGE FOR USER
    @Operation(summary = "Index Page For User")
    @GetMapping("/index")
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome User");
    }


    // UPDATING USER PROFILE
    @PatchMapping("/update")
    @Operation(summary = "Updating User Profile")
    public ResponseEntity<UpdatedUserResponse> UpdateProfile(
            @RequestBody UserUpdatingDetailsRequestDto userdto, Principal principal) {
        String email = principal.getName();
        User user = this.userrepo.findByEmail(email);

        if (user != null) {
            UpdatedUserResponse updatedUserResponse = this.userservice.updateUser(userdto, user);
            if (updatedUserResponse != null) {
                return ResponseEntity.ok(updatedUserResponse);
            } else {
                return ResponseEntity.badRequest().build();
            }

        } else {
            throw new ValidationException(404, "User Not Found");
        }

    }

    // GETTING USER WALLET BALANCE
    @GetMapping("/wallet")
    @Operation(summary = "Getting User Wallet Balance")
    public ResponseEntity<String> UserWallet(Principal principal) {
        String email = principal.getName();
        User user = this.userrepo.findByEmail(email);
        if (user != null) {
            long currency = user.getUserWallet().getCurrency();
            return ResponseEntity.ok("Your Wallet Balance is : " + currency);
        } else {
            throw new ValidationException(404, "User Not Found");
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
                throw new ValidationException(404, "Products Not Found");
            }
        } else {
            throw new ValidationException(404, "Product Not Found");
        }
    }

    // PURCHASING PRODUCT

    @GetMapping("/product/purchase")
    @Operation(summary = "Purchasing product")
    public ResponseEntity<PurchaseHistoryUserResponse> PurchaseProduct(
            @RequestBody ProductPurchaseUserRequestDto productPurchaseUserRequestDto, Principal principal) throws Exception {
        String email = principal.getName();

        User user = this.userrepo.findByEmail(email);

        if (user != null) {
            return ResponseEntity.ok(this.userservice.PurchaseProduct(user, productPurchaseUserRequestDto));

        } else {
            throw new ValidationException(404, "User Not Found");
        }
    }

    //SHOWING PURCHASE HISTORY

    @GetMapping("/product/purchase/history")
    @Operation(summary = "Showing the purchase history")
    public ResponseEntity<List<PurchaseHistoryUserResponse>> PurchaseHistory(Principal principal) {
        String email = principal.getName();
        List<PurchaseHistoryUserResponse> purchaseHistoryUserResponseList = this.userservice.PurchaseHistory(email);
        if (!(purchaseHistoryUserResponseList.isEmpty())) {
            return ResponseEntity.ok(purchaseHistoryUserResponseList);
        } else {
            throw new ValidationException(404, "Purchase History Is Empty");
        }
    }

    //REQUESTING ADMIN TO INCREASE WALLET LIMIT
    @PatchMapping("/request/{amount}")
    @Operation(summary = "Sending The Request To Admin To Increase User's Wallet Limit")
    public ResponseEntity<String> RequestAdmin(@PathVariable("amount") long amount, Principal principal) {
        User user = this.userrepo.findByEmail(principal.getName());
        if (user.getUserWallet().getCurrency() <= 10000) {
            UserRequests userRequests= this.userservice.RequestAdminForWallet(user,amount);
           this.userWalletRequestsRepo.save(userRequests);
           return ResponseEntity.ok("Request Sent");
        }
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("Sorry, Your Balance is More Than 10000");
    }
}