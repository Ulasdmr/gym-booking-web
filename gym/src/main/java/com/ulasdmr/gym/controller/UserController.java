package com.ulasdmr.gym.controller;

import com.ulasdmr.gym.dto.auth.AuthResponse;
import com.ulasdmr.gym.dto.user.UserDto;
import com.ulasdmr.gym.dto.user.UserLoginDto;
import com.ulasdmr.gym.dto.user.UserRegisterDto;
import com.ulasdmr.gym.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController   {

    private final IUserService userService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody UserRegisterDto dto) {
        return ResponseEntity.ok(userService.register(dto));
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody UserLoginDto dto) {
        return ResponseEntity.ok(userService.login(dto));
    }


    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(@RequestParam String oldPassword,
                                               @RequestParam String newPassword,
                                               Authentication authentication) {

        String email = authentication.getName();
        Long currentUserId = userService.getByEmail(email).getId();
        userService.updatePassword(currentUserId, oldPassword, newPassword);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
    }


    @GetMapping("/me/authorities")
    public ResponseEntity<java.util.List<String>> myAuthorities(org.springframework.security.core.Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).build();
        }
        var list = authentication.getAuthorities()
                .stream().map(org.springframework.security.core.GrantedAuthority::getAuthority)
                .toList();
        return ResponseEntity.ok(list);
    }
}
