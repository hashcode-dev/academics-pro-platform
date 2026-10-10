package com.academicspro.platform.schedule.service;

import com.academicspro.platform.schedule.dto.CreateSlotRequest;
import com.academicspro.platform.schedule.dto.TimetableMatrixDto;
import com.academicspro.platform.schedule.model.ScheduleConflict;
import com.academicspro.platform.schedule.model.TimetableSlot;
import com.academicspro.platform.tenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TimetableService {

    private final Map<String, List<TimetableSlot>> slotStore = new ConcurrentHashMap<>();

    public TimetableService() {
        seedApexHighSlots();
    }

    private void seedApexHighSlots() {
        String tenantId = "apex-high";

        List<TimetableSlot> slots = List.of(
                TimetableSlot.builder().id("slot-1").tenantId(tenantId).day("Monday").period(1).time("08:30 - 09:25").subject("Mathematics (Calculus)").subjectCode("MATH-BC").teacherId("tch-2").teacherName("Sarah Lin").roomNumber("Lab 301").section("11-A").hasConflict(false).build(),
                TimetableSlot.builder().id("slot-2").tenantId(tenantId).day("Monday").period(2).time("09:30 - 10:25").subject("Physics HL").subjectCode("IB-PHYS-HL").teacherId("tch-3").teacherName("Marcus Brody").roomNumber("Physics Lab 1").section("11-A").hasConflict(false).build(),
                TimetableSlot.builder().id("slot-3").tenantId(tenantId).day("Monday").period(3).time("10:40 - 11:35").subject("Literature & Composition").subjectCode("ENG-LIT-AP").teacherId("tch-5").teacherName("Rachel Hayes").roomNumber("Hall B").section("11-A").hasConflict(false).build(),
                TimetableSlot.builder().id("slot-4").tenantId(tenantId).day("Monday").period(4).time("11:40 - 12:35").subject("Computer Science & AI").subjectCode("STEM-AI").teacherId("tch-6").teacherName("Kenji Sato").roomNumber("Turing Lab").section("11-A").hasConflict(false).build(),
                TimetableSlot.builder().id("slot-5").tenantId(tenantId).day("Monday").period(5).time("01:25 - 02:20").subject("Chemistry HL").subjectCode("IB-CHEM-HL").teacherId("tch-7").teacherName("Dr. Alistair Cook").roomNumber("Chemistry Lab 2").section("11-A").hasConflict(false).build(),
                TimetableSlot.builder().id("slot-6").tenantId(tenantId).day("Monday").period(6).time("02:25 - 03:20").subject("World History AP").subjectCode("HIST-AP").teacherId("tch-1").teacherName("Dr. Arthur Vance").roomNumber("Seminar Room 4").section("11-A").hasConflict(false).build()
        );

        slotStore.put(tenantId, new ArrayList<>(slots));
    }

    private String resolveTenant(String tenantId) {
        if (tenantId != null && !tenantId.isBlank()) return tenantId;
        String ctxTenant = TenantContext.getTenantId();
        return (ctxTenant != null && !ctxTenant.isBlank()) ? ctxTenant : "apex-high";
    }

    public List<TimetableSlot> getSlots(String tenantId, String section, String teacherName) {
        String tid = resolveTenant(tenantId);
        List<TimetableSlot> list = slotStore.getOrDefault(tid, Collections.emptyList());

        return list.stream()
                .filter(s -> section == null || section.isBlank() || s.getSection().equalsIgnoreCase(section))
                .filter(s -> teacherName == null || teacherName.isBlank() || s.getTeacherName().equalsIgnoreCase(teacherName))
                .toList();
    }

    public Optional<ScheduleConflict> checkConflict(String tenantId, TimetableSlot newSlot) {
        String tid = resolveTenant(tenantId);
        List<TimetableSlot> existing = slotStore.getOrDefault(tid, Collections.emptyList());

        for (TimetableSlot s : existing) {
            if (!s.getId().equals(newSlot.getId()) &&
                s.getDay().equalsIgnoreCase(newSlot.getDay()) &&
                s.getPeriod() == newSlot.getPeriod()) {

                if (s.getTeacherName().equalsIgnoreCase(newSlot.getTeacherName())) {
                    return Optional.of(ScheduleConflict.builder()
                            .type("TEACHER_DOUBLE_BOOKED")
                            .message("Teacher " + s.getTeacherName() + " is already assigned to section " + s.getSection() + " at Period " + s.getPeriod() + " on " + s.getDay())
                            .day(s.getDay())
                            .period(s.getPeriod())
                            .conflictingSlotIds(List.of(s.getId(), newSlot.getId()))
                            .build());
                }

                if (s.getRoomNumber().equalsIgnoreCase(newSlot.getRoomNumber())) {
                    return Optional.of(ScheduleConflict.builder()
                            .type("ROOM_DOUBLE_BOOKED")
                            .message("Room " + s.getRoomNumber() + " is already occupied by section " + s.getSection() + " at Period " + s.getPeriod() + " on " + s.getDay())
                            .day(s.getDay())
                            .period(s.getPeriod())
                            .conflictingSlotIds(List.of(s.getId(), newSlot.getId()))
                            .build());
                }

                if (s.getSection().equalsIgnoreCase(newSlot.getSection())) {
                    return Optional.of(ScheduleConflict.builder()
                            .type("SECTION_DOUBLE_BOOKED")
                            .message("Section " + s.getSection() + " already has " + s.getSubject() + " scheduled at Period " + s.getPeriod() + " on " + s.getDay())
                            .day(s.getDay())
                            .period(s.getPeriod())
                            .conflictingSlotIds(List.of(s.getId(), newSlot.getId()))
                            .build());
                }
            }
        }
        return Optional.empty();
    }

    public TimetableSlot addSlot(String tenantId, CreateSlotRequest request) {
        String tid = resolveTenant(tenantId);
        String slotId = "slot-" + UUID.randomUUID().toString().substring(0, 6);

        TimetableSlot slot = TimetableSlot.builder()
                .id(slotId)
                .tenantId(tid)
                .day(request.getDay())
                .period(request.getPeriod())
                .time(request.getTime())
                .subject(request.getSubject())
                .subjectCode(request.getSubjectCode() != null ? request.getSubjectCode() : "SUBJ")
                .teacherId(request.getTeacherId() != null ? request.getTeacherId() : "tch-ext")
                .teacherName(request.getTeacherName())
                .roomNumber(request.getRoomNumber())
                .section(request.getSection())
                .hasConflict(false)
                .build();

        Optional<ScheduleConflict> conflict = checkConflict(tid, slot);
        if (conflict.isPresent()) {
            slot.setHasConflict(true);
            slot.setConflictReason(conflict.get().getMessage());
        }

        slotStore.computeIfAbsent(tid, k -> new ArrayList<>()).add(slot);
        return slot;
    }

    public TimetableMatrixDto getMatrix(String tenantId) {
        String tid = resolveTenant(tenantId);
        List<TimetableSlot> slots = slotStore.getOrDefault(tid, Collections.emptyList());
        long conflictCount = slots.stream().filter(TimetableSlot::isHasConflict).count();

        return TimetableMatrixDto.builder()
                .tenantId(tid)
                .totalSlots(slots.size())
                .totalConflicts((int) conflictCount)
                .conflictFree(conflictCount == 0)
                .slots(slots)
                .build();
    }
}
