package com.tracker.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tracker.dao.UserRepo;
import com.tracker.entities.User;
import com.tracker.exceptionhandler.ValidationException;
import com.tracker.request.UserRegisterRequestDto;
import com.tracker.securityconfig.AuthenticationRequestDto;
import com.tracker.securityconfig.JwtUtil;
import com.tracker.service.UserService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class RegisterController {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtUtil jwtutil;

	@Autowired
	private UserRepo userrepo;

	@Autowired
	AuthenticationManager authenticationmanager;

	// REGISTERING A USER
	@PostMapping("/register")
	@Operation(summary = "Registering User")
	public ResponseEntity<String> RegisterUser(@Valid @RequestBody UserRegisterRequestDto userdto) {
		User CheckingExistedUser = this.userrepo.findByEmail(userdto.getEmail());
		if (CheckingExistedUser == null) {
			User user = this.userService.RegisterUser(userdto);
	        if (user != null) {
	            return ResponseEntity.status(HttpStatus.CREATED).body("User Successfully Registered..");
	        } else {
//	            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	        	throw new ValidationException(404,"Not Acceptable!");
	        }
		}
		else {
			throw new ValidationException(400,"User Aleady Registered!");
		}
	}

	// AUTHENTICATING AND GENERATING A WEB TOKEN
	@PostMapping("/authenticate")
	public String GenerateToken(@RequestBody AuthenticationRequestDto requestDto) {

		try {
			authenticationmanager.authenticate(
					new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword()));
		} catch (UsernameNotFoundException exception) {
			throw new UsernameNotFoundException("Invalid Username & Password");
		}
		User user = this.userrepo.findByEmail(requestDto.getUsername());
		return jwtutil.generateToken(requestDto.getUsername(), user);
	}

}