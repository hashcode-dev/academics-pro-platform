package com.academicspro.platform.cms.controller;

import com.academicspro.platform.cms.dto.CmsPublishResponse;
import com.academicspro.platform.cms.dto.PublishContentRequest;
import com.academicspro.platform.cms.model.*;
import com.academicspro.platform.cms.service.CmsService;
import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CmsControllerTest {

    private CmsService cmsService;
    private CmsController cmsController;

    @BeforeEach
    void setUp() {
        cmsService = new CmsService();
        cmsController = new CmsController(cmsService);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetWebsiteFeedBySlugFound() {
        ResponseEntity<ApiResponse<TenantWebsiteFeed>> response = cmsController.getWebsiteFeedBySlug("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Apex High School", response.getBody().getData().getInstitutionName());
    }

    @Test
    void testGetWebsiteFeedBySlugNotFound() {
        ResponseEntity<ApiResponse<TenantWebsiteFeed>> response = cmsController.getWebsiteFeedBySlug("unknown-slug");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testGetCurrentTenantFeed() {
        TenantContext.set(TenantContextData.builder().tenantId("apex-high").build());
        ResponseEntity<ApiResponse<TenantWebsiteFeed>> response = cmsController.getCurrentTenantFeed(null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("apex-high", response.getBody().getData().getTenantId());
    }

    @Test
    void testPublishFeed() {
        TenantContext.set(TenantContextData.builder().tenantId("apex-high").userId("admin-test").build());
        PublishContentRequest request = PublishContentRequest.builder()
                .hero(HeroSection.builder().headline("Updated Headline").build())
                .build();

        ResponseEntity<ApiResponse<CmsPublishResponse>> response = cmsController.publishFeed("apex-high", request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("PUBLISHED", response.getBody().getData().getStatus());
    }

    @Test
    void testGetPrograms() {
        ResponseEntity<ApiResponse<List<AcademicProgram>>> response = cmsController.getPrograms("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getData().size());
    }

    @Test
    void testGetTestimonials() {
        ResponseEntity<ApiResponse<List<Testimonial>>> response = cmsController.getTestimonials("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().getData().size());
    }

    @Test
    void testGetEvents() {
        ResponseEntity<ApiResponse<List<SchoolEvent>>> response = cmsController.getEvents("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getData().size());
    }

    @Test
    void testAddEvent() {
        SchoolEvent event = SchoolEvent.builder()
                .title("Winter Math Olympiad")
                .eventDate(LocalDate.of(2026, 12, 10))
                .build();

        ResponseEntity<ApiResponse<SchoolEvent>> response = cmsController.addEvent("apex-high", event);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getData().getId());
    }

    @Test
    void testUpdateHero() {
        TenantContext.set(TenantContextData.builder().userId("editor-user").build());
        HeroSection hero = HeroSection.builder()
                .headline("New Headline from Test")
                .build();

        ResponseEntity<ApiResponse<HeroSection>> response = cmsController.updateHero("apex-high", hero);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("New Headline from Test", response.getBody().getData().getHeadline());
    }
}
