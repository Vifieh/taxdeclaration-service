package com.example.taxdeclarationservice.service;

import com.example.taxdeclarationservice.dto.LoginDTO;
import com.example.taxdeclarationservice.model.RefreshToken;
import com.example.taxdeclarationservice.model.User;
import com.example.taxdeclarationservice.payload.LoginRequestDTO;
import com.example.taxdeclarationservice.payload.RegisterRequestDTO;

import java.util.Optional;

public interface AuthenticationService {

    void userRegistration(RegisterRequestDTO registerRequestDTO);

    String confirmToken(String token);

    void resendVerificationEmailToUsUser(String email);

    RefreshToken verifyExpirationOfJwtToken(RefreshToken token);

    RefreshToken createRefreshJwtToken(String userId);

    LoginDTO userLogin(LoginRequestDTO loginRequestDTO);

    User checkIfEmailExist(String email);

    Optional<RefreshToken> getRefreshToken(String token);

}
