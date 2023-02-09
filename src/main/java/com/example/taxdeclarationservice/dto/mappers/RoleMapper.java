package com.example.taxdeclarationservice.dto.mappers;

import com.example.taxdeclarationservice.dto.RoleDTO;
import com.example.taxdeclarationservice.model.Role;
import com.example.taxdeclarationservice.payload.RoleRequestDTO;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN, imports = {UUID.class, LocalDate.class})
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    Role mapToRole(RoleRequestDTO roleRequestDTO);

    RoleDTO mapToRoleDTO(Role role);

    @InheritConfiguration
    List<RoleDTO> mapToRoleList(List<Role> roles);

}
