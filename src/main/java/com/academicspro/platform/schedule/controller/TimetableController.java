package com.academicspro.platform.schedule.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.schedule.dto.CreateSlotRequest;
import com.academicspro.platform.schedule.dto.TimetableMatrixDto;
import com.academicspro.platform.schedule.model.TimetableSlot;
import com.academicspro.platform.schedule.service.TimetableService;
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
@RequestMapping("/api/v1/schedule")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    @GetMapping("/matrix")
    public ResponseEntity<ApiResponse<TimetableMatrixDto>> getMatrix(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(timetableService.getMatrix(effectiveTenant)));
    }

    @GetMapping("/slots")
    public ResponseEntity<ApiResponse<List<TimetableSlot>>> getSlots(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @RequestParam(value = "section", required = false) String section,
            @RequestParam(value = "teacherName", required = false) String teacherName) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(timetableService.getSlots(effectiveTenant, section, teacherName)));
    }

    @PostMapping("/slots")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN, Role.ACADEMIC_COORDINATOR})
    public ResponseEntity<ApiResponse<TimetableSlot>> addSlot(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @Valid @RequestBody CreateSlotRequest request) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        TimetableSlot slot = timetableService.addSlot(effectiveTenant, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(slot));
    }
}
