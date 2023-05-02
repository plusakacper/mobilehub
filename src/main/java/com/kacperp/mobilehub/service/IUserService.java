package com.kacperp.mobilehub.service;


import com.kacperp.mobilehub.dto.UserDto;
import com.kacperp.mobilehub.model.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserDto findById(Long id);
    List<UserDto> findAll();
    Optional<User> findByEmail(String email);
    UserDto convertEntityToDto(User user);
    User convertDtoToEntity(UserDto userDto);
    void save(UserDto userDto);
    void delete(Long id);
}
