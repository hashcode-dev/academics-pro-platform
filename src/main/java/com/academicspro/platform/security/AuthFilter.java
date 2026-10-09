package com.academicspro.platform.security;

import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 2)
@RequiredArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String AUTHENTICATED_USER_ATTR = "AUTHENTICATED_USER";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            String token = authHeader.substring(BEARER_PREFIX.length()).trim();
            try {
                TenantContextData authData = jwtUtil.validateAndExtract(token);
                // Preserve tenantId from TenantFilter if token didn't specify one
                if (authData.getTenantId() == null || authData.getTenantId().isEmpty()) {
                    String existingTenant = TenantContext.getTenantId();
                    authData.setTenantId(existingTenant);
                }

                TenantContext.set(authData);
                request.setAttribute(AUTHENTICATED_USER_ATTR, authData);
                log.debug("Authenticated user {} for tenant {}", authData.getUserEmail(), authData.getTenantId());
            } catch (JWTVerificationException e) {
                log.warn("Invalid JWT token provided: {}", e.getMessage());
                // For non-public endpoints, the interceptor will reject. For invalid bearer headers, continue with unauthenticated context
            }
        }

        filterChain.doFilter(request, response);
    }
}
