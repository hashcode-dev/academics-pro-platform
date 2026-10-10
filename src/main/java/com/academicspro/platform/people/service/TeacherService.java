package com.academicspro.platform.people.service;

import com.academicspro.platform.people.dto.CreateTeacherRequest;
import com.academicspro.platform.people.model.TeacherRecord;
import com.academicspro.platform.tenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TeacherService {

    private final Map<String, List<TeacherRecord>> teacherStore = new ConcurrentHashMap<>();

    public TeacherService() {
        seedApexHighTeachers();
    }

    private void seedApexHighTeachers() {
        String tenantId = "apex-high";

        List<TeacherRecord> teachers = List.of(
                TeacherRecord.builder()
                        .id("tch-1")
                        .tenantId(tenantId)
                        .employeeId("FAC-101")
                        .name("Dr. Arthur Vance")
                        .designation("Head of School & Senior Faculty")
                        .department("Administration & History")
                        .subjects(List.of("World History AP", "Global Ethics"))
                        .qualifications("Ph.D. Education, Harvard University")
                        .assignedClasses(List.of("Grade 11-A", "Grade 12-A"))
                        .status("ACTIVE")
                        .email("a.vance@apexhigh.edu")
                        .phone("+1 (555) 382-9011")
                        .hireDate(LocalDate.of(2014, 7, 1))
                        .build(),
                TeacherRecord.builder()
                        .id("tch-2")
                        .tenantId(tenantId)
                        .employeeId("FAC-102")
                        .name("Sarah Lin")
                        .designation("Academic Coordinator & Mathematics Chair")
                        .department("Mathematics")
                        .subjects(List.of("Calculus BC", "Linear Algebra"))
                        .qualifications("M.Sc. Pure Mathematics, Stanford")
                        .assignedClasses(List.of("Grade 11-A", "Grade 12-B"))
                        .status("ACTIVE")
                        .email("s.lin@apexhigh.edu")
                        .phone("+1 (555) 382-9012")
                        .hireDate(LocalDate.of(2018, 8, 15))
                        .build(),
                TeacherRecord.builder()
                        .id("tch-3")
                        .tenantId(tenantId)
                        .employeeId("FAC-103")
                        .name("Marcus Brody")
                        .designation("Senior Physics Faculty")
                        .department("Physical Sciences")
                        .subjects(List.of("IB Physics HL", "Astronomy Lab"))
                        .qualifications("M.Sc. Physics, Oxford University")
                        .assignedClasses(List.of("Grade 10-A", "Grade 11-A", "Grade 12-A"))
                        .status("ACTIVE")
                        .email("m.brody@apexhigh.edu")
                        .phone("+1 (555) 382-9013")
                        .hireDate(LocalDate.of(2019, 8, 1))
                        .build(),
                TeacherRecord.builder()
                        .id("tch-4")
                        .tenantId(tenantId)
                        .employeeId("FAC-104")
                        .name("Dr. Elena Rostova")
                        .designation("Director of Financial Operations & Economics")
                        .department("Finance & Economics")
                        .subjects(List.of("Microeconomics AP", "Corporate Finance"))
                        .qualifications("CPA, MBA Wharton")
                        .assignedClasses(List.of("Grade 12-A"))
                        .status("ACTIVE")
                        .email("e.rostova@apexhigh.edu")
                        .phone("+1 (555) 382-9014")
                        .hireDate(LocalDate.of(2021, 6, 1))
                        .build()
        );

        teacherStore.put(tenantId, new ArrayList<>(teachers));
    }

    private String resolveTenant(String tenantId) {
        if (tenantId != null && !tenantId.isBlank()) return tenantId;
        String ctxTenant = TenantContext.getTenantId();
        return (ctxTenant != null && !ctxTenant.isBlank()) ? ctxTenant : "apex-high";
    }

    public List<TeacherRecord> getTeachers(String tenantId) {
        String tid = resolveTenant(tenantId);
        return teacherStore.getOrDefault(tid, Collections.emptyList());
    }

    public Optional<TeacherRecord> getTeacherById(String tenantId, String id) {
        return getTeachers(tenantId).stream()
                .filter(t -> t.getId().equalsIgnoreCase(id) || t.getEmployeeId().equalsIgnoreCase(id))
                .findFirst();
    }

    public TeacherRecord createTeacher(String tenantId, CreateTeacherRequest request) {
        String tid = resolveTenant(tenantId);
        String generatedId = "tch-" + UUID.randomUUID().toString().substring(0, 6);
        String empId = "FAC-" + String.format("%03d", (int)(Math.random() * 900) + 100);

        TeacherRecord teacher = TeacherRecord.builder()
                .id(generatedId)
                .tenantId(tid)
                .employeeId(empId)
                .name(request.getName())
                .designation(request.getDesignation())
                .department(request.getDepartment())
                .subjects(request.getSubjects() != null ? new ArrayList<>(request.getSubjects()) : new ArrayList<>())
                .qualifications(request.getQualifications())
                .assignedClasses(request.getAssignedClasses() != null ? new ArrayList<>(request.getAssignedClasses()) : new ArrayList<>())
                .status("ACTIVE")
                .email(request.getEmail())
                .phone(request.getPhone())
                .hireDate(LocalDate.now())
                .build();

        teacherStore.computeIfAbsent(tid, k -> new ArrayList<>()).add(teacher);
        return teacher;
    }
}
