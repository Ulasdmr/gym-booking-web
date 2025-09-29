package com.ulasdmr.gym.service.impl;

import com.ulasdmr.gym.dto.auth.AuthResponse;
import com.ulasdmr.gym.dto.user.UserDto;
import com.ulasdmr.gym.dto.user.UserLoginDto;
import com.ulasdmr.gym.dto.user.UserRegisterDto;
import com.ulasdmr.gym.enums.UserType;
import com.ulasdmr.gym.jwt.JwtService;
import com.ulasdmr.gym.model.User;
import com.ulasdmr.gym.repository.UserRepository;
import com.ulasdmr.gym.service.IUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponse register(UserRegisterDto dto) {
        final String email = dto.getEmail() == null ? null : dto.getEmail().trim();

        log.info("REGISTER req email={}, userType={}", email, dto.getUserType());

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already in use");
        }
        if (dto.getPassword() == null || dto.getConfirmPassword() == null) {
            throw new IllegalArgumentException("Password and confirm password are required");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new IllegalStateException("Passwords do not match");
        }

        LocalDate birth = dto.getBirthDate();

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));


        UserType type = (dto.getUserType() != null) ? dto.getUserType() : UserType.USER;
        log.debug("REGISTER mapping userType -> {}", type);
        user.setUserType(type);

        user.setBirthDate(birth != null ? birth.atStartOfDay() : null);

        User saved = userRepository.save(user);


        log.info("REGISTER saved id={}, userType={}", saved.getId(), saved.getUserType());

        String token = jwtService.generateToken(saved);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthResponse login(UserLoginDto dto) {
        final String email = dto.getEmail() == null ? null : dto.getEmail().trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalStateException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public void updatePassword(Long currentUserId, String oldPassword, String newPassword) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (oldPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Passwords cannot be null");
        }
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalStateException("Old password is incorrect");
        }

        if (newPassword.isBlank() || newPassword.length() < 6) {
            throw new IllegalArgumentException("New password must be at least 6 characters");
        }
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalStateException("New password must be different from the old one");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapToDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getByEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        User user = userRepository.findByEmail(email.trim())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return mapToDto(user);
    }

    private UserDto mapToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        return dto;
    }
}
