package com.academicspro.platform.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.security.RequireRole;
import com.academicspro.platform.security.Role;
import com.academicspro.platform.tenancy.TenantContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class GatewayRouteController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, Object>>> healthCheck() {
        Map<String, Object> health = new LinkedHashMap<>();
        health.put("status", "UP");
        health.put("timestamp", Instant.now().toString());
        health.put("service", "academics-pro-platform");
        health.put("currentTenant", TenantContext.getTenantId());
        health.put("activeUser", TenantContext.getUserId());
        health.put("systemTime", System.currentTimeMillis());
        return ResponseEntity.ok(ApiResponse.ok(health));
    }

    @GetMapping("/routes")
    public ResponseEntity<ApiResponse<List<Map<String, String>>>> listGatewayRoutes() {
        List<Map<String, String>> routes = List.of(
                Map.of("path", "/api/v1/auth/**", "target", "Identity and Tenancy Service", "auth", "Public/JWT"),
                Map.of("path", "/api/v1/tenants/**", "target", "Tenancy Management", "auth", "Public/Tenant"),
                Map.of("path", "/api/v1/academics/**", "target", "Institution & Academics Service", "auth", "Role Protected"),
                Map.of("path", "/api/v1/people/**", "target", "People & Admissions Service", "auth", "Role Protected / Public Form"),
                Map.of("path", "/api/v1/scheduling/**", "target", "Scheduling & Timetable Service", "auth", "Role Protected"),
                Map.of("path", "/api/v1/finance/**", "target", "Finance & Payroll Service", "auth", "Finance Role Protected"),
                Map.of("path", "/api/v1/cms/**", "target", "Content CMS Service", "auth", "Public Read / Coordinator Write")
        );
        return ResponseEntity.ok(ApiResponse.ok(routes));
    }

    // CMS Landing Content for Tenant School Website (UI-2 / BE-5 contract)
    @GetMapping("/cms/landing")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTenantSchoolCmsContent(
            @RequestParam(defaultValue = "apex-high") String tenantId) {

        Map<String, Object> cms = new LinkedHashMap<>();

        cms.put("hero", Map.of(
                "headline", "Empowering Tomorrow's Pioneers with Academic Rigor & Character",
                "subline", "Providing an exceptional Pre-K through Grade 12 educational journey cultivating curiosity, critical inquiry, and global leadership.",
                "primaryCta", Map.of("label", "Book an Open Day", "href", "#enquiry"),
                "secondaryCta", Map.of("label", "Explore Programs", "href", "#programs"),
                "badgeText", "Admissions Open for Academic Year 2026–27"
        ));

        cms.put("trustBar", Map.of(
                "stats", List.of(
                        Map.of("label", "University Placement", "value", "98.4%"),
                        Map.of("label", "Established Since", "value", "1982"),
                        Map.of("label", "Student Body", "value", "1,250+"),
                        Map.of("label", "Average Class Size", "value", "16:1")
                ),
                "accreditations", List.of("IB World School", "Cambridge Assessment", "CIS Certified", "STEM Alliance")
        ));

        cms.put("programs", List.of(
                Map.of("id", "prog-primary", "category", "Primary", "name", "Primary Years Programme (Ages 4–11)", "description", "Fostering innate wonder through foundational literacy, discovery math, and playful science.", "curriculum", "IB PYP"),
                Map.of("id", "prog-middle", "category", "Middle", "name", "Middle Years Exploration (Ages 11–14)", "description", "Rigorous interdisciplinary curriculum designed to stimulate critical thinking and scientific inquiry.", "curriculum", "Cambridge Checkpoint"),
                Map.of("id", "prog-senior", "category", "Senior", "name", "Senior Diploma & AP Capstone (Ages 15–18)", "description", "Pre-university curriculum with advanced dual-enrollment credits and world-class university counseling.", "curriculum", "IB DP & AP Courses"),
                Map.of("id", "prog-stem", "category", "Extracurricular", "name", "Robotics & Applied AI Atelier", "description", "Hands-on engineering laboratories mentored by MIT and Caltech research alumni.", "curriculum", "STEM Elective")
        ));

        cms.put("testimonials", List.of(
                Map.of("quote", "Apex transformed my daughter's perspective on STEM. The faculty mentors don't just teach subjects; they inspire students to believe in their unique potential.", "author", "Eleanor Vance", "role", "Grade 11 Parent", "avatar", "EV"),
                Map.of("quote", "The collaborative culture and IB Diploma preparation gave me a seamless transition to Oxford University. I felt prepared from day one.", "author", "Julian Voss", "role", "Alumnus (Class of 2024)", "avatar", "JV"),
                Map.of("quote", "Every child is known, respected, and championed. As educators, we are supported with world-class facilities and continuous professional development.", "author", "Marcus Brody", "role", "Lead Physics Faculty", "avatar", "MB")
        ));

        cms.put("events", List.of(
                Map.of("id", "evt-1", "title", "Spring Campus Open Day & STEM Showcase", "date", "2026-10-24", "time", "10:00 AM - 1:00 PM", "location", "Main Auditorium & Innovation Lab", "spotsAvailable", 24),
                Map.of("id", "evt-2", "title", "Virtual Admissions Roundtable with Head of School", "date", "2026-11-05", "time", "6:00 PM - 7:30 PM", "location", "Live Interactive Webcast", "spotsAvailable", 50),
                Map.of("id", "evt-3", "title", "Annual Performing Arts Gala & Symphony", "date", "2026-11-18", "time", "5:30 PM - 8:30 PM", "location", "Grand Theater Hall", "spotsAvailable", 15)
        ));

        cms.put("news", List.of(
                Map.of("id", "news-1", "title", "Apex Robotics Team Clinches First Place in National Science Olympiad", "date", "October 2, 2026", "category", "Achievements", "readTime", "3 min read"),
                Map.of("id", "news-2", "title", "Introducing Sustainable Energy & Environmental Science Lab for Middle Schoolers", "date", "September 24, 2026", "category", "Campus Update", "readTime", "4 min read")
        ));

        return ResponseEntity.ok(ApiResponse.ok(cms));
    }

    // Admissions enquiry submission (People & Admissions contract)
    @PostMapping("/people/admissions/enquiry")
    public ResponseEntity<ApiResponse<Map<String, Object>>> submitAdmissionsEnquiry(@RequestBody Map<String, Object> payload) {
        String confirmationNumber = "ENQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("confirmationNumber", confirmationNumber);
        result.put("status", "SUBMITTED");
        result.put("submittedAt", Instant.now().toString());
        result.put("tenantId", TenantContext.getTenantId());
        result.put("details", payload);
        return ResponseEntity.ok(ApiResponse.ok(result, "Admissions inquiry submitted successfully. Confirmation: " + confirmationNumber));
    }

    // Protected Domain Endpoints for RBAC Verification
    @RequireRole({Role.SCHOOL_ADMIN, Role.ACADEMIC_COORDINATOR, Role.TEACHER})
    @GetMapping("/academics/overview")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getAcademicsOverview() {
        Map<String, Object> overview = Map.of(
                "tenantId", TenantContext.getTenantId(),
                "activeAcademicYear", "2026-2027",
                "currentTerm", "Term 1 (Fall)",
                "totalClasses", 36,
                "totalSections", 72,
                "totalSubjects", 48,
                "totalClassrooms", 54
        );
        return ResponseEntity.ok(ApiResponse.ok(overview));
    }

    @RequireRole({Role.SCHOOL_ADMIN, Role.FINANCE_OFFICER})
    @GetMapping("/finance/summary")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getFinanceSummary() {
        Map<String, Object> summary = Map.of(
                "tenantId", TenantContext.getTenantId(),
                "currency", "USD",
                "totalFeeTarget", 4500000.00,
                "collectedFees", 3890250.00,
                "pendingFees", 609750.00,
                "collectionRatePercentage", 86.4,
                "monthlyPayrollDisbursed", 285400.00,
                "defaultersCount", 14
        );
        return ResponseEntity.ok(ApiResponse.ok(summary));
    }

    @RequireRole({Role.SCHOOL_ADMIN, Role.ACADEMIC_COORDINATOR, Role.TEACHER, Role.STUDENT})
    @GetMapping("/scheduling/timetable")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getTimetableSlots() {
        Map<String, Object> timetable = Map.of(
                "tenantId", TenantContext.getTenantId(),
                "classId", "class-10-A",
                "days", List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
                "periodsPerDay", 7,
                "conflictStatus", "ZERO_CONFLICTS"
        );
        return ResponseEntity.ok(ApiResponse.ok(timetable));
    }
}
