package com.example.taxdeclarationservice.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    private String companyName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "password is required")
    private String password;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "role is required")
    private String role;
}