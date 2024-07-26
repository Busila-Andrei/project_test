package com.example.demo.auth.controller;

import com.example.demo.UserAlreadyExistException;
import com.example.demo.auth.model.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    private ResponseEntity register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerUser(registerRequest);
            System.out.println(registerRequest.toString());
            return ResponseEntity.ok().build();
        } catch (UserAlreadyExistException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

    }
}
