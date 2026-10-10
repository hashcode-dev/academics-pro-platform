package com.academicspro.platform.finance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordPaymentRequest {
    @NotBlank(message = "Invoice ID is required")
    private String invoiceId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @Positive(message = "Payment amount must be greater than zero")
    private double amount;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    private String transactionReference;
}
