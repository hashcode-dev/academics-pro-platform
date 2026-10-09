package com.academicspro.platform.tenancy;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class TenantFilterTest {

    private final TenantFilter filter = new TenantFilter();

    @Test
    void testTenantHeaderExtractedAndCleanedUp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(TenantFilter.TENANT_HEADER, "oakridge-academy");
        MockHttpServletResponse response = new MockHttpServletResponse();

        final String[] capturedTenant = new String[1];
        MockFilterChain chain = new MockFilterChain() {
            @Override
            public void doFilter(jakarta.servlet.ServletRequest req, jakarta.servlet.ServletResponse res) {
                capturedTenant[0] = TenantContext.getTenantId();
            }
        };

        filter.doFilterInternal(request, response, chain);

        assertEquals("oakridge-academy", capturedTenant[0]);
        assertEquals("oakridge-academy", response.getHeader(TenantFilter.TENANT_HEADER));
        // Ensure cleanup happened
        assertNull(TenantContext.getTenantId());
    }

    @Test
    void testDefaultTenantUsedWhenHeaderMissing() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        final String[] capturedTenant = new String[1];
        MockFilterChain chain = new MockFilterChain() {
            @Override
            public void doFilter(jakarta.servlet.ServletRequest req, jakarta.servlet.ServletResponse res) {
                capturedTenant[0] = TenantContext.getTenantId();
            }
        };

        filter.doFilterInternal(request, response, chain);

        assertEquals("apex-high", capturedTenant[0]);
        assertEquals("apex-high", response.getHeader(TenantFilter.TENANT_HEADER));
        assertNull(TenantContext.getTenantId());
    }
}
