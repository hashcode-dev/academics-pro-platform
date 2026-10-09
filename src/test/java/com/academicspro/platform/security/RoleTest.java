package com.academicspro.platform.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testRoleFromString() {
        assertEquals(Role.SUPER_ADMIN, Role.fromString("SUPER_ADMIN"));
        assertEquals(Role.SCHOOL_ADMIN, Role.fromString("school_admin"));
        assertEquals(Role.TEACHER, Role.fromString("TEACHER "));
        assertNull(Role.fromString("INVALID_ROLE"));
        assertNull(Role.fromString(null));
    }

    @Test
    void testRoleDescription() {
        assertNotNull(Role.SUPER_ADMIN.getDescription());
        assertNotNull(Role.FINANCE_OFFICER.getDescription());
    }
}
