package com.academicspro.platform.cms.controller;

import com.academicspro.platform.cms.dto.CmsPublishResponse;
import com.academicspro.platform.cms.dto.PublishContentRequest;
import com.academicspro.platform.cms.model.*;
import com.academicspro.platform.cms.service.CmsService;
import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.security.RequireRole;
import com.academicspro.platform.security.Role;
import com.academicspro.platform.tenancy.TenantContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cms")
@RequiredArgsConstructor
public class CmsController {

    private final CmsService cmsService;

    @GetMapping("/tenant/{tenantSlug}")
    public ResponseEntity<ApiResponse<TenantWebsiteFeed>> getWebsiteFeedBySlug(@PathVariable String tenantSlug) {
        return cmsService.getWebsiteFeedBySlug(tenantSlug)
                .map(feed -> ResponseEntity.ok(ApiResponse.ok(feed)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("NOT_FOUND", "No published website feed found for tenant slug: " + tenantSlug)));
    }

    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<TenantWebsiteFeed>> getCurrentTenantFeed(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        TenantWebsiteFeed feed = cmsService.getWebsiteFeedByTenantId(effectiveTenant);
        return ResponseEntity.ok(ApiResponse.ok(feed));
    }

    @PostMapping("/publish")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN, Role.ACADEMIC_COORDINATOR})
    public ResponseEntity<ApiResponse<CmsPublishResponse>> publishFeed(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @RequestBody PublishContentRequest request) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        String user = TenantContext.getUserId();
        CmsPublishResponse response = cmsService.publishFeed(effectiveTenant, request, user);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @GetMapping("/programs")
    public ResponseEntity<ApiResponse<List<AcademicProgram>>> getPrograms(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(cmsService.getPrograms(effectiveTenant)));
    }

    @GetMapping("/testimonials")
    public ResponseEntity<ApiResponse<List<Testimonial>>> getTestimonials(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(cmsService.getTestimonials(effectiveTenant)));
    }

    @GetMapping("/events")
    public ResponseEntity<ApiResponse<List<SchoolEvent>>> getEvents(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(cmsService.getEvents(effectiveTenant)));
    }

    @PostMapping("/events")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN, Role.ACADEMIC_COORDINATOR})
    public ResponseEntity<ApiResponse<SchoolEvent>> addEvent(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @RequestBody SchoolEvent event) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        SchoolEvent saved = cmsService.addEvent(effectiveTenant, event);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(saved));
    }

    @PutMapping("/hero")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN, Role.ACADEMIC_COORDINATOR})
    public ResponseEntity<ApiResponse<HeroSection>> updateHero(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @RequestBody HeroSection hero) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        String user = TenantContext.getUserId();
        HeroSection updated = cmsService.updateHero(effectiveTenant, hero, user);
        return ResponseEntity.ok(ApiResponse.ok(updated));
    }
}
