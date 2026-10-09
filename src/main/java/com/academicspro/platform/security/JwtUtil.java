package com.academicspro.platform.security;

import com.academicspro.platform.tenancy.TenantContextData;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final long expirationMs;

    public JwtUtil(
            @Value("${app.jwt.secret:academics-pro-super-secure-jwt-secret-key-for-saas-platform-2026-production-grade}") String secret,
            @Value("${app.jwt.expiration-ms:86400000}") long expirationMs) {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(algorithm).build();
        this.expirationMs = expirationMs;
    }

    public String generateToken(String userId, String email, String name, String tenantId, String institutionId, Set<Role> roles) {
        List<String> roleNames = roles != null ? roles.stream().map(Role::name).collect(Collectors.toList()) : Collections.emptyList();
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return JWT.create()
                .withSubject(userId)
                .withClaim("email", email)
                .withClaim("name", name)
                .withClaim("tenantId", tenantId)
                .withClaim("institutionId", institutionId)
                .withClaim("roles", roleNames)
                .withIssuedAt(now)
                .withExpiresAt(expiry)
                .sign(algorithm);
    }

    public TenantContextData validateAndExtract(String token) throws JWTVerificationException {
        DecodedJWT decoded = verifier.verify(token);

        String userId = decoded.getSubject();
        String email = decoded.getClaim("email").asString();
        String name = decoded.getClaim("name").asString();
        String tenantId = decoded.getClaim("tenantId").asString();
        String institutionId = decoded.getClaim("institutionId").asString();
        List<String> roleStrings = decoded.getClaim("roles").asList(String.class);

        Set<Role> roles = new HashSet<>();
        if (roleStrings != null) {
            for (String r : roleStrings) {
                Role role = Role.fromString(r);
                if (role != null) {
                    roles.add(role);
                }
            }
        }

        return TenantContextData.builder()
                .userId(userId)
                .userEmail(email)
                .userName(name)
                .tenantId(tenantId)
                .institutionId(institutionId)
                .roles(roles)
                .build();
    }
}
