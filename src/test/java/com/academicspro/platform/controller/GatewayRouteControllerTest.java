package com.academicspro.platform.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GatewayRouteControllerTest {

    private final GatewayRouteController controller = new GatewayRouteController();

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testHealthCheck() {
        TenantContext.set(TenantContextData.builder().tenantId("apex-high").userId("usr-1").build());
        ResponseEntity<ApiResponse<Map<String, Object>>> response = controller.healthCheck();
        assertEquals(200, response.getStatusCode().value());
        assertEquals("UP", response.getBody().getData().get("status"));
        assertEquals("apex-high", response.getBody().getData().get("currentTenant"));
    }

    @Test
    void testListGatewayRoutes() {
        ResponseEntity<ApiResponse<List<Map<String, String>>>> response = controller.listGatewayRoutes();
        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().getData().isEmpty());
    }

    @Test
    void testGetTenantSchoolCmsContent() {
        ResponseEntity<ApiResponse<Map<String, Object>>> response = controller.getTenantSchoolCmsContent("apex-high");
        assertEquals(200, response.getStatusCode().value());
        Map<String, Object> cms = response.getBody().getData();
        assertNotNull(cms.get("hero"));
        assertNotNull(cms.get("trustBar"));
        assertNotNull(cms.get("programs"));
        assertNotNull(cms.get("testimonials"));
        assertNotNull(cms.get("events"));
    }

    @Test
    void testSubmitAdmissionsEnquiry() {
        TenantContext.set(TenantContextData.builder().tenantId("apex-high").build());
        Map<String, Object> form = Map.of(
                "parentName", "John Doe",
                "email", "john@example.com",
                "studentAge", 12,
                "gradeApplyingFor", "Grade 7"
        );
        ResponseEntity<ApiResponse<Map<String, Object>>> response = controller.submitAdmissionsEnquiry(form);
        assertEquals(200, response.getStatusCode().value());
        assertTrue(response.getBody().isSuccess());
        assertNotNull(response.getBody().getData().get("confirmationNumber"));
    }

    @Test
    void testDomainEndpoints() {
        TenantContext.set(TenantContextData.builder().tenantId("apex-high").build());
        ResponseEntity<ApiResponse<Map<String, Object>>> academics = controller.getAcademicsOverview();
        assertEquals(200, academics.getStatusCode().value());

        ResponseEntity<ApiResponse<Map<String, Object>>> finance = controller.getFinanceSummary();
        assertEquals(200, finance.getStatusCode().value());

        ResponseEntity<ApiResponse<Map<String, Object>>> scheduling = controller.getTimetableSlots();
        assertEquals(200, scheduling.getStatusCode().value());
    }
}
