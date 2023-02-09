package com.example.taxdeclarationservice.dto;

import com.example.taxdeclarationservice.constant.TaxType;
import com.example.taxdeclarationservice.model.CustomEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxDTO extends CustomEntity {
    private String id;
    private TaxType typeOfTax;
    private String description;
    private double amount;
    private double amountToPay;
    private boolean paid;
}
