package com.example.taxdeclarationservice.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class Util {

    public String generateToken() {
        UUID token = UUID.randomUUID();
        return token.toString();
    }
}
