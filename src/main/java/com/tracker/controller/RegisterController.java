package com.tracker.controller;

import javax.validation.Valid;

import com.tracker.request.UserRegisterRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.entities.User;
import com.tracker.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Registering User")
    public ResponseEntity<String> RegisterUser(@Valid @RequestBody UserRegisterRequestDto userdto) {
        User user = this.userService.RegisterUser(userdto);
        if (user != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("User Successfully Registered..");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }

    }

}
