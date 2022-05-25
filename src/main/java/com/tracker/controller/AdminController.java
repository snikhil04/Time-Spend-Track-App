package com.tracker.controller;

import java.util.List;

import javax.validation.Valid;

import com.tracker.response.MostSpendUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "Index Page For Admin")
    @GetMapping("/index")
    public String home() {
        return "home";
    }

    // GETTING THE LIST OF ALL USERS
    @Operation(summary = "Getting All Users")
    @GetMapping("/getusers")
    public ResponseEntity<List<User>> ListOfUsers() {
        List<User> users = this.adminService.getAllUsers();
        if (users.size() == 0) {
            return ResponseEntity.noContent().build();
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
            return ResponseEntity.notFound().build();
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

    // UPDATING A PARTICULAR USER WALLET
    @Operation(summary = "Updating a User Wallet")
    @PatchMapping("/updateuserwallet/{email}")
    public ResponseEntity<User> updateUserWallet(@PathVariable("email") String email,
                                                 @RequestBody UserWalletDtoJson userwalletjson) {

        User user = this.adminService.UpdateUserWallet(email, userwalletjson.getCurrency());
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
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
            return ResponseEntity.notFound().build();
        }
    }

    // DISABLING A PARTICULAR USER
    @Operation(summary = "Disabling a User")
    @PatchMapping("/disableuser/{email}")
    public ResponseEntity<User> DisableUser(@PathVariable("email") String email) {
        User user = this.adminService.DisableParticularUser(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
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
            return ResponseEntity.noContent().build();
        }
    }

}