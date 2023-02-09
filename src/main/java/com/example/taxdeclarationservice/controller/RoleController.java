package com.example.taxdeclarationservice.controller;

import com.example.taxdeclarationservice.dto.*;
import com.example.taxdeclarationservice.dto.mappers.RoleMapper;
import com.example.taxdeclarationservice.model.*;
import com.example.taxdeclarationservice.payload.*;
import com.example.taxdeclarationservice.service.serviceImpl.RoleServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/public/roles")
public class RoleController {

    private final RoleServiceImpl roleService;
    private final ModelMapper modelMapper;

    String message = null;

    @PostMapping()
    public ResponseEntity<?> createRole(@RequestBody RoleRequestDTO roleRequestDTO) {
        roleService.createRole(roleRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{roleId}")
    public ResponseEntity<?> updateRole(@PathVariable("roleId") String roleId,
                                                    @RequestBody RoleRequestDTO roleRequestDTO) {
        roleService.editRole(roleId, roleRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping()
    public ResponseEntity<List<RoleDTO>> getRoles() {
        List<Role> roles = roleService.getAllRoles();
        List<RoleDTO> roleDtoList = roles.stream()
                .map(role -> this.modelMapper.map(role, RoleDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok(roleDtoList);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable("roleId") String roleId) {
        Role role = roleService.getRole(roleId);
        return ResponseEntity.ok(RoleMapper.INSTANCE.mapToRoleDTO(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(@PathVariable("roleId") String roleId) {
        roleService.deleteRole(roleId);
        return ResponseEntity.noContent().build();
    }

}
