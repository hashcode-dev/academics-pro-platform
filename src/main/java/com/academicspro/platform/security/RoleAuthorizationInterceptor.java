package com.academicspro.platform.security;

import com.academicspro.platform.tenancy.TenantContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class RoleAuthorizationInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return true;
        }

        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole == null) {
            requireRole = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), RequireRole.class);
        }

        if (requireRole == null) {
            return true; // No role restriction
        }

        String userId = TenantContext.getUserId();
        if (userId == null) {
            writeErrorResponse(response, HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication is required to access this resource");
            return false;
        }

        Set<Role> userRoles = TenantContext.getRoles();
        List<Role> allowedRoles = Arrays.asList(requireRole.value());

        boolean hasPermission = allowedRoles.stream().anyMatch(userRoles::contains);
        if (!hasPermission) {
            log.warn("Access denied for user {} with roles {}. Required: {}", userId, userRoles, allowedRoles);
            writeErrorResponse(response, HttpStatus.FORBIDDEN, "FORBIDDEN",
                    "User does not possess sufficient role privileges. Required: " + allowedRoles);
            return false;
        }

        return true;
    }

    private void writeErrorResponse(HttpServletResponse response, HttpStatus status, String errorCode, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorBody = new LinkedHashMap<>();
        errorBody.put("timestamp", Instant.now().toString());
        errorBody.put("status", status.value());
        errorBody.put("errorCode", errorCode);
        errorBody.put("message", message);
        errorBody.put("tenantId", TenantContext.getTenantId());

        response.getWriter().write(objectMapper.writeValueAsString(errorBody));
    }
}
