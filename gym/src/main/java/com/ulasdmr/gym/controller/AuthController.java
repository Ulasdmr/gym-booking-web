package com.ulasdmr.gym.controller;

import com.ulasdmr.gym.dto.auth.AuthResponse;
import com.ulasdmr.gym.dto.user.UserLoginDto;
import com.ulasdmr.gym.dto.user.UserRegisterDto;
import com.ulasdmr.gym.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegisterDto dto) {
        return ResponseEntity.ok(userService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }
}
