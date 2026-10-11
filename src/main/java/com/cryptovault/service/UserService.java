package com.cryptovault.service;

import com.cryptovault.dto.PasswordChangeDto;
import com.cryptovault.dto.UserRegisterDto;
import com.cryptovault.dto.UserUpdateDto;
import com.cryptovault.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(UserRegisterDto registerDto);
    User updateProfile(Long userId, UserUpdateDto updateDto);
    boolean changePassword(Long userId, PasswordChangeDto passwordDto);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    List<User> getAllUsers();
    User toggleUserActive(Long userId);
    long countTotalUsers();
}
