package com.example.taxdeclarationservice.service.serviceImpl;


import com.example.taxdeclarationservice.exception.ResourceNotFoundException;
import com.example.taxdeclarationservice.model.User;
import com.example.taxdeclarationservice.repository.UserRepository;
import com.example.taxdeclarationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthenticationServiceImpl authenticationService;
    private final UserRepository userRepository;

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return authenticationService.checkIfEmailExist(userDetails.getUsername());
    }

    @Override
    public User getUser(String id) {
        Optional<User> optionalUser = userRepository.findById(UUID.fromString(id));
        if (optionalUser.isEmpty()) {
            throw new ResourceNotFoundException("User not found");
        }
        return optionalUser.get();
    }

    @Override
    public void editUserProfile(User user) {

    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }
}
