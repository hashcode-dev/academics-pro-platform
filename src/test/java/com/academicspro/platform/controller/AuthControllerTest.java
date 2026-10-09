package com.academicspro.platform.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.dto.AuthResponse;
import com.academicspro.platform.dto.LoginRequest;
import com.academicspro.platform.security.JwtUtil;
import com.academicspro.platform.security.Role;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private AuthController authController;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("test-jwt-secret-for-auth-controller-unit-testing-32-chars", 86400000);
        authController = new AuthController(jwtUtil);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testLoginWithAdminUser() {
        LoginRequest request = LoginRequest.builder()
                .email("admin@apexhigh.edu")
                .password("password123")
                .tenantId("apex-high")
                .build();

        ResponseEntity<ApiResponse<AuthResponse>> response = authController.login(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());

        AuthResponse authData = response.getBody().getData();
        assertNotNull(authData.getToken());
        assertEquals("Bearer", authData.getTokenType());
        assertEquals("admin@apexhigh.edu", authData.getUser().getEmail());
        assertTrue(authData.getUser().getRoles().contains(Role.SCHOOL_ADMIN));
    }

    @Test
    void testGetCurrentUserWhenAuthenticated() {
        TenantContext.set(TenantContextData.builder()
                .userId("usr-123")
                .userEmail("teacher@apexhigh.edu")
                .userName("Marcus Brody")
                .tenantId("apex-high")
                .institutionId("inst-apex-high")
                .roles(Set.of(Role.TEACHER))
                .build());

        ResponseEntity<ApiResponse<AuthResponse.UserDto>> response = authController.getCurrentUser();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Marcus Brody", response.getBody().getData().getName());
        assertEquals("teacher@apexhigh.edu", response.getBody().getData().getEmail());
    }

    @Test
    void testGetCurrentUserWhenNotAuthenticated() {
        TenantContext.clear();
        ResponseEntity<ApiResponse<AuthResponse.UserDto>> response = authController.getCurrentUser();
        assertEquals(401, response.getStatusCode().value());
    }
}
