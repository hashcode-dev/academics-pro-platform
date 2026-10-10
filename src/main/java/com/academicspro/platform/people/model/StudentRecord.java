package com.academicspro.platform.people.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRecord {
    private String id;
    private String tenantId;
    private String studentId;
    private String name;
    private String grade;
    private String section;
    private String rollNumber;
    private double attendanceRate;
    private double gpa;
    private String status;
    private GuardianContact guardian;
    private String feeStatus;
    private LocalDate enrollmentDate;
    private String advisorName;
}
