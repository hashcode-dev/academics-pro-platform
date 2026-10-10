package com.academicspro.platform.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenerateInvoiceRequest {
    @NotBlank(message = "Student ID is required")
    private String studentId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Grade is required")
    private String grade;

    @Positive(message = "Amount due must be positive")
    private double amountDue;

    private LocalDate dueDate;
}
