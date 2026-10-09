package com.academicspro.platform.tenancy;

import com.academicspro.platform.security.Role;
import lombok.Builder;
import lombok.Data;

import java.util.Collections;
import java.util.Set;

@Data
@Builder
public class TenantContextData {
    private String tenantId;
    private String institutionId;
    private String institutionName;
    private String userId;
    private String userEmail;
    private String userName;
    @Builder.Default
    private Set<Role> roles = Collections.emptySet();

    public boolean hasRole(Role role) {
        return roles != null && roles.contains(role);
    }
}
