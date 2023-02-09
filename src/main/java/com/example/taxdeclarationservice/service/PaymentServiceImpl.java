package com.example.taxdeclarationservice.service;

import com.example.taxdeclarationservice.model.DeclaredTax;
import com.example.taxdeclarationservice.model.Payment;
import com.example.taxdeclarationservice.repository.DeclaredTaxRepository;
import com.example.taxdeclarationservice.repository.PaymentRepository;
import com.example.taxdeclarationservice.service.serviceImpl.TaxDeclarationServiceImpl;
import com.example.taxdeclarationservice.service.serviceImpl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements PaymentService {

    private final UserServiceImpl userService;
    private final TaxDeclarationServiceImpl declarationService;
    private final DeclaredTaxRepository declaredTaxRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public void payDeclaredTax(String declaredTaxId) {
        Payment payment = new Payment();
        DeclaredTax declaredTax = declarationService.getDeclaredTax(declaredTaxId);
        payment.setAmount(declaredTax.getAmountToPay());
        payment.setDeclaredTax(declaredTax);
        payment.setUser(userService.getAuthenticatedUser());
        declaredTaxRepository.changePaymentStatus(UUID.fromString(declaredTaxId));
        paymentRepository.save(payment);
    }
}
