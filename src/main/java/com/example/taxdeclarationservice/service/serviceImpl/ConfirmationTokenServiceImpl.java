package com.example.taxdeclarationservice.service.serviceImpl;

import com.example.taxdeclarationservice.exception.ResourceNotFoundException;
import com.example.taxdeclarationservice.model.ConfirmationToken;
import com.example.taxdeclarationservice.payload.ConfirmationTokenRequestDTO;
import com.example.taxdeclarationservice.repository.ConfirmationTokenRepository;
import com.example.taxdeclarationservice.service.ConfirmationTokenService;
import com.example.taxdeclarationservice.util.Util;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmationTokenServiceImpl implements ConfirmationTokenService {

    private final Util util = new Util();
   private final ConfirmationTokenRepository confirmationTokenRepository;

    @Override
    public void saveConfirmationToken(ConfirmationTokenRequestDTO confirmationTokenRequestDTO) {
        ConfirmationToken confirmationToken = new ConfirmationToken();
        confirmationToken.setToken(confirmationTokenRequestDTO.getToken());
        confirmationToken.setConfirmedAt(confirmationTokenRequestDTO.getConfirmedAt());
        confirmationToken.setExpiresAt(confirmationTokenRequestDTO.getExpiresAt());
        confirmationToken.setCreatedAt(confirmationTokenRequestDTO.getCreatedAt());
        confirmationToken.setUser(confirmationTokenRequestDTO.getUser());
        confirmationTokenRepository.save(confirmationToken);
    }

    @Override
    public Optional<ConfirmationToken> getToken(String token) {
        Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(token);
        confirmationToken.orElseThrow(() -> new ResourceNotFoundException("token not found"));
        return confirmationToken;
    }

    @Override
    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
