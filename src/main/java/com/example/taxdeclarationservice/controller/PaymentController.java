package com.example.taxdeclarationservice.controller;

import com.example.taxdeclarationservice.constant.TaxType;
import com.example.taxdeclarationservice.dto.ResponseMessageDTO;
import com.example.taxdeclarationservice.dto.TaxDTO;
import com.example.taxdeclarationservice.model.DeclaredTax;
import com.example.taxdeclarationservice.payload.TaxRequestDTO;
import com.example.taxdeclarationservice.service.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@RequestMapping("api/v1/protected/payments")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    private final ModelMapper modelMapper;


    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping("/taxes")
    public ResponseEntity<ResponseMessageDTO> payForDeclaredTax(@RequestParam("declaredTaxId") String declaredTaxId) {
        paymentService.payDeclaredTax(declaredTaxId);
        return new ResponseEntity<>(new ResponseMessageDTO("Payment Successful!!"), HttpStatus.CREATED);
    }

}
