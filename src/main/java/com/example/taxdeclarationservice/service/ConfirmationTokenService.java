package com.example.taxdeclarationservice.service;


import com.example.taxdeclarationservice.model.ConfirmationToken;
import com.example.taxdeclarationservice.payload.ConfirmationTokenRequestDTO;

import java.util.Optional;

public interface ConfirmationTokenService {

    void saveConfirmationToken(ConfirmationTokenRequestDTO confirmationTokenRequestDTO);

    Optional<ConfirmationToken> getToken(String token);

    int setConfirmedAt(String token);
}
