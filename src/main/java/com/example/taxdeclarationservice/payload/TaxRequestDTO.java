package com.example.taxdeclarationservice.payload;

import com.example.taxdeclarationservice.constant.TaxType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaxRequestDTO {
    private TaxType typeOfTax;
    private String description;
    private double amount;
}