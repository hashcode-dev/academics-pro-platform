package com.academicspro.platform.finance.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSummaryDto {
    private String tenantId;
    private double totalFeeTarget;
    private double collectedFees;
    private double pendingFees;
    private double collectionRatePercentage;
    private double monthlyPayrollDisbursed;
    private int defaultersCount;
}
