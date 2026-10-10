package com.academicspro.platform.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableSlot {
    private String id;
    private String tenantId;
    private String day;
    private int period;
    private String time;
    private String subject;
    private String subjectCode;
    private String teacherId;
    private String teacherName;
    private String roomNumber;
    private String section;
    private boolean hasConflict;
    private String conflictReason;
}
