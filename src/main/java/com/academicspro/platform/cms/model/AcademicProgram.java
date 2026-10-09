package com.academicspro.platform.cms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicProgram {
    private String id;
    private String name;
    private String code;
    private String level;
    private String ageGroup;
    private String description;
    private List<String> highlights;
    private String accreditation;
    private String coordinatorName;
    private String badgeColor;
}
