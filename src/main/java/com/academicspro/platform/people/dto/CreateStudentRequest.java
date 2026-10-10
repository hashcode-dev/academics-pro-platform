package com.academicspro.platform.people.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {
    @NotBlank(message = "Student name is required")
    private String name;

    @NotBlank(message = "Grade is required")
    private String grade;

    @NotBlank(message = "Section is required")
    private String section;

    private String rollNumber;
    private String guardianName;
    private String guardianPhone;
    private String guardianEmail;
    private double gpa;
}
