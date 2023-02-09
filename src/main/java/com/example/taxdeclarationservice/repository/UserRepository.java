package com.example.taxdeclarationservice.repository;

import com.example.taxdeclarationservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);
//    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);

    @Transactional
    @Modifying
    @Query("UPDATE User a " +
            "SET a.enabled = true WHERE a.email = ?1")
    int enableUser(String email);
}
