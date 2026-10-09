package com.academicspro.platform.tenancy;

import com.academicspro.platform.security.Role;

import java.util.Collections;
import java.util.Set;

public final class TenantContext {

    private static final ThreadLocal<TenantContextData> CURRENT_TENANT = new ThreadLocal<>();

    private TenantContext() {
        // Utility class
    }

    public static void set(TenantContextData contextData) {
        CURRENT_TENANT.set(contextData);
    }

    public static TenantContextData get() {
        return CURRENT_TENANT.get();
    }

    public static String getTenantId() {
        TenantContextData data = CURRENT_TENANT.get();
        return data != null ? data.getTenantId() : null;
    }

    public static String getInstitutionId() {
        TenantContextData data = CURRENT_TENANT.get();
        return data != null ? data.getInstitutionId() : null;
    }

    public static String getUserId() {
        TenantContextData data = CURRENT_TENANT.get();
        return data != null ? data.getUserId() : null;
    }

    public static Set<Role> getRoles() {
        TenantContextData data = CURRENT_TENANT.get();
        return data != null && data.getRoles() != null ? data.getRoles() : Collections.emptySet();
    }

    public static boolean hasRole(Role role) {
        TenantContextData data = CURRENT_TENANT.get();
        return data != null && data.hasRole(role);
    }

    public static void clear() {
        CURRENT_TENANT.remove();
    }
}
