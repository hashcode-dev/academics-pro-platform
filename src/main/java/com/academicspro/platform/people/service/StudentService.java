package com.academicspro.platform.people.service;

import com.academicspro.platform.people.dto.CreateStudentRequest;
import com.academicspro.platform.people.model.GuardianContact;
import com.academicspro.platform.people.model.StudentRecord;
import com.academicspro.platform.tenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class StudentService {

    private final Map<String, List<StudentRecord>> studentStore = new ConcurrentHashMap<>();

    public StudentService() {
        seedApexHighStudents();
    }

    private void seedApexHighStudents() {
        String tenantId = "apex-high";

        List<StudentRecord> students = List.of(
                StudentRecord.builder()
                        .id("stu-1")
                        .tenantId(tenantId)
                        .studentId("APX-2024-001")
                        .name("Julian Voss")
                        .grade("Grade 11")
                        .section("A")
                        .rollNumber("11-A-04")
                        .attendanceRate(96.8)
                        .gpa(3.94)
                        .status("ACTIVE")
                        .guardian(GuardianContact.builder().name("Thomas Voss").relationship("Father").phone("+1 (555) 492-8819").email("t.voss@bostonbiotech.com").build())
                        .feeStatus("PAID")
                        .enrollmentDate(LocalDate.of(2024, 8, 20))
                        .advisorName("Sarah Lin")
                        .build(),
                StudentRecord.builder()
                        .id("stu-2")
                        .tenantId(tenantId)
                        .studentId("APX-2024-002")
                        .name("Sophia Chen")
                        .grade("Grade 11")
                        .section("A")
                        .rollNumber("11-A-05")
                        .attendanceRate(98.2)
                        .gpa(4.00)
                        .status("ACTIVE")
                        .guardian(GuardianContact.builder().name("David Chen").relationship("Father").phone("+1 (555) 492-9901").email("d.chen@mit.edu").build())
                        .feeStatus("PAID")
                        .enrollmentDate(LocalDate.of(2024, 8, 20))
                        .advisorName("Sarah Lin")
                        .build(),
                StudentRecord.builder()
                        .id("stu-3")
                        .tenantId(tenantId)
                        .studentId("APX-2024-003")
                        .name("Liam O'Connor")
                        .grade("Grade 10")
                        .section("B")
                        .rollNumber("10-B-12")
                        .attendanceRate(91.5)
                        .gpa(3.72)
                        .status("ACTIVE")
                        .guardian(GuardianContact.builder().name("Fiona O'Connor").relationship("Mother").phone("+1 (555) 302-1144").email("fiona.oc@lawfirm.com").build())
                        .feeStatus("PENDING")
                        .enrollmentDate(LocalDate.of(2025, 8, 22))
                        .advisorName("Marcus Brody")
                        .build(),
                StudentRecord.builder()
                        .id("stu-4")
                        .tenantId(tenantId)
                        .studentId("APX-2024-004")
                        .name("Amara Patel")
                        .grade("Grade 12")
                        .section("A")
                        .rollNumber("12-A-01")
                        .attendanceRate(99.1)
                        .gpa(3.98)
                        .status("ACTIVE")
                        .guardian(GuardianContact.builder().name("Rajesh Patel").relationship("Father").phone("+1 (555) 881-2299").email("rajesh.patel@globaltech.com").build())
                        .feeStatus("PAID")
                        .enrollmentDate(LocalDate.of(2023, 8, 25))
                        .advisorName("Dr. Arthur Vance")
                        .build(),
                StudentRecord.builder()
                        .id("stu-5")
                        .tenantId(tenantId)
                        .studentId("APX-2024-005")
                        .name("Mateo Alvarez")
                        .grade("Grade 9")
                        .section("C")
                        .rollNumber("09-C-18")
                        .attendanceRate(72.4)
                        .gpa(2.85)
                        .status("ACTIVE")
                        .guardian(GuardianContact.builder().name("Carmen Alvarez").relationship("Mother").phone("+1 (555) 601-3321").email("carmen@designstudio.org").build())
                        .feeStatus("OVERDUE")
                        .enrollmentDate(LocalDate.of(2026, 8, 25))
                        .advisorName("Marcus Brody")
                        .build()
        );

        studentStore.put(tenantId, new ArrayList<>(students));
    }

    private String resolveTenant(String tenantId) {
        if (tenantId != null && !tenantId.isBlank()) return tenantId;
        String ctxTenant = TenantContext.getTenantId();
        return (ctxTenant != null && !ctxTenant.isBlank()) ? ctxTenant : "apex-high";
    }

    public List<StudentRecord> getStudents(String tenantId) {
        String tid = resolveTenant(tenantId);
        return studentStore.getOrDefault(tid, Collections.emptyList());
    }

    public Optional<StudentRecord> getStudentById(String tenantId, String id) {
        return getStudents(tenantId).stream()
                .filter(s -> s.getId().equalsIgnoreCase(id) || s.getStudentId().equalsIgnoreCase(id))
                .findFirst();
    }

    public StudentRecord createStudent(String tenantId, CreateStudentRequest request) {
        String tid = resolveTenant(tenantId);
        String generatedId = "stu-" + UUID.randomUUID().toString().substring(0, 6);
        String studentId = "APX-2026-" + String.format("%03d", (int)(Math.random() * 900) + 100);

        StudentRecord student = StudentRecord.builder()
                .id(generatedId)
                .tenantId(tid)
                .studentId(studentId)
                .name(request.getName())
                .grade(request.getGrade())
                .section(request.getSection())
                .rollNumber(request.getRollNumber() != null ? request.getRollNumber() : "SEC-01")
                .attendanceRate(100.0)
                .gpa(request.getGpa() > 0 ? request.getGpa() : 3.80)
                .status("ACTIVE")
                .guardian(GuardianContact.builder()
                        .name(request.getGuardianName())
                        .phone(request.getGuardianPhone())
                        .email(request.getGuardianEmail())
                        .build())
                .feeStatus("PENDING")
                .enrollmentDate(LocalDate.now())
                .advisorName("Academic Dean")
                .build();

        studentStore.computeIfAbsent(tid, k -> new ArrayList<>()).add(student);
        return student;
    }
}
