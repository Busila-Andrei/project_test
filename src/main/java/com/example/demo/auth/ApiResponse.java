package com.example.demo.auth;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ApiResponse {

    private boolean success;
    private String failureReason;
    private Object jwt;
}
