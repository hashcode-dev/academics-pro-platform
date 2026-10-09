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
public class AdmissionStep {
    private int stepNumber;
    private String title;
    private String deadline;
    private String description;
    private List<String> requirements;
}
