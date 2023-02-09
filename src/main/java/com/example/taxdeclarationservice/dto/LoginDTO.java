package com.example.taxdeclarationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class LoginDTO {

    private final String accessToken;
    private final String refreshToken;
    private final String id;
    private final String companyName;
    private final String phoneNumber;
    private final Instant expiredIn;
    private final String email;
    private final List<String> role;
    private String type = "Bearer";

    public LoginDTO(String accessToken, String refreshToken, String id, Instant expiredIn, String email, String companyName, String phoneNumber, List<String> role) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.id = id;
        this.expiredIn = expiredIn;
        this.email = email;
        this.companyName = companyName;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }
}