package com.academicspro.platform.schedule.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.schedule.dto.CreateSlotRequest;
import com.academicspro.platform.schedule.dto.TimetableMatrixDto;
import com.academicspro.platform.schedule.model.TimetableSlot;
import com.academicspro.platform.schedule.service.TimetableService;
import com.academicspro.platform.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TimetableControllerTest {

    private TimetableController timetableController;

    @BeforeEach
    void setUp() {
        TimetableService ts = new TimetableService();
        timetableController = new TimetableController(ts);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetMatrix() {
        ResponseEntity<ApiResponse<TimetableMatrixDto>> response = timetableController.getMatrix("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getData().isConflictFree());
    }

    @Test
    void testGetSlots() {
        ResponseEntity<ApiResponse<List<TimetableSlot>>> response = timetableController.getSlots("apex-high", "11-A", null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(6, response.getBody().getData().size());
    }

    @Test
    void testAddSlot() {
        CreateSlotRequest req = CreateSlotRequest.builder()
                .day("Tuesday")
                .period(2)
                .time("09:30 - 10:25")
                .subject("Biology HL")
                .teacherName("Dr. Vance")
                .roomNumber("Lab 201")
                .section("11-A")
                .build();

        ResponseEntity<ApiResponse<TimetableSlot>> response = timetableController.addSlot("apex-high", req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getData().getId());
    }
}
