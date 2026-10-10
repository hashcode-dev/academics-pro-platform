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
public class PayrollEntry {
    private String id;
    private String tenantId;
    private String month;
    private int year;
    private double totalDisbursed;
    private int employeeCount;
    private LocalDate disbursedDate;
    private String status;
}
