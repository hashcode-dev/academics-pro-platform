package com.academicspro.platform.academics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    private String id;
    private String tenantId;
    private String code;
    private String name;
    private String curriculum; // "IB PYP", "Cambridge", "AP", "IB DP"
    private String department;
    private int credits;
    private int weeklyHours;
}
