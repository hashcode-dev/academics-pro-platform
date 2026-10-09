package com.academicspro.platform.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.dto.TenantInfoDto;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TenantControllerTest {

    private final TenantController tenantController = new TenantController();

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testListTenants() {
        ResponseEntity<ApiResponse<List<TenantInfoDto>>> response = tenantController.listTenants();
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody().getData());
        assertFalse(response.getBody().getData().isEmpty());
    }

    @Test
    void testGetCurrentTenantWithDefault() {
        TenantContext.clear();
        ResponseEntity<ApiResponse<TenantInfoDto>> response = tenantController.getCurrentTenant();
        assertEquals(200, response.getStatusCode().value());
        assertEquals("apex-high", response.getBody().getData().getTenantId());
    }

    @Test
    void testGetCurrentTenantWithExplicitTenant() {
        TenantContext.set(TenantContextData.builder().tenantId("oakridge-academy").build());
        ResponseEntity<ApiResponse<TenantInfoDto>> response = tenantController.getCurrentTenant();
        assertEquals(200, response.getStatusCode().value());
        assertEquals("oakridge-academy", response.getBody().getData().getTenantId());
    }

    @Test
    void testGetTenantBySlugSuccess() {
        ResponseEntity<ApiResponse<TenantInfoDto>> response = tenantController.getTenantBySlug("apex-high");
        assertEquals(200, response.getStatusCode().value());
        assertEquals("Apex High School", response.getBody().getData().getName());
        assertEquals(1982, response.getBody().getData().getEstablishedYear());
    }

    @Test
    void testGetTenantBySlugNotFound() {
        ResponseEntity<ApiResponse<TenantInfoDto>> response = tenantController.getTenantBySlug("unknown-slug");
        assertEquals(404, response.getStatusCode().value());
        assertFalse(response.getBody().isSuccess());
    }
}
