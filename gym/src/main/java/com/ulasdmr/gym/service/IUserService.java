package com.ulasdmr.gym.service;

import com.ulasdmr.gym.dto.auth.AuthResponse;
import com.ulasdmr.gym.dto.user.UserDto;
import com.ulasdmr.gym.dto.user.UserLoginDto;
import com.ulasdmr.gym.dto.user.UserRegisterDto;

public interface IUserService {

    AuthResponse register(UserRegisterDto dto);

    AuthResponse login(UserLoginDto dto);

    void updatePassword(Long currentUserId, String oldPassword, String newPassword);

    UserDto getById(Long id);
    UserDto getByEmail(String email);
}

