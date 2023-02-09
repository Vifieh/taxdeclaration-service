package com.example.taxdeclarationservice.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class RefreshTokenDTO {

    private final String accessToken;
    private final String refreshToken;
    private String tokenType = "Bearer";

}