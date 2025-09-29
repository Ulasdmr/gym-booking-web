package com.ulasdmr.gym.service;

import com.ulasdmr.gym.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(u -> new org.springframework.security.core.userdetails.User(
                        u.getEmail(),
                        u.getPassword(),
                        List.of(new SimpleGrantedAuthority(u.getUserType().name()))
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }
}
