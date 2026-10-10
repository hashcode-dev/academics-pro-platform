package com.academicspro.platform.people.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdmissionsEnquiry {
    private String id;
    private String tenantId;
    private String enquiryNumber;
    private String parentName;
    private String email;
    private String phone;
    private String studentName;
    private String gradeApplyingFor;
    private String enquiryType;
    private String message;
    private String status;
    private Instant submittedAt;
}
