package com.kacperp.mobilehub.service;

import com.kacperp.mobilehub.dto.RoleDto;
import com.kacperp.mobilehub.model.Role;
import java.util.List;
import java.util.Optional;

public interface IRoleService {
    RoleDto findById(Long id);
    List<RoleDto> findAll();
    Optional<Role> findByName(String name);
    RoleDto convertEntityToDto(Role role);
    void save(RoleDto roleDto);
    Role findOrCreate(String roleName);
}
