package com.example.taxdeclarationservice.service;


import com.example.taxdeclarationservice.model.User;

import java.util.List;

public interface UserService {

    User getAuthenticatedUser();

    User getUser(String id);

    void editUserProfile(User user);

    List<User> getAllUsers();

}
