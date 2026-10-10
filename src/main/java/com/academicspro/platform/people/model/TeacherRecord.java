package com.academicspro.platform.people.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeacherRecord {
    private String id;
    private String tenantId;
    private String employeeId;
    private String name;
    private String designation;
    private String department;
    @Builder.Default
    private List<String> subjects = new ArrayList<>();
    private String qualifications;
    @Builder.Default
    private List<String> assignedClasses = new ArrayList<>();
    private String status;
    private String email;
    private String phone;
    private LocalDate hireDate;
}
