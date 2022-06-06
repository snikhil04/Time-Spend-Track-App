package com.tracker.controller;

import java.util.List;

import javax.validation.Valid;

import com.tracker.dao.UserRepo;
import com.tracker.entities.PurchaseHistory;
import com.tracker.entities.UserRequests;
import com.tracker.response.MostSpendUsersResponse;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tracker.dto.UserWalletDtoJson;
import com.tracker.request.AdminUpdatingUserRequestDto;
import com.tracker.entities.User;
import com.tracker.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepo userrepo;

    //LOG OUT USER METHOD
    @GetMapping("/logout")
    @Operation(summary = "Logging-Out")
    public String LogOut() {
        return this.adminService.AdminLogout();
    }

    //INDEX PAGE FOR ADMIN
    @Operation(summary = "Index Page For Admin")
    @GetMapping("/index")
    public String home() {
        return "THIS IS ADMIN INDEX";
    }


    // GETTING THE LIST OF ALL USERS
    @Operation(summary = "Getting All Users")
    @GetMapping("/getusers")
    public List<User> ListOfUsers() {
        return this.adminService.getAllUsers();
    }

    // GETTING A PARTICULAR USER
    @Operation(summary = "Getting a User")
    @GetMapping("/getuser/{email}")
    public User ParticularUser(@PathVariable("email") String email) {
        return this.adminService.getParticularUser(email);
    }

    // UPDATING A PARTICULAR USER
    @Operation(summary = "Updating a User")
    @PutMapping("/updateuser/{email}")
    public User updateUser(@PathVariable("email") String email, @Valid @RequestBody AdminUpdatingUserRequestDto userdto) {
        return this.adminService.UpdateUser(email, userdto);
    }

    // UPDATING USER WALLET
    @Operation(summary = "Updating User Wallet")
    @PatchMapping("/wallet/update/{email}")
    public String updateUserWallet(@PathVariable("email") String email, @RequestBody UserWalletDtoJson userwalletjson) {
        return this.adminService.UpdateUserWallet(email, userwalletjson);
    }

    //UPDATING USER WALLET AS PER USER REQUEST
    @Operation(summary = "Updating User Wallet On User Request")
    @PatchMapping("/wallet/request/update/{email}")
    public String updateUserWalletOnRequest(@PathVariable("email") String email, @RequestBody UserWalletDtoJson userwalletjson) {
        return this.adminService.UpdateUserWalletOnRequest(email, userwalletjson);
    }

    // DELETING A PARTICULAR USER
    @Operation(summary = "Deleting a User")
    @DeleteMapping("/deleteuser/{email}")
    public User deleteUser(@PathVariable("email") String email) {
        return this.adminService.DeleteUser(email);
    }

    // DISABLING A PARTICULAR USER
    @Operation(summary = "Disabling a User")
    @PatchMapping("/disableuser/{email}")
    public User DisableUser(@PathVariable("email") String email) {
        return this.adminService.DisableParticularUser(email);
    }

    //FETCHING ALL PURCHASE HISTORY
    @GetMapping("/purchasehistory")
    @Operation(summary = "Getting All Purchase History")
    public List<PurchaseHistory> AllPurchaseHistory() {
        return this.adminService.AllPurchaseHistory();
    }

    //FETCHING A PARTICULAR USER PURCHASE HISTORY
    @GetMapping("/purchasehistory/{email}")
    @Operation(summary = "Getting A  Particular User Purchase History")
    public List<PurchaseHistory> UserPurchaseHistory(@PathVariable("email") String email) {
        return this.adminService.UserPurchaseHistory(email);
    }

    //FETCHING ALL MOST SPENDED USER
    @GetMapping("/topspendusers")
    @Operation(summary = "Getting Most Purchased Product's")
    public List<MostSpendUsersResponse> MostSpendUser() {
        return this.adminService.MostPurchasedProduct();
    }


    //RECEIVING ALL THE WALLET REQUESTS FROM USERS
    @GetMapping("/wallet/requests")
    @Operation(summary = "Receiving All Wallet Requests From Users")
    public List<UserRequests> ReceiveRequests() {
        return this.adminService.WalletRequests();
    }
}