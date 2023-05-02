package com.kacperp.mobilehub.service.impl;

import com.kacperp.mobilehub.dto.RoleDto;
import com.kacperp.mobilehub.model.Role;
import com.kacperp.mobilehub.repository.RoleRepository;
import com.kacperp.mobilehub.service.IRoleService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDto findById(Long id)  {
        Optional<Role> role = roleRepository.findById(id);

        if(role.isPresent()) {
            return convertEntityToDto(role.get());
        } else {
            return null;
        }
    }

    @Override
    public List<RoleDto> findAll() {
        List<Role> roles = roleRepository.findAll();

        return roles.stream()
                .map(role -> convertEntityToDto(role))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public RoleDto convertEntityToDto(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());

        return roleDto;
    }

    @Override
    @Transactional
    public void save(RoleDto roleDto) {
        Role role = new Role();
        role.setName(roleDto.getName());
        roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role findOrCreate(String roleName) {
        Optional<Role> existingRole = findByName(roleName);
        if(existingRole.isPresent()) {
            return existingRole.get();
        } else {
            Role role = new Role();
            role.setName(roleName);
            Role newRole = roleRepository.save(role);
            return newRole;
        }
    }
}
