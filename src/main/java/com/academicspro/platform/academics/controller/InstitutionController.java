package com.academicspro.platform.academics.controller;

import com.academicspro.platform.academics.dto.CreateCampusRequest;
import com.academicspro.platform.academics.dto.InstitutionOverviewDto;
import com.academicspro.platform.academics.model.*;
import com.academicspro.platform.academics.service.InstitutionService;
import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.security.RequireRole;
import com.academicspro.platform.security.Role;
import com.academicspro.platform.tenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/institution")
@RequiredArgsConstructor
public class InstitutionController {

    private final InstitutionService institutionService;

    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<InstitutionOverviewDto>> getOverview(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(institutionService.getOverview(effectiveTenant)));
    }

    @GetMapping("/campuses")
    public ResponseEntity<ApiResponse<List<Campus>>> getCampuses(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(institutionService.getCampuses(effectiveTenant)));
    }

    @PostMapping("/campuses")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN})
    public ResponseEntity<ApiResponse<Campus>> createCampus(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @Valid @RequestBody CreateCampusRequest request) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        Campus campus = institutionService.createCampus(effectiveTenant, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(campus));
    }

    @GetMapping("/academic-years")
    public ResponseEntity<ApiResponse<List<AcademicYear>>> getAcademicYears(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(institutionService.getAcademicYears(effectiveTenant)));
    }

    @GetMapping("/academic-years/current")
    public ResponseEntity<ApiResponse<AcademicYear>> getCurrentAcademicYear(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return institutionService.getCurrentAcademicYear(effectiveTenant)
                .map(ay -> ResponseEntity.ok(ApiResponse.ok(ay)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("NOT_FOUND", "No active academic year found for tenant")));
    }

    @GetMapping("/classes")
    public ResponseEntity<ApiResponse<List<ClassGrade>>> getClassGrades(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(institutionService.getClassGrades(effectiveTenant)));
    }

    @GetMapping("/subjects")
    public ResponseEntity<ApiResponse<List<Subject>>> getSubjects(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(institutionService.getSubjects(effectiveTenant)));
    }

    @GetMapping("/classrooms")
    public ResponseEntity<ApiResponse<List<Classroom>>> getClassrooms(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(institutionService.getClassrooms(effectiveTenant)));
    }
}
