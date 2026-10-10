package com.academicspro.platform.schedule.service;

import com.academicspro.platform.schedule.dto.CreateSlotRequest;
import com.academicspro.platform.schedule.dto.TimetableMatrixDto;
import com.academicspro.platform.schedule.model.ScheduleConflict;
import com.academicspro.platform.schedule.model.TimetableSlot;
import com.academicspro.platform.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TimetableServiceTest {

    private TimetableService timetableService;

    @BeforeEach
    void setUp() {
        timetableService = new TimetableService();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetInitialMatrix() {
        TimetableMatrixDto matrix = timetableService.getMatrix("apex-high");
        assertNotNull(matrix);
        assertEquals(6, matrix.getTotalSlots());
        assertEquals(0, matrix.getTotalConflicts());
        assertTrue(matrix.isConflictFree());
    }

    @Test
    void testGetSlotsWithFilters() {
        List<TimetableSlot> slotsSection = timetableService.getSlots("apex-high", "11-A", null);
        assertEquals(6, slotsSection.size());

        List<TimetableSlot> slotsTeacher = timetableService.getSlots("apex-high", null, "Sarah Lin");
        assertEquals(1, slotsTeacher.size());
        assertEquals("Mathematics (Calculus)", slotsTeacher.get(0).getSubject());

        List<TimetableSlot> slotsNone = timetableService.getSlots("apex-high", "12-Z", null);
        assertEquals(0, slotsNone.size());
    }

    @Test
    void testAddNonConflictingSlot() {
        CreateSlotRequest req = CreateSlotRequest.builder()
                .day("Tuesday")
                .period(1)
                .time("08:30 - 09:25")
                .subject("Physics HL")
                .subjectCode("IB-PHYS-HL")
                .teacherName("Marcus Brody")
                .roomNumber("Physics Lab 1")
                .section("11-A")
                .build();

        TimetableSlot slot = timetableService.addSlot("apex-high", req);
        assertNotNull(slot);
        assertFalse(slot.isHasConflict());

        TimetableMatrixDto matrix = timetableService.getMatrix("apex-high");
        assertEquals(7, matrix.getTotalSlots());
        assertTrue(matrix.isConflictFree());
    }

    @Test
    void testTeacherConflictDetection() {
        // Teacher Sarah Lin is already on Monday Period 1 (for 11-A in Lab 301)
        CreateSlotRequest conflictingTeacher = CreateSlotRequest.builder()
                .day("Monday")
                .period(1)
                .time("08:30 - 09:25")
                .subject("Calculus BC")
                .subjectCode("MATH-BC")
                .teacherName("Sarah Lin")
                .roomNumber("Room 401")
                .section("12-A")
                .build();

        TimetableSlot slot = timetableService.addSlot("apex-high", conflictingTeacher);
        assertTrue(slot.isHasConflict());
        assertTrue(slot.getConflictReason().contains("Sarah Lin is already assigned"));

        TimetableMatrixDto matrix = timetableService.getMatrix("apex-high");
        assertFalse(matrix.isConflictFree());
        assertEquals(1, matrix.getTotalConflicts());
    }

    @Test
    void testRoomConflictDetection() {
        // Room Lab 301 is already occupied on Monday Period 1
        CreateSlotRequest conflictingRoom = CreateSlotRequest.builder()
                .day("Monday")
                .period(1)
                .time("08:30 - 09:25")
                .subject("Statistics")
                .subjectCode("MATH-STAT")
                .teacherName("Dr. Alistair Cook")
                .roomNumber("Lab 301")
                .section("12-B")
                .build();

        TimetableSlot slot = timetableService.addSlot("apex-high", conflictingRoom);
        assertTrue(slot.isHasConflict());
        assertTrue(slot.getConflictReason().contains("Room Lab 301 is already occupied"));
    }

    @Test
    void testSectionConflictDetection() {
        // Section 11-A already has class on Monday Period 1
        CreateSlotRequest conflictingSection = CreateSlotRequest.builder()
                .day("Monday")
                .period(1)
                .time("08:30 - 09:25")
                .subject("Art & Design")
                .subjectCode("ART-01")
                .teacherName("Rachel Hayes")
                .roomNumber("Room 105")
                .section("11-A")
                .build();

        TimetableSlot slot = timetableService.addSlot("apex-high", conflictingSection);
        assertTrue(slot.isHasConflict());
        assertTrue(slot.getConflictReason().contains("Section 11-A already has Mathematics (Calculus) scheduled"));
    }
}
