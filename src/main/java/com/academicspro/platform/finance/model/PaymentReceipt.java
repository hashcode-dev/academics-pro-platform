package com.academicspro.platform.finance.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentReceipt {
    private String id;
    private String tenantId;
    private String receiptNumber;
    private String invoiceId;
    private String studentName;
    private double amount;
    private String paymentMethod;
    private Instant paymentDate;
    private String transactionReference;
    private String collectedBy;
}
