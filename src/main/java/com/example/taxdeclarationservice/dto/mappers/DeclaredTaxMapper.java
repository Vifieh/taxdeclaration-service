package com.example.taxdeclarationservice.dto.mappers;

import com.example.taxdeclarationservice.dto.TaxDTO;
import com.example.taxdeclarationservice.model.DeclaredTax;
import com.example.taxdeclarationservice.payload.TaxRequestDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.WARN, imports = {UUID.class, LocalDate.class})
public interface DeclaredTaxMapper {

    DeclaredTaxMapper INSTANCE = Mappers.getMapper(DeclaredTaxMapper.class);

    DeclaredTax mapToDeclaredTax(TaxRequestDTO taxRequestDTO);

    TaxDTO mapToTaxDTO(DeclaredTax declaredTax);

    @InheritConfiguration
    List<TaxDTO> mapToDeclaredTaxList(List<DeclaredTax> declaredTaxList);

}
