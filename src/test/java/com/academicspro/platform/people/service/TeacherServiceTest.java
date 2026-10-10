package com.academicspro.platform.people.service;

import com.academicspro.platform.people.dto.CreateTeacherRequest;
import com.academicspro.platform.people.model.TeacherRecord;
import com.academicspro.platform.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TeacherServiceTest {

    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetTeachersApexHigh() {
        List<TeacherRecord> teachers = teacherService.getTeachers("apex-high");
        assertNotNull(teachers);
        assertEquals(4, teachers.size());
        assertEquals("Dr. Arthur Vance", teachers.get(0).getName());
        assertEquals("Sarah Lin", teachers.get(1).getName());
    }

    @Test
    void testGetTeacherByIdFound() {
        Optional<TeacherRecord> teacher = teacherService.getTeacherById("apex-high", "tch-2");
        assertTrue(teacher.isPresent());
        assertEquals("Sarah Lin", teacher.get().getName());
        assertEquals("Mathematics", teacher.get().getDepartment());
    }

    @Test
    void testGetTeacherByIdNotFound() {
        Optional<TeacherRecord> teacher = teacherService.getTeacherById("apex-high", "tch-unknown");
        assertFalse(teacher.isPresent());
    }

    @Test
    void testCreateTeacher() {
        CreateTeacherRequest request = CreateTeacherRequest.builder()
                .name("Rachel Hayes")
                .designation("Senior Literature Specialist")
                .department("Humanities")
                .subjects(List.of("English Literature AP", "Comparative Poetry"))
                .qualifications("M.A. English, Columbia University")
                .assignedClasses(List.of("Grade 11-A"))
                .email("r.hayes@apexhigh.edu")
                .phone("+1 (555) 382-9019")
                .build();

        TeacherRecord created = teacherService.createTeacher("apex-high", request);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Rachel Hayes", created.getName());
        assertEquals("ACTIVE", created.getStatus());

        List<TeacherRecord> list = teacherService.getTeachers("apex-high");
        assertEquals(5, list.size());
    }
}
