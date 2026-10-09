package com.academicspro.platform.tenancy;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class TenantFilter extends OncePerRequestFilter {

    public static final String TENANT_HEADER = "X-Tenant-ID";
    public static final String INSTITUTION_HEADER = "X-Institution-ID";
    public static final String MDC_TENANT_KEY = "tenantId";
    public static final String DEFAULT_TENANT = "apex-high";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String tenantId = request.getHeader(TENANT_HEADER);
        String institutionId = request.getHeader(INSTITUTION_HEADER);

        // Fallback: check query parameter or default
        if (tenantId == null || tenantId.trim().isEmpty()) {
            tenantId = request.getParameter("tenantId");
        }
        if (tenantId == null || tenantId.trim().isEmpty()) {
            tenantId = DEFAULT_TENANT;
        }

        if (institutionId == null || institutionId.trim().isEmpty()) {
            institutionId = "inst-" + tenantId;
        }

        TenantContextData currentData = TenantContext.get();
        if (currentData == null) {
            currentData = TenantContextData.builder()
                    .tenantId(tenantId)
                    .institutionId(institutionId)
                    .build();
        } else {
            currentData.setTenantId(tenantId);
            currentData.setInstitutionId(institutionId);
        }

        TenantContext.set(currentData);
        MDC.put(MDC_TENANT_KEY, tenantId);
        response.setHeader(TENANT_HEADER, tenantId);

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
            MDC.remove(MDC_TENANT_KEY);
        }
    }
}
