package com.example.taxdeclarationservice.service.serviceImpl;

import com.example.taxdeclarationservice.config.security.JwtUtils;
import com.example.taxdeclarationservice.dto.LoginDTO;
import com.example.taxdeclarationservice.exception.*;
import com.example.taxdeclarationservice.model.*;
import com.example.taxdeclarationservice.payload.*;
import com.example.taxdeclarationservice.repository.*;
import com.example.taxdeclarationservice.service.*;
import com.example.taxdeclarationservice.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final Util util = new Util();
    private final RoleServiceImpl roleService;
    private final EmailServiceImpl emailService;
    private final ConfirmationTokenServiceImpl confirmationTokenService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${taxdeclaration.app.baseUrlLocal}")
    private String baseUrlLocal;
    @Value("${taxdeclaration.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Override
    public void userRegistration(RegisterRequestDTO registerRequestDTO) {
        EmailDetails email = new EmailDetails();
        User user = new User();
        Optional<User> optionalUser = userRepository.findByEmail(registerRequestDTO.getEmail());
        if (optionalUser.isPresent()) {
            throw new ResourceAlreadyExistException("User already exist with email - " + registerRequestDTO.getEmail());
        }
        user.setCompanyName(registerRequestDTO.getCompanyName());
        user.setPhoneNumber(registerRequestDTO.getPhoneNumber());
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setRoles(Collections.singletonList(roleService.getRoleByName(registerRequestDTO.getRole())));
        ConfirmationTokenRequestDTO confirmationTokenRequestDTO = new ConfirmationTokenRequestDTO(
                util.generateToken(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                user
        );
        String link = baseUrlLocal + "api/v1/public/auth/confirm?token=" + confirmationTokenRequestDTO.getToken();
        emailService.sendUserRegistration(user, email, link);
        userRepository.save(user);
        confirmationTokenService.saveConfirmationToken(confirmationTokenRequestDTO);
    }

    @Transactional
    @Override
    public String confirmToken(String token) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenService.getToken(token);
        if (confirmationToken.get().getConfirmedAt() != null) {
            throw new ResourceAlreadyExistException("email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.get().getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new ResourceNotFoundException("token expired");
        }
        confirmationTokenService.setConfirmedAt(token);
        enableUser(confirmationToken.get().getUser().getEmail());
        return "Account Confirmed";
    }

    public void enableUser(String email) {
        userRepository.enableUser(email);
    }

    @Override
    public void resendVerificationEmailToUsUser(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        EmailDetails email1 = new EmailDetails();
        ConfirmationTokenRequestDTO confirmationTokenRequestDTO = new ConfirmationTokenRequestDTO(
                util.generateToken(),
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(10),
                optionalUser.get()
        );
        String link = baseUrlLocal + "api/v1/public/auth/confirm?token=" + confirmationTokenRequestDTO.getToken();
        if (optionalUser.get().getTokens().get(0).getConfirmedAt() == null) {
            confirmationTokenService.saveConfirmationToken(confirmationTokenRequestDTO);
            emailService.sendUserRegistration(optionalUser.get(), email1, link);
        }
    }

    @Override
    public RefreshToken verifyExpirationOfJwtToken(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Override
    public RefreshToken createRefreshJwtToken(String userId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findById(UUID.fromString(userId)).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    @Override
    public LoginDTO userLogin(LoginRequestDTO loginRequestDTO) {
        Authentication authentication;
        try {
            User user = userRepository.findByEmail(loginRequestDTO.getEmail()).orElseThrow(
                    () -> new ResourceNotFoundException("User not found with email: " + loginRequestDTO.getEmail()));
            checkIfUserIsEnabled(user);
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword()));
        } catch (BadCredentialsException ex) {
            throw new BadRequestException("Invalid email or password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(authentication);
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        RefreshToken refreshToken = createRefreshJwtToken(userDetails.getUser().getId().toString());

        return new LoginDTO(jwt, refreshToken.getToken(), userDetails.getUser().getId().toString(), refreshToken.getExpiryDate(),
                userDetails.getUsername(), userDetails.getUser().getCompanyName(), userDetails.getUser().getPhoneNumber(), roles);
    }

    @Override
    public User checkIfEmailExist(String email) {
        return userRepository.findByEmail(email).
                orElseThrow(() -> new ResourceNotFoundException("User does not exist with email: " + email));
    }

    public User checkIfPhoneNumberExist(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).
                orElseThrow(() -> new ResourceNotFoundException("User does not exist with phone number: " + phoneNumber));
    }

    public void checkIfUserIsEnabled(User user) {
        if (!user.getEnabled()) {
            throw new BadRequestException("User has nas not yet confirmed the account");
        }
    }

    @Override
    public Optional<RefreshToken> getRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

}
