package com.academicspro.platform.academics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicTerm {
    private String id;
    private String termName;
    private int termNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate examStartDate;
    private LocalDate examEndDate;
    private String status; // CURRENT, UPCOMING, COMPLETED
}
