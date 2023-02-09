package com.example.taxdeclarationservice.service;


import com.example.taxdeclarationservice.constant.TaxType;
import com.example.taxdeclarationservice.model.DeclaredTax;
import com.example.taxdeclarationservice.payload.TaxRequestDTO;

import java.util.List;

public interface TaxDeclarationService {

    DeclaredTax declareTax(TaxRequestDTO taxRequestDTO);

    void editDeclaredTax(String declaredTaxId, TaxRequestDTO taxRequestDTO);

    DeclaredTax getDeclaredTax(String declaredTaxId);

    List<DeclaredTax> getTaxesDeclaredByUser(TaxType taxType);

    void deleteDeclaredTax(String declaredTaxId);
}
