package com.academicspro.platform.security;

import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthFilterTest {

    private JwtUtil jwtUtil;
    private AuthFilter authFilter;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil("secret-key-for-auth-filter-testing-32-chars-long", 3600000);
        authFilter = new AuthFilter(jwtUtil);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testFilterWithNoAuthHeader() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        authFilter.doFilterInternal(request, response, chain);
        assertNull(TenantContext.getUserId());
    }

    @Test
    void testFilterWithValidBearerToken() throws Exception {
        String token = jwtUtil.generateToken("usr-test", "test@apexhigh.edu", "Tester", "apex-high", "inst-apex", Set.of(Role.SCHOOL_ADMIN));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AuthFilter.AUTH_HEADER, "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        authFilter.doFilterInternal(request, response, chain);

        assertEquals("usr-test", TenantContext.getUserId());
        assertEquals("test@apexhigh.edu", TenantContext.get().getUserEmail());
        assertNotNull(request.getAttribute(AuthFilter.AUTHENTICATED_USER_ATTR));
    }

    @Test
    void testFilterWithMalformedTokenDoesNotThrow() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AuthFilter.AUTH_HEADER, "Bearer malformed.token.value");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        authFilter.doFilterInternal(request, response, chain);
        assertNull(TenantContext.getUserId());
    }
}
