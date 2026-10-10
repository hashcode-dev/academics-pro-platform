package com.academicspro.platform.people.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.people.dto.*;
import com.academicspro.platform.people.model.*;
import com.academicspro.platform.people.service.*;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PeopleControllerTest {

    private PeopleController peopleController;

    @BeforeEach
    void setUp() {
        StudentService ss = new StudentService();
        TeacherService ts = new TeacherService();
        AdmissionsService as = new AdmissionsService();
        peopleController = new PeopleController(ss, ts, as);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetStudents() {
        ResponseEntity<ApiResponse<List<StudentRecord>>> response = peopleController.getStudents("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(5, response.getBody().getData().size());
    }

    @Test
    void testGetStudentByIdFound() {
        ResponseEntity<ApiResponse<StudentRecord>> response = peopleController.getStudentById("stu-1", "apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Julian Voss", response.getBody().getData().getName());
    }

    @Test
    void testGetStudentByIdNotFound() {
        ResponseEntity<ApiResponse<StudentRecord>> response = peopleController.getStudentById("stu-missing", "apex-high");
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    void testCreateStudent() {
        CreateStudentRequest req = CreateStudentRequest.builder()
                .name("New Student")
                .grade("Grade 11")
                .section("A")
                .guardianName("Guardian")
                .build();

        ResponseEntity<ApiResponse<StudentRecord>> response = peopleController.createStudent("apex-high", req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Student", response.getBody().getData().getName());
    }

    @Test
    void testGetTeachers() {
        ResponseEntity<ApiResponse<List<TeacherRecord>>> response = peopleController.getTeachers("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().getData().size());
    }

    @Test
    void testGetTeacherByIdFound() {
        ResponseEntity<ApiResponse<TeacherRecord>> response = peopleController.getTeacherById("tch-1", "apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Dr. Arthur Vance", response.getBody().getData().getName());
    }

    @Test
    void testCreateTeacher() {
        CreateTeacherRequest req = CreateTeacherRequest.builder()
                .name("New Faculty")
                .designation("Lecturer")
                .department("Science")
                .build();

        ResponseEntity<ApiResponse<TeacherRecord>> response = peopleController.createTeacher("apex-high", req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("New Faculty", response.getBody().getData().getName());
    }

    @Test
    void testSubmitAdmissionsEnquiry() {
        SubmitEnquiryRequest req = SubmitEnquiryRequest.builder()
                .parentName("John Doe")
                .email("john@doe.com")
                .phone("+1 555-1234")
                .studentName("Johnny")
                .gradeApplyingFor("Grade 10")
                .build();

        ResponseEntity<ApiResponse<EnquiryResponse>> response = peopleController.submitAdmissionsEnquiry("apex-high", req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getData().getConfirmationNumber());
    }

    @Test
    void testGetEnquiries() {
        ResponseEntity<ApiResponse<List<AdmissionsEnquiry>>> response = peopleController.getEnquiries("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
