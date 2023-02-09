package com.example.taxdeclarationservice.repository;

import com.example.taxdeclarationservice.model.DeclaredTax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public interface DeclaredTaxRepository extends JpaRepository<DeclaredTax, UUID> {

    @Transactional
    @Modifying
    @Query("UPDATE DeclaredTax a " +
            "SET a.paid = true WHERE a.id = ?1")
    int changePaymentStatus(UUID id);

}
