package com.kacperp.mobilehub.unit;

import com.kacperp.mobilehub.dto.RoleDto;
import com.kacperp.mobilehub.model.Role;
import com.kacperp.mobilehub.repository.RoleRepository;
import com.kacperp.mobilehub.service.impl.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
public class RoleServiceTest {

    private RoleRepository roleRepository = mock(RoleRepository.class);

    private RoleService roleService = new RoleService(roleRepository);

    private Role roleUser;
    private Role roleAdmin;
    private RoleDto roleUserDto;
    private RoleDto roleAdminDto;

    @BeforeEach
    void setUp() {

        roleUser = new Role();
        roleUser.setId(1L);
        roleUser.setName("ROLE_USER");

        roleAdmin = new Role();
        roleAdmin.setId(2L);
        roleAdmin.setName("ROLE_ADMIN");

        roleUserDto = new RoleDto();
        roleUserDto.setId(1L);
        roleUserDto.setName("ROLE_USER");

        roleAdminDto = new RoleDto();
        roleAdminDto.setId(2L);
        roleAdminDto.setName("ROLE_ADMIN");
    }

    @Test
    void testFindById() {
        when(roleRepository.findById(any())).thenReturn(Optional.of(roleUser));

        RoleDto result = roleService.findById(1L);

        assertNotNull(result);
        assertEquals(roleUserDto.getId(), result.getId());
        assertEquals(roleUserDto.getName(), result.getName());
    }

    @Test
    void testFindAll() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(roleUser, roleAdmin));

        List<RoleDto> result = roleService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());

        RoleDto foundRoleUserDto = result.get(0);
        assertEquals(roleUserDto.getId(), foundRoleUserDto.getId());
        assertEquals(roleUserDto.getName(), foundRoleUserDto.getName());

        RoleDto foundRoleAdminDto = result.get(1);
        assertEquals(roleAdminDto.getId(), foundRoleAdminDto.getId());
        assertEquals(roleAdminDto.getName(), foundRoleAdminDto.getName());
    }

    @Test
    void testFindByName() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(roleAdmin));

        Optional<Role> resultUser = roleService.findByName("ROLE_USER");
        Optional<Role> resultAdmin = roleService.findByName("ROLE_ADMIN");

        assertNotNull(resultUser);
        assertTrue(resultUser.isPresent());
        assertEquals(roleUser.getId(), resultUser.get().getId());
        assertEquals(roleUser.getName(), resultUser.get().getName());

        assertNotNull(resultAdmin);
        assertTrue(resultAdmin.isPresent());
        assertEquals(roleAdmin.getId(), resultAdmin.get().getId());
        assertEquals(roleAdmin.getName(), resultAdmin.get().getName());
    }

    @Test
    void testConvertEntityToDto() {
        RoleDto resultUser = roleService.convertEntityToDto(roleUser);
        RoleDto resultAdmin = roleService.convertEntityToDto(roleAdmin);

        assertNotNull(resultUser);
        assertEquals(roleUserDto.getId(), resultUser.getId());
        assertEquals(roleUserDto.getName(), resultUser.getName());

        assertNotNull(resultAdmin);
        assertEquals(roleAdminDto.getId(), resultAdmin.getId());
        assertEquals(roleAdminDto.getName(), resultAdmin.getName());
    }

    @Test
    void testFindOrCreate() {
        Role roleNew = new Role();
        roleNew.setId(3L);
        roleNew.setName("ROLE_NEW");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser));
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(roleAdmin));
        when(roleRepository.save(argThat(role -> role.getName().equals("ROLE_NEW")))).thenReturn(roleNew);

        Role resultExistingUser = roleService.findOrCreate("ROLE_USER");
        Role resultExistingAdmin = roleService.findOrCreate("ROLE_ADMIN");
        Role resultNewRole = roleService.findOrCreate("ROLE_NEW");

        assertNotNull(resultExistingUser);
        assertEquals(roleUser.getId(), resultExistingUser.getId());
        assertEquals(roleUser.getName(), resultExistingUser.getName());

        assertNotNull(resultExistingAdmin);
        assertEquals(roleAdmin.getId(), resultExistingAdmin.getId());
        assertEquals(roleAdmin.getName(), resultExistingAdmin.getName());

        assertNotNull(resultNewRole);
        assertEquals("ROLE_NEW", resultNewRole.getName());
    }

}