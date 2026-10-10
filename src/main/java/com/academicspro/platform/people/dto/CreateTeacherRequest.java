package com.academicspro.platform.people.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTeacherRequest {
    @NotBlank(message = "Teacher name is required")
    private String name;

    @NotBlank(message = "Designation is required")
    private String designation;

    @NotBlank(message = "Department is required")
    private String department;

    private List<String> subjects;
    private String qualifications;
    private List<String> assignedClasses;
    private String email;
    private String phone;
}
