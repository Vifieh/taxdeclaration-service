package com.example.taxdeclarationservice.dto;

import com.example.taxdeclarationservice.model.CustomEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleDTO extends CustomEntity {

    private String id;
    private String name;
}
