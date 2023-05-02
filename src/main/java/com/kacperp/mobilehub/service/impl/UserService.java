package com.kacperp.mobilehub.service.impl;

import com.kacperp.mobilehub.dto.UserDto;
import com.kacperp.mobilehub.model.Role;
import com.kacperp.mobilehub.model.User;
import com.kacperp.mobilehub.repository.UserRepository;
import com.kacperp.mobilehub.service.IRoleService;
import com.kacperp.mobilehub.service.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final IRoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository, IRoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Override
    public UserDto findById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(user.isPresent()) {
            return convertEntityToDto(user.get());
        } else {
            return null;
        }
    }

    @Override
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .map(user -> convertEntityToDto(user))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserDto convertEntityToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setFirstName(user.getFirstname());
        userDto.setLastName(user.getLastname());

        return userDto;
    }

    @Override
    public User convertDtoToEntity(UserDto userDto) {
        User user = new User();
        user.setFirstname(userDto.getFirstName());
        user.setLastname(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));

        return user;
    }

    @Override
    @Transactional
    public void save(UserDto userDto) {
        User user = convertDtoToEntity(userDto);
        Role role = roleService.findOrCreate("ROLE_USER");
        user.setRoles(Collections.singletonList(role));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Optional<User> person = userRepository.findById(id);
        if(person.isPresent()) {
            userRepository.delete(person.get());
        }
    }
}
