package com.tracker.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import com.tracker.dao.UserActivityRepo;
import com.tracker.dao.UserRepo;
import com.tracker.dao.UserWalletRequestsRepo;
import com.tracker.entities.PurchaseHistory;
import com.tracker.entities.UserActivity;
import com.tracker.entities.UserRequests;
import com.tracker.exceptionhandler.ValidationException;
import com.tracker.response.MostSpendUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.tracker.dto.UserWalletDtoJson;
import com.tracker.request.AdminUpdatingUserRequestDto;
import com.tracker.entities.User;
import com.tracker.service.AdminService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private UserRepo userrepo;

    @Autowired
    private UserActivityRepo userActivityRepo;

    @Autowired
    UserWalletRequestsRepo userWalletRequestsRepo;

    //LOG OUT USER METHOD
    @GetMapping("/logout")
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

    @Operation(summary = "Index Page For Admin")
    @GetMapping("/index")
    public String home() {
        return "THIS IS ADMIN INDEX";
    }


    // GETTING THE LIST OF ALL USERS

    @Operation(summary = "Getting All Users")
    @GetMapping("/getusers")
    public ResponseEntity<List<User>> ListOfUsers() {
        List<User> users = this.adminService.getAllUsers();
        if (users.size() == 0) {
            throw new ValidationException(404, "User Not Found");
//            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    // GETTING A PARTICULAR USER

    @Operation(summary = "Getting a User")
    @GetMapping("/getuser/{email}")
    public ResponseEntity<User> ParticularUser(@PathVariable("email") String email) {
        User user = this.adminService.getParticularUser(email);
        if (user == null) {
            throw new ValidationException(404, "User Not Found");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    // UPDATING A PARTICULAR USER

    @Operation(summary = "Updating a User")
    @PutMapping("/updateuser/{email}")
    public ResponseEntity<User> updateUser(@PathVariable("email") String email, @Valid @RequestBody AdminUpdatingUserRequestDto userdto) {

        User updatedUser = this.adminService.UpdateUser(email, userdto);
        if (updatedUser != null) {
            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
    }

    // UPDATING USER WALLET AS PER REQUEST

    @Operation(summary = "Updating a User Wallet")
    @PatchMapping("/wallet/update/{email}")
    public ResponseEntity<String> updateUserWallet(@PathVariable("email") String email,
                                                 @RequestBody UserWalletDtoJson userwalletjson) {

        User user = this.adminService.UpdateUserWallet(email, userwalletjson.getCurrency());
        if (user != null) {
            UserRequests request = this.userWalletRequestsRepo.getRequestIdByUserEmail(email);
            this.userWalletRequestsRepo.delete(request);
            return ResponseEntity.ok("User Wallet Updated =>" + userwalletjson.getCurrency());
        } else {
            throw new ValidationException(404, "User Not Found");
        }
    }

    // DELETING A PARTICULAR USER

    @Operation(summary = "Deleting a User")
    @DeleteMapping("/deleteuser/{email}")
    public ResponseEntity<User> deleteUser(@PathVariable("email") String email) {

        User user = this.adminService.DeleteUser(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            throw new ValidationException(404, "Product Not Found");
        }
    }

    // DISABLING A PARTICULAR USER

    @Operation(summary = "Disabling a User")
    @PatchMapping("/disableuser/{email}")
    public ResponseEntity<User> DisableUser(@PathVariable("email") String email) {
        User user = this.adminService.DisableParticularUser(email);
        if (user == null) {
            throw new ValidationException(404, "User Not Found");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    //FETCHING A PARTICULAR USER HISTORY
    @GetMapping("/purchasehistory/{email}")
    @Operation(summary = "Getting A  Particular User Purchase History")
    public ResponseEntity<List<PurchaseHistory>> UserPurchaseHistory(@PathVariable("email") String email) {
        User user = this.userRepo.findByEmail(email);
        if (user != null) {
            List<PurchaseHistory> purchaseHistoryList = this.adminService.UserPurchaseHistory(user);
            if (purchaseHistoryList.size() > 0) {
                return ResponseEntity.ok(purchaseHistoryList);
            } else {
                throw new ValidationException(404, "Purchase History Is Empty");
            }
        } else {
            throw new ValidationException(404, "User Not Found");
        }
    }


    //FETCHING ALL MOST SPENDED USER
    @GetMapping("/topspendusers")
    @Operation(summary = "Getting Most Purchased Product's")
    public ResponseEntity<List<MostSpendUsersResponse>> MostSpendUser() {
        List<MostSpendUsersResponse> mostSpendUsersResponseList = this.adminService.MostPurchasedProduct();
        if (!(mostSpendUsersResponseList.isEmpty())) {
            return ResponseEntity.ok(mostSpendUsersResponseList);
        } else {
            throw new ValidationException(404, "Data Is Not Available");
        }
    }


    //RECEIVING ALL THE WALLET REQUESTS FROM USERS
    @GetMapping("/wallet/requests")
    @Operation(summary = "Receiving All Wallet Requests From Users")
    public ResponseEntity<List<UserRequests>> ReceiveRequests() {
        List<UserRequests> userRequestsList = this.userWalletRequestsRepo.findAll();
        if (userRequestsList.size() > 0) {
            return ResponseEntity.ok(userRequestsList);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}