package com.academicspro.platform.academics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassGrade {
    private String id;
    private String tenantId;
    private String name; // e.g. "Grade 10", "Grade 11"
    private String stage; // "Primary", "Middle", "Senior"
    private int displayOrder;
    @Builder.Default
    private List<Section> sections = new ArrayList<>();
}
