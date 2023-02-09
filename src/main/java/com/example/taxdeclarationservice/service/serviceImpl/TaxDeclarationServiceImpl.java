package com.example.taxdeclarationservice.service.serviceImpl;


import com.example.taxdeclarationservice.constant.TaxType;
import com.example.taxdeclarationservice.exception.ResourceNotFoundException;
import com.example.taxdeclarationservice.model.DeclaredTax;
import com.example.taxdeclarationservice.model.User;
import com.example.taxdeclarationservice.payload.TaxRequestDTO;
import com.example.taxdeclarationservice.repository.DeclaredTaxRepository;
import com.example.taxdeclarationservice.service.TaxDeclarationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TaxDeclarationServiceImpl implements TaxDeclarationService {

    private final UserServiceImpl userService;
    private final DeclaredTaxRepository declaredTaxRepository;

    @Override
    public DeclaredTax declareTax(TaxRequestDTO taxRequestDTO) {
        User user = userService.getAuthenticatedUser();
        DeclaredTax declaredTax = new DeclaredTax();
        declaredTax.setTypeOfTax(taxRequestDTO.getTypeOfTax());
        declaredTax.setDescription(taxRequestDTO.getDescription());
        declaredTax.setAmount(taxRequestDTO.getAmount());
        declaredTax.setAmountToPay((taxRequestDTO.getTypeOfTax().value() * taxRequestDTO.getAmount()) / 100);
        declaredTax.setUser(user);
        return declaredTaxRepository.save(declaredTax);
    }

    @Override
    public void editDeclaredTax(String declaredTaxId, TaxRequestDTO taxRequestDTO) {
        Optional<DeclaredTax> optionalDeclaredTax = getTaxesDeclaredByUser(null).stream().filter(declaredTax1 ->
               declaredTax1.getId().toString().equals(declaredTaxId)).findFirst();
        if (optionalDeclaredTax.isEmpty()) {
           throw new ResourceNotFoundException("No tax declared with id- " + declaredTaxId);
        }
        optionalDeclaredTax.get().setTypeOfTax(taxRequestDTO.getTypeOfTax());
        optionalDeclaredTax.get().setDescription(taxRequestDTO.getDescription());
        optionalDeclaredTax.get().setAmount(taxRequestDTO.getAmount());
        optionalDeclaredTax.get().setAmountToPay((taxRequestDTO.getTypeOfTax().value() * taxRequestDTO.getAmount()) / 100);
        declaredTaxRepository.save(optionalDeclaredTax.get());
    }

    @Override
    public DeclaredTax getDeclaredTax(String declaredTaxId) {
        return declaredTaxRepository.findById(UUID.fromString(declaredTaxId))
                .orElseThrow(() -> new ResourceNotFoundException("No tax is declared with id- " + declaredTaxId));
    }

    @Override
    public List<DeclaredTax> getTaxesDeclaredByUser(TaxType taxType) {
        User user = userService.getAuthenticatedUser();
        if (taxType == null) {
            return user.getDeclaredTaxList();
        }else {
            return user.getDeclaredTaxList().stream().filter(declaredTax -> declaredTax.getTypeOfTax().equals(taxType))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void deleteDeclaredTax(String declaredTaxId) {
        getDeclaredTax(declaredTaxId);
        declaredTaxRepository.deleteById(UUID.fromString(declaredTaxId));
    }

}
