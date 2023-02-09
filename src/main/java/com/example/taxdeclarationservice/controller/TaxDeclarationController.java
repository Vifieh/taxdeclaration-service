package com.example.taxdeclarationservice.controller;

import com.example.taxdeclarationservice.constant.TaxType;
import com.example.taxdeclarationservice.dto.ResponseMessageDTO;
import com.example.taxdeclarationservice.dto.TaxDTO;
import com.example.taxdeclarationservice.dto.mappers.DeclaredTaxMapper;
import com.example.taxdeclarationservice.model.DeclaredTax;
import com.example.taxdeclarationservice.payload.TaxRequestDTO;
import com.example.taxdeclarationservice.service.serviceImpl.TaxDeclarationServiceImpl;
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
@RequestMapping("api/v1/protected/declareTaxes")
public class TaxDeclarationController {

    private final TaxDeclarationServiceImpl taxDeclarationService;

    private final ModelMapper modelMapper;

    private String message;

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PostMapping()
    public ResponseEntity<TaxDTO> declareTax(@Valid @RequestBody TaxRequestDTO taxRequestDTO) {
        DeclaredTax declaredTax = taxDeclarationService.declareTax(taxRequestDTO);
        TaxDTO taxDTO = modelMapper.map(declaredTax, TaxDTO.class);
        return new ResponseEntity<>(taxDTO, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @PutMapping()
    public ResponseEntity<ResponseMessageDTO> updateDeclaredTax(
                                                @RequestParam("declaredTaxId") String declaredTaxId,
                                                @Valid @RequestBody TaxRequestDTO taxRequestDTO) {
            taxDeclarationService.editDeclaredTax(declaredTaxId, taxRequestDTO);
            message = "Declared tax has been edited Successfully";
            return new ResponseEntity<>(new ResponseMessageDTO(message), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping("/{declaredTaxId}")
    ResponseEntity<TaxDTO> getDeclaredTax(@PathVariable String declaredTaxId) {
        DeclaredTax declaredTax = taxDeclarationService.getDeclaredTax(declaredTaxId);
       TaxDTO taxDTO = modelMapper.map(declaredTax, TaxDTO.class);
        return ResponseEntity.ok(taxDTO);
    }

    @PreAuthorize("hasRole('INDIVIDUAL')")
    @GetMapping()
    ResponseEntity<List<TaxDTO>> getTaxesDeclaredByUser(
            @RequestParam(required = false, name="taxType")TaxType taxType) {
        List<DeclaredTax> declaredTaxList = taxDeclarationService.getTaxesDeclaredByUser(taxType);
        List<TaxDTO> taxDTOList = declaredTaxList.stream().map(declaredTax ->
                this.modelMapper.map(declaredTax, TaxDTO.class)).collect(Collectors.toList());
        return new ResponseEntity<>(taxDTOList, HttpStatus.OK);
    }


    @PreAuthorize("hasRole('INDIVIDUAL')")
    @DeleteMapping()
    ResponseEntity<ResponseMessageDTO> deleteDeclaredTax(@RequestParam("declaredTaxId") String declaredTaxId) {
        taxDeclarationService.deleteDeclaredTax(declaredTaxId);
        message = "Declared tax has been deleted Successfully";
        return new ResponseEntity<>(new ResponseMessageDTO(message), HttpStatus.OK);
    }

}
