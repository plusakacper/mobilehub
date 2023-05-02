package com.kacperp.mobilehub.unit;

import com.kacperp.mobilehub.dto.UserDto;
import com.kacperp.mobilehub.model.Role;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.repository.RoleRepository;
import com.kacperp.mobilehub.repository.UserRepository;
import com.kacperp.mobilehub.service.impl.RoleService;
import com.kacperp.mobilehub.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    private UserRepository userRepository = mock(UserRepository.class);
    private RoleRepository roleRepository = mock(RoleRepository.class);

    private RoleService roleService = new RoleService(roleRepository);
    private UserService userService = new UserService(userRepository,roleService);

    private User user;
    private UserDto userDto;
    private Role role;

    @BeforeEach
    void setUp() {

        user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john@example.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmail("john@example.com");
        userDto.setPassword("password123");

        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
    }

    @Test
    void findById() {
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        UserDto result = userService.findById(1L);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getFirstName(), result.getFirstName());
        assertEquals(userDto.getLastName(), result.getLastName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void findAll() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<UserDto> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());

        UserDto foundUserDto = result.get(0);
        assertEquals(userDto.getId(), foundUserDto.getId());
        assertEquals(userDto.getFirstName(), foundUserDto.getFirstName());
        assertEquals(userDto.getLastName(), foundUserDto.getLastName());
        assertEquals(userDto.getEmail(), foundUserDto.getEmail());
    }

    @Test
    void convertEntityToDto() {
        UserDto result = userService.convertEntityToDto(user);

        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getFirstName(), result.getFirstName());
        assertEquals(userDto.getLastName(), result.getLastName());
        assertEquals(userDto.getEmail(), result.getEmail());
    }

    @Test
    void convertDtoToEntity() {
        when(roleService.findOrCreate(anyString())).thenReturn(role);

        User result = userService.convertDtoToEntity(userDto);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        assertNotNull(result);
        assertEquals(user.getFirstname(), result.getFirstname());
        assertEquals(user.getLastname(), result.getLastname());
        assertEquals(user.getEmail(), result.getEmail());
        assertTrue(encoder.matches(userDto.getPassword(), result.getPassword()));
    }
}
