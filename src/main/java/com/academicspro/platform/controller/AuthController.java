package com.academicspro.platform.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.dto.AuthResponse;
import com.academicspro.platform.dto.LoginRequest;
import com.academicspro.platform.security.JwtUtil;
import com.academicspro.platform.security.Role;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    // Simulated user repository for standard tenant roles
    private static final Map<String, DemoUserRecord> DEMO_USERS = new HashMap<>();

    static {
        registerDemoUser("admin@apexhigh.edu", "Dr. Arthur Vance", "apex-high", "inst-apex-high", "Apex High School",
                Set.of(Role.SCHOOL_ADMIN));
        registerDemoUser("coordinator@apexhigh.edu", "Sarah Lin", "apex-high", "inst-apex-high", "Apex High School",
                Set.of(Role.ACADEMIC_COORDINATOR));
        registerDemoUser("teacher@apexhigh.edu", "Marcus Brody", "apex-high", "inst-apex-high", "Apex High School",
                Set.of(Role.TEACHER));
        registerDemoUser("finance@apexhigh.edu", "Elena Rostova", "apex-high", "inst-apex-high", "Apex High School",
                Set.of(Role.FINANCE_OFFICER));
        registerDemoUser("student@apexhigh.edu", "Julian Voss", "apex-high", "inst-apex-high", "Apex High School",
                Set.of(Role.STUDENT));
        registerDemoUser("parent@apexhigh.edu", "Thomas Voss", "apex-high", "inst-apex-high", "Apex High School",
                Set.of(Role.PARENT));
        registerDemoUser("superadmin@academicspro.com", "Chief Administrator", "platform", "inst-platform", "Academics Pro Global",
                Set.of(Role.SUPER_ADMIN, Role.SCHOOL_ADMIN));
    }

    private static void registerDemoUser(String email, String name, String tenantId, String instId, String instName, Set<Role> roles) {
        DEMO_USERS.put(email.toLowerCase(), new DemoUserRecord("usr-" + UUID.randomUUID().toString().substring(0, 8), email, name, tenantId, instId, instName, roles));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        String email = request.getEmail().toLowerCase();
        DemoUserRecord user = DEMO_USERS.get(email);

        String tenantId = request.getTenantId();
        if (tenantId == null || tenantId.isBlank()) {
            tenantId = TenantContext.getTenantId() != null ? TenantContext.getTenantId() : (user != null ? user.tenantId() : "apex-high");
        }

        // Allow demo login for any valid email (generate default SCHOOL_ADMIN if unknown for flexible onboarding)
        if (user == null) {
            String name = email.split("@")[0].replace(".", " ");
            user = new DemoUserRecord("usr-" + UUID.randomUUID().toString().substring(0, 8), email, name, tenantId, "inst-" + tenantId, "Institution " + tenantId, Set.of(Role.SCHOOL_ADMIN));
        }

        String token = jwtUtil.generateToken(
                user.id(),
                user.email(),
                user.name(),
                tenantId,
                user.institutionId(),
                user.roles()
        );

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .expiresInMs(86400000L)
                .user(AuthResponse.UserDto.builder()
                        .id(user.id())
                        .email(user.email())
                        .name(user.name())
                        .tenantId(tenantId)
                        .institutionId(user.institutionId())
                        .institutionName(user.institutionName())
                        .roles(user.roles())
                        .build())
                .build();

        return ResponseEntity.ok(ApiResponse.ok(authResponse, "Authentication successful"));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<AuthResponse.UserDto>> getCurrentUser() {
        TenantContextData context = TenantContext.get();
        if (context == null || context.getUserId() == null) {
            return ResponseEntity.status(401).body(ApiResponse.error("UNAUTHORIZED", "Not authenticated"));
        }

        AuthResponse.UserDto userDto = AuthResponse.UserDto.builder()
                .id(context.getUserId())
                .email(context.getUserEmail())
                .name(context.getUserName())
                .tenantId(context.getTenantId())
                .institutionId(context.getInstitutionId())
                .institutionName(context.getInstitutionName())
                .roles(context.getRoles())
                .build();

        return ResponseEntity.ok(ApiResponse.ok(userDto));
    }

    private record DemoUserRecord(String id, String email, String name, String tenantId, String institutionId, String institutionName, Set<Role> roles) {}
}
