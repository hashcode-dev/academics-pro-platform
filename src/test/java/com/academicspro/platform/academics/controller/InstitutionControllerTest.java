package com.academicspro.platform.academics.controller;

import com.academicspro.platform.academics.dto.CreateCampusRequest;
import com.academicspro.platform.academics.dto.InstitutionOverviewDto;
import com.academicspro.platform.academics.model.*;
import com.academicspro.platform.academics.service.InstitutionService;
import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InstitutionControllerTest {

    private InstitutionService institutionService;
    private InstitutionController institutionController;

    @BeforeEach
    void setUp() {
        institutionService = new InstitutionService();
        institutionController = new InstitutionController(institutionService);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetOverview() {
        TenantContext.set(TenantContextData.builder().tenantId("apex-high").build());
        ResponseEntity<ApiResponse<InstitutionOverviewDto>> response = institutionController.getOverview(null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("apex-high", response.getBody().getData().getTenantId());
        assertEquals(2, response.getBody().getData().getTotalCampuses());
    }

    @Test
    void testGetCampuses() {
        ResponseEntity<ApiResponse<List<Campus>>> response = institutionController.getCampuses("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    void testCreateCampus() {
        CreateCampusRequest request = CreateCampusRequest.builder()
                .name("New Campus Wing")
                .code("NEW-WING")
                .address("100 Science Park, Boston, MA")
                .studentCapacity(200)
                .gradesOffered("K-5")
                .build();

        ResponseEntity<ApiResponse<Campus>> response = institutionController.createCampus("apex-high", request);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Campus Wing", response.getBody().getData().getName());
    }

    @Test
    void testGetAcademicYears() {
        ResponseEntity<ApiResponse<List<AcademicYear>>> response = institutionController.getAcademicYears("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getData().size());
    }

    @Test
    void testGetCurrentAcademicYearSuccess() {
        ResponseEntity<ApiResponse<AcademicYear>> response = institutionController.getCurrentAcademicYear("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Academic Year 2026–2027", response.getBody().getData().getName());
    }

    @Test
    void testGetCurrentAcademicYearNotFound() {
        ResponseEntity<ApiResponse<AcademicYear>> response = institutionController.getCurrentAcademicYear("non-existent");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testGetClassGrades() {
        ResponseEntity<ApiResponse<List<ClassGrade>>> response = institutionController.getClassGrades("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getData().size());
    }

    @Test
    void testGetSubjects() {
        ResponseEntity<ApiResponse<List<Subject>>> response = institutionController.getSubjects("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getData().size());
    }

    @Test
    void testGetClassrooms() {
        ResponseEntity<ApiResponse<List<Classroom>>> response = institutionController.getClassrooms("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().getData().size());
    }
}
