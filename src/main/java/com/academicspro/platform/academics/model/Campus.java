package com.academicspro.platform.academics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Campus {
    private String id;
    private String tenantId;
    private String institutionId;
    private String name;
    private String code;
    private String address;
    private String contactPhone;
    private String contactEmail;
    private int studentCapacity;
    private String gradesOffered;
    @Builder.Default
    private boolean active = true;
}
