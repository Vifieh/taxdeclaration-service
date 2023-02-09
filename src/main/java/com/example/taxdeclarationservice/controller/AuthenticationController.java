package com.example.taxdeclarationservice.controller;

import com.example.taxdeclarationservice.config.security.JwtUtils;
import com.example.taxdeclarationservice.dto.*;
import com.example.taxdeclarationservice.exception.TokenRefreshException;
import com.example.taxdeclarationservice.model.RefreshToken;
import com.example.taxdeclarationservice.payload.*;
import com.example.taxdeclarationservice.service.serviceImpl.AuthenticationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@CrossOrigin()
@RequestMapping("/api/v1/public/auth/")
public class AuthenticationController {
    
   private final AuthenticationServiceImpl authenticationService;
   private final JwtUtils jwtUtils;
    private final ModelMapper modelMapper;

    private String message;

    @PostMapping("register")
    public ResponseEntity<ResponseMessageDTO> registerUser(@Valid @RequestBody RegisterRequestDTO registerRequestDTO) {
        authenticationService.userRegistration(registerRequestDTO);
        message = "User registered Successfully!";
        return new ResponseEntity<>(new ResponseMessageDTO(message), HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<LoginDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        LoginDTO loginDTO = authenticationService.userLogin(loginRequestDTO);
        return ResponseEntity.ok(loginDTO);
    }

    @GetMapping("confirm")
    public ResponseEntity<String> confirmAccount(@RequestParam("token") String token) {
        String message = authenticationService.confirmToken(token);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping("resend-verification-link")
    public ResponseEntity<ResponseMessageDTO> resendVerification(@Valid @RequestBody String email) {
        authenticationService.resendVerificationEmailToUsUser(email);
        message = "Email sent successfully";
        return new ResponseEntity<>(new ResponseMessageDTO(message), HttpStatus.OK);
    }

    @PostMapping("refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO) {
        String requestRefreshToken = refreshTokenRequestDTO.getRefreshToken();

        return authenticationService.getRefreshToken(requestRefreshToken).map(authenticationService::verifyExpirationOfJwtToken)
                .map(RefreshToken::getUser).map(user -> {
                    String token = jwtUtils.generateTokenFromEmail(user.getEmail());
                    return ResponseEntity.ok(new RefreshTokenDTO(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));
    }
}
