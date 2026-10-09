package com.academicspro.platform.academics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicYear {
    private String id;
    private String tenantId;
    private String name; // e.g. "2026–2027"
    private String code;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status; // CURRENT, UPCOMING, ARCHIVED
    @Builder.Default
    private List<AcademicTerm> terms = new ArrayList<>();
}
