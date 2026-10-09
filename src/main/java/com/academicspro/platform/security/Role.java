package com.academicspro.platform.security;

public enum Role {
    SUPER_ADMIN("Platform Super Administrator"),
    SCHOOL_ADMIN("School/College Principal or Administrator"),
    ACADEMIC_COORDINATOR("Academic Coordinator / Curriculum Lead"),
    TEACHER("Faculty / Teacher"),
    FINANCE_OFFICER("Finance & Accounts Officer"),
    STUDENT("Enrolled Student"),
    PARENT("Parent / Legal Guardian");

    private final String description;

    Role(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Role fromString(String roleName) {
        if (roleName == null) return null;
        try {
            return Role.valueOf(roleName.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
