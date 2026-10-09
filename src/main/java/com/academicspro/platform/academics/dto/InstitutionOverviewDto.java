package com.academicspro.platform.academics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionOverviewDto {
    private String tenantId;
    private int totalCampuses;
    private int totalStudentCapacity;
    private String currentAcademicYear;
    private String currentTerm;
    private int totalClasses;
    private int totalSections;
    private int totalSubjects;
    private int totalClassrooms;
}
