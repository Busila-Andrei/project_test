package com.example.demo.auth.controller;

import com.example.demo.auth.model.RegisterRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/register")
    private void register(@RequestBody RegisterRequest registerRequest) {
        System.out.println(registerRequest);
    }
}