package com.academicspro.platform.security;

import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleAuthorizationInterceptorTest {

    private RoleAuthorizationInterceptor interceptor;
    private final ObjectMapper objectMapper = new ObjectMapper();

    static class SampleController {
        @RequireRole(Role.FINANCE_OFFICER)
        public void financeMethod() {}

        public void publicMethod() {}
    }

    @BeforeEach
    void setUp() {
        interceptor = new RoleAuthorizationInterceptor(objectMapper);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testPublicMethodPassesWithoutAuth() throws Exception {
        Method method = SampleController.class.getMethod("publicMethod");
        HandlerMethod handlerMethod = new HandlerMethod(new SampleController(), method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = interceptor.preHandle(request, response, handlerMethod);
        assertTrue(result);
        assertEquals(200, response.getStatus());
    }

    @Test
    void testProtectedMethodRejectsUnauthenticated() throws Exception {
        Method method = SampleController.class.getMethod("financeMethod");
        HandlerMethod handlerMethod = new HandlerMethod(new SampleController(), method);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = interceptor.preHandle(request, response, handlerMethod);
        assertFalse(result);
        assertEquals(401, response.getStatus());
    }

    @Test
    void testProtectedMethodRejectsForbiddenRole() throws Exception {
        Method method = SampleController.class.getMethod("financeMethod");
        HandlerMethod handlerMethod = new HandlerMethod(new SampleController(), method);

        TenantContext.set(TenantContextData.builder()
                .userId("usr-student-1")
                .roles(Set.of(Role.STUDENT))
                .build());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = interceptor.preHandle(request, response, handlerMethod);
        assertFalse(result);
        assertEquals(403, response.getStatus());
    }

    @Test
    void testProtectedMethodAllowsAuthorizedRole() throws Exception {
        Method method = SampleController.class.getMethod("financeMethod");
        HandlerMethod handlerMethod = new HandlerMethod(new SampleController(), method);

        TenantContext.set(TenantContextData.builder()
                .userId("usr-finance-1")
                .roles(Set.of(Role.FINANCE_OFFICER))
                .build());

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean result = interceptor.preHandle(request, response, handlerMethod);
        assertTrue(result);
        assertEquals(200, response.getStatus());
    }
}
