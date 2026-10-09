package com.academicspro.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TenantInfoDto {
    private String tenantId;
    private String institutionId;
    private String name;
    private String tagline;
    private String slug;
    private String logoUrl;
    private String primaryColor;
    private String contactEmail;
    private String phone;
    private String address;
    private int establishedYear;
    private int studentCount;
    private int facultyCount;
    private double passRatePercentage;
    private List<String> accreditations;
    private List<String> curricula;
}
