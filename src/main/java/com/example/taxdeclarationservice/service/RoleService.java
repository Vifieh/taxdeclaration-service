package com.example.taxdeclarationservice.service;


import com.example.taxdeclarationservice.model.Role;
import com.example.taxdeclarationservice.payload.RoleRequestDTO;

import java.util.List;

public interface RoleService {

    void createRole(RoleRequestDTO roleRequestDTO);

    void editRole(String roleId, RoleRequestDTO roleRequestDTO);

    List<Role> getAllRoles();

    Role getRole(String id);

    Role getRoleByName(String name);

    void deleteRole(String id);
}
