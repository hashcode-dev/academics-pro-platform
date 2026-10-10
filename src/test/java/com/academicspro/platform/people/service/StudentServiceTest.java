package com.academicspro.platform.people.service;

import com.academicspro.platform.people.dto.CreateStudentRequest;
import com.academicspro.platform.people.model.StudentRecord;
import com.academicspro.platform.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class StudentServiceTest {

    private StudentService studentService;

    @BeforeEach
    void setUp() {
        studentService = new StudentService();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetStudentsApexHigh() {
        List<StudentRecord> students = studentService.getStudents("apex-high");
        assertNotNull(students);
        assertEquals(5, students.size());
        assertEquals("Julian Voss", students.get(0).getName());
        assertEquals("Grade 11", students.get(0).getGrade());
        assertEquals("PAID", students.get(0).getFeeStatus());
    }

    @Test
    void testGetStudentByIdFound() {
        Optional<StudentRecord> student = studentService.getStudentById("apex-high", "stu-1");
        assertTrue(student.isPresent());
        assertEquals("Julian Voss", student.get().getName());
        assertEquals("APX-2024-001", student.get().getStudentId());
    }

    @Test
    void testGetStudentByIdNotFound() {
        Optional<StudentRecord> student = studentService.getStudentById("apex-high", "stu-999");
        assertFalse(student.isPresent());
    }

    @Test
    void testCreateStudent() {
        CreateStudentRequest request = CreateStudentRequest.builder()
                .name("Kiran Sharma")
                .grade("Grade 10")
                .section("A")
                .rollNumber("10-A-15")
                .guardianName("Sunil Sharma")
                .guardianPhone("+1 (555) 749-2211")
                .guardianEmail("s.sharma@tech.com")
                .gpa(3.88)
                .build();

        StudentRecord created = studentService.createStudent("apex-high", request);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Kiran Sharma", created.getName());
        assertEquals("ACTIVE", created.getStatus());

        List<StudentRecord> updated = studentService.getStudents("apex-high");
        assertEquals(6, updated.size());
    }
}
