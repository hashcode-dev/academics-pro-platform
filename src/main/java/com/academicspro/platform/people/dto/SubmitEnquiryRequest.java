package com.academicspro.platform.people.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubmitEnquiryRequest {
    @NotBlank(message = "Parent name is required")
    private String parentName;

    @Email(message = "Valid email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Grade applying for is required")
    private String gradeApplyingFor;

    private String enquiryType;
    private String message;
    private boolean gdprConsent;
}
