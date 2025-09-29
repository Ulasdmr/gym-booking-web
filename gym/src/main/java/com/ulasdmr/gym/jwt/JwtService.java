package com.ulasdmr.gym.jwt;

import com.ulasdmr.gym.enums.UserType;
import com.ulasdmr.gym.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {

    @Value("${jwt.secret:qANLG4aJTCBQOMKRaS31mAHm5v6x+hEDnnUY8F6TBAQ=}")
    private String secret;

    @Value("${jwt.expires-in-seconds:3600}")
    private long expiresInSeconds;


    public String generateToken(String email) {
        return buildToken(new HashMap<>(), email, toMillis(expiresInSeconds));
    }


    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", user.getId());
        UserType type = user.getUserType();
        if (type != null) {
            claims.put("role", type.name());
        }
        return buildToken(claims, user.getEmail(), toMillis(expiresInSeconds));
    }

    private String buildToken(Map<String, Object> claims, String emailSubject, long validityMillis) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(emailSubject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validityMillis))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    public String getEmailByToken(String token) {
        return exportToken(token, Claims::getSubject);
    }


    public String getRoleByToken(String token) {
        return exportToken(token, c -> c.get("role", String.class));
    }


    public Long getUserIdByToken(String token) {
        return exportToken(token, c -> c.get("uid", Long.class));
    }

    public boolean validateToken(String token) {
        try {
            Date exp = exportToken(token, Claims::getExpiration);
            return new Date().before(exp);
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenValidForEmail(String token, String expectedEmail) {
        final String email = getEmailByToken(token);
        return email != null && email.equals(expectedEmail) && validateToken(token);
    }

    public <T> T exportToken(String token, Function<Claims, T> claimsFunction) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsFunction.apply(claims);
    }


    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private long toMillis(long seconds) {
        return Duration.ofSeconds(seconds).toMillis();
    }
}
