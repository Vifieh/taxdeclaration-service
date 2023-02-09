package com.example.taxdeclarationservice.service.serviceImpl;

import com.example.taxdeclarationservice.exception.*;
import com.example.taxdeclarationservice.model.Role;
import com.example.taxdeclarationservice.payload.RoleRequestDTO;
import com.example.taxdeclarationservice.repository.RoleRepository;
import com.example.taxdeclarationservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {


    private final RoleRepository roleRepository;

    @Override
    public void createRole(RoleRequestDTO roleRequestDTO) {
        Optional<Role> optionalRole = roleRepository.findByName(roleRequestDTO.getName());
        if(optionalRole.isPresent()) {
            throw new ResourceAlreadyExistException("Role already exist");
        }
        Role role = new Role();
        role.setName(roleRequestDTO.getName());
        roleRepository.save(role);
    }

    @Override
    public void editRole(String roleId, RoleRequestDTO roleRequestDTO) {
        Role role = getRole(roleId);
        role.setName(roleRequestDTO.getName());
        roleRepository.save(role);
    }

    @Override
    public List<Role> getAllRoles() {
       return roleRepository.findAll();
    }

    @Override
    public Role getRole(String id) {
        Optional<Role> optionalRole = roleRepository.findById(UUID.fromString(id));
        if(optionalRole.isEmpty()){
            throw new ResourceNotFoundException("Role not found");
        }
        return optionalRole.get();

    }

    @Override
    public Role getRoleByName(String name) {
        Optional<Role> optionalRole = roleRepository.findByName(name);
        if(optionalRole.isEmpty()){
            throw new ResourceNotFoundException("Role not found");
        }
        return optionalRole.get();
    }

    @Override
    public void deleteRole(String id) {
        getRole(id);
        roleRepository.deleteById(UUID.fromString(id));
    }
}
