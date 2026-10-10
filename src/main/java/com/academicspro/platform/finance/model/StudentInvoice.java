package com.academicspro.platform.finance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentInvoice {
    private String id;
    private String tenantId;
    private String invoiceNumber;
    private String studentId;
    private String studentName;
    private String grade;
    private double amountDue;
    private double amountPaid;
    private double balance;
    private LocalDate dueDate;
    private String status;
    private LocalDate issueDate;
}
