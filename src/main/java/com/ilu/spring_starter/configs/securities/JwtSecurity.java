package com.ilu.spring_starter.configs.securities;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ilu.spring_starter.entities.User;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtSecurity {
    private final String secretKey = "super-secret";
    private final String issuer = "spring starter";
    private final int validityInSeconds = 3600;
    private final String audience = "spring starter";

    private final Algorithm algorithm = Algorithm.HMAC256(secretKey.getBytes(StandardCharsets.UTF_8));

    public String generateToken(User user) {
        List<String> roles = user.getRoles().stream().map(role -> role.getName()).toList();
        return JWT.create()
                .withSubject(user.getId())
                .withAudience(audience)
                .withIssuer(issuer)
                .withExpiresAt(Instant.now().plusSeconds(validityInSeconds))
                .withIssuedAt(Instant.now())
                .withClaim("roles", roles)
                .sign(algorithm);
    }

    public Map<String, String> validateTokenAndGetData(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(token);

            Map<String, String> data = new HashMap<>();
            data.put("userId", decodedJWT.getSubject());
            data.put("roles", decodedJWT.getClaim("roles").asString());

            return data;
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getUserIdFromJwtToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.decode(token);
            return decodedJWT.getSubject();
        } catch (JWTDecodeException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Menghapus prefix "Bearer "
        }
        return null;
    }

    public String getUserIdFromRequest(HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        if (token != null) {
            return getUserIdFromJwtToken(token);
        }
        return null;
    }

}