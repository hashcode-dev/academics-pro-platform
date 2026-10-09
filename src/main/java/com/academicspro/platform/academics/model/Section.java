package com.academicspro.platform.academics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    private String id;
    private String classGradeId;
    private String name; // e.g. "A", "B", "C"
    private String roomNumber;
    private int maxCapacity;
    private int currentEnrollment;
    private String classTeacherName;
}
