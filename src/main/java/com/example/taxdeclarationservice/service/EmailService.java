package com.example.taxdeclarationservice.service;


import com.example.taxdeclarationservice.model.EmailDetails;
import com.example.taxdeclarationservice.model.User;

public interface EmailService {
    void sendUserRegistration(User user, EmailDetails email, String link);

}
