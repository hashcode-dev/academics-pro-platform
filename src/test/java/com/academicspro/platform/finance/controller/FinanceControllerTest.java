package com.academicspro.platform.finance.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.finance.dto.FinancialSummaryDto;
import com.academicspro.platform.finance.dto.GenerateInvoiceRequest;
import com.academicspro.platform.finance.dto.RecordPaymentRequest;
import com.academicspro.platform.finance.model.PaymentReceipt;
import com.academicspro.platform.finance.model.PayrollEntry;
import com.academicspro.platform.finance.model.StudentInvoice;
import com.academicspro.platform.finance.service.FinanceService;
import com.academicspro.platform.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinanceControllerTest {

    private FinanceController financeController;

    @BeforeEach
    void setUp() {
        FinanceService fs = new FinanceService();
        financeController = new FinanceController(fs);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetSummary() {
        ResponseEntity<ApiResponse<FinancialSummaryDto>> response = financeController.getSummary("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4500000.0, response.getBody().getData().getTotalFeeTarget());
    }

    @Test
    void testGetInvoices() {
        ResponseEntity<ApiResponse<List<StudentInvoice>>> response = financeController.getInvoices("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(5, response.getBody().getData().size());
    }

    @Test
    void testGenerateInvoice() {
        GenerateInvoiceRequest req = GenerateInvoiceRequest.builder()
                .studentId("APX-STU-100")
                .studentName("Student")
                .grade("Grade 10")
                .amountDue(4000.0)
                .build();

        ResponseEntity<ApiResponse<StudentInvoice>> response = financeController.generateInvoice("apex-high", req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getData().getId());
    }

    @Test
    void testRecordPayment() {
        RecordPaymentRequest req = RecordPaymentRequest.builder()
                .invoiceId("inv-3")
                .studentName("Liam O'Connor")
                .amount(1000.0)
                .paymentMethod("ONLINE")
                .build();

        ResponseEntity<ApiResponse<PaymentReceipt>> response = financeController.recordPayment("apex-high", req);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getData().getReceiptNumber());
    }

    @Test
    void testGetPayroll() {
        ResponseEntity<ApiResponse<List<PayrollEntry>>> response = financeController.getPayrollEntries("apex-high");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getData().size());
    }
}
