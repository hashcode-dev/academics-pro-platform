package com.academicspro.platform.tenancy;

import com.academicspro.platform.security.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TenantContextTest {

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testTenantContextSetAndGet() {
        TenantContextData data = TenantContextData.builder()
                .tenantId("apex-high")
                .institutionId("inst-apex-high")
                .userId("usr-123")
                .userEmail("admin@apexhigh.edu")
                .roles(Set.of(Role.SCHOOL_ADMIN))
                .build();

        TenantContext.set(data);

        assertEquals("apex-high", TenantContext.getTenantId());
        assertEquals("inst-apex-high", TenantContext.getInstitutionId());
        assertEquals("usr-123", TenantContext.getUserId());
        assertTrue(TenantContext.hasRole(Role.SCHOOL_ADMIN));
        assertFalse(TenantContext.hasRole(Role.TEACHER));

        TenantContext.clear();
        assertNull(TenantContext.getTenantId());
        assertNull(TenantContext.getUserId());
    }

    @Test
    void testThreadLocalIsolation() throws InterruptedException {
        TenantContext.set(TenantContextData.builder().tenantId("tenant-1").build());

        Thread otherThread = new Thread(() -> {
            assertNull(TenantContext.getTenantId());
            TenantContext.set(TenantContextData.builder().tenantId("tenant-2").build());
            assertEquals("tenant-2", TenantContext.getTenantId());
            TenantContext.clear();
        });

        otherThread.start();
        otherThread.join();

        assertEquals("tenant-1", TenantContext.getTenantId());
    }
}
