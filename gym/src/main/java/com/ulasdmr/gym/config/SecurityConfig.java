package com.ulasdmr.gym.config;

import com.ulasdmr.gym.jwt.AuthenticationFilter;
import com.ulasdmr.gym.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@EnableMethodSecurity
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            @Lazy AuthenticationFilter authenticationFilter,
            UserDetailsService userDetailsService) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults()) // <-- CORS aktif

                        // Preflight her yere serbest
                        .authorizeHttpRequests(auth -> auth
                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                .requestMatchers(HttpMethod.POST,
                                        "/api/v1/auth/register",
                                        "/api/v1/auth/login",
                                        "/api/v1/users/register",
                                        "/api/v1/users/login"
                                ).permitAll()


                                .requestMatchers("/api/v1/bookings", "/api/v1/bookings/**").authenticated()

                                .requestMatchers("/api/v1/sessions/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/api/v1/users/me/authorities").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/v1/gyms", "/api/v1/gyms/**").hasRole("ADMIN")
                                .anyRequest().authenticated()
                        )



                .authenticationProvider(authenticationProvider(userDetailsService))
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cfg = new CorsConfiguration();
        // Dev ortamı: Angular dev server
        cfg.setAllowedOriginPatterns(List.of("http://localhost:4200"));
        cfg.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cfg.setAllowedHeaders(List.of("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
        cfg.setExposedHeaders(List.of("Authorization"));
        cfg.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cfg);
        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> userRepository.findByEmail(email)
                .map(u -> {
                    String role = u.getUserType().name();
                    var authorities = java.util.List.of(
                            new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role),
                            new org.springframework.security.core.authority.SimpleGrantedAuthority(role)
                    );
                    return new org.springframework.security.core.userdetails.User(
                            u.getEmail(),
                            u.getPassword(),
                            authorities
                    );
                })
                .orElseThrow(() -> new org.springframework.security.core.userdetails.UsernameNotFoundException("User not found: " + email));
    }
}
