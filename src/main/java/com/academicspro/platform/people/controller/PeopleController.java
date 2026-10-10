package com.academicspro.platform.people.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.people.dto.*;
import com.academicspro.platform.people.model.*;
import com.academicspro.platform.people.service.*;
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
@RequestMapping("/api/v1/people")
@RequiredArgsConstructor
public class PeopleController {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final AdmissionsService admissionsService;

    @GetMapping("/students")
    public ResponseEntity<ApiResponse<List<StudentRecord>>> getStudents(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(studentService.getStudents(effectiveTenant)));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<ApiResponse<StudentRecord>> getStudentById(
            @PathVariable String id,
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return studentService.getStudentById(effectiveTenant, id)
                .map(s -> ResponseEntity.ok(ApiResponse.ok(s)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("NOT_FOUND", "Student record not found for ID: " + id)));
    }

    @PostMapping("/students")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN, Role.ACADEMIC_COORDINATOR})
    public ResponseEntity<ApiResponse<StudentRecord>> createStudent(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @Valid @RequestBody CreateStudentRequest request) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        StudentRecord student = studentService.createStudent(effectiveTenant, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(student));
    }

    @GetMapping("/teachers")
    public ResponseEntity<ApiResponse<List<TeacherRecord>>> getTeachers(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(teacherService.getTeachers(effectiveTenant)));
    }

    @GetMapping("/teachers/{id}")
    public ResponseEntity<ApiResponse<TeacherRecord>> getTeacherById(
            @PathVariable String id,
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return teacherService.getTeacherById(effectiveTenant, id)
                .map(t -> ResponseEntity.ok(ApiResponse.ok(t)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("NOT_FOUND", "Teacher record not found for ID: " + id)));
    }

    @PostMapping("/teachers")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN})
    public ResponseEntity<ApiResponse<TeacherRecord>> createTeacher(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @Valid @RequestBody CreateTeacherRequest request) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        TeacherRecord teacher = teacherService.createTeacher(effectiveTenant, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(teacher));
    }

    @PostMapping("/admissions/enquiry")
    public ResponseEntity<ApiResponse<EnquiryResponse>> submitAdmissionsEnquiry(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @Valid @RequestBody SubmitEnquiryRequest request) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        EnquiryResponse response = admissionsService.submitEnquiry(effectiveTenant, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(response));
    }

    @GetMapping("/admissions/enquiries")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN, Role.ACADEMIC_COORDINATOR})
    public ResponseEntity<ApiResponse<List<AdmissionsEnquiry>>> getEnquiries(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(admissionsService.getEnquiries(effectiveTenant)));
    }
}
