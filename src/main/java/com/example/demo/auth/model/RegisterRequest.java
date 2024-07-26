package com.example.demo.auth.model;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterRequest {

    @NotBlank
    @NotNull
    private String firstname;

    @NotBlank
    @NotNull
    private String lastname;

    @NotBlank
    @NotNull
    @Email
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 64)
    private String password;

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "firstName='" + firstname + '\'' +
                ", lastName='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
