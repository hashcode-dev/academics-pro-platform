package com.academicspro.platform.security;

import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("my-test-secret-key-1234567890-must-be-long-enough", 3600000);
    }

    @Test
    void testTokenGenerationAndExtraction() {
        String token = jwtUtil.generateToken(
                "usr-001",
                "principal@apexhigh.edu",
                "Dr. Vance",
                "apex-high",
                "inst-apex-high",
                Set.of(Role.SCHOOL_ADMIN, Role.ACADEMIC_COORDINATOR)
        );

        assertNotNull(token);

        TenantContextData data = jwtUtil.validateAndExtract(token);
        assertEquals("usr-001", data.getUserId());
        assertEquals("principal@apexhigh.edu", data.getUserEmail());
        assertEquals("Dr. Vance", data.getUserName());
        assertEquals("apex-high", data.getTenantId());
        assertEquals("inst-apex-high", data.getInstitutionId());
        assertTrue(data.hasRole(Role.SCHOOL_ADMIN));
        assertTrue(data.hasRole(Role.ACADEMIC_COORDINATOR));
        assertFalse(data.hasRole(Role.FINANCE_OFFICER));
    }
}
