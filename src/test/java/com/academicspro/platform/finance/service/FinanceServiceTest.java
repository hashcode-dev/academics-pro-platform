package com.academicspro.platform.finance.service;

import com.academicspro.platform.finance.dto.FinancialSummaryDto;
import com.academicspro.platform.finance.dto.GenerateInvoiceRequest;
import com.academicspro.platform.finance.dto.RecordPaymentRequest;
import com.academicspro.platform.finance.model.PaymentReceipt;
import com.academicspro.platform.finance.model.PayrollEntry;
import com.academicspro.platform.finance.model.StudentInvoice;
import com.academicspro.platform.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FinanceServiceTest {

    private FinanceService financeService;

    @BeforeEach
    void setUp() {
        financeService = new FinanceService();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetSummary() {
        FinancialSummaryDto summary = financeService.getSummary("apex-high");
        assertNotNull(summary);
        assertEquals(4500000.0, summary.getTotalFeeTarget());
        assertEquals(3890250.0, summary.getCollectedFees());
        assertEquals(609750.0, summary.getPendingFees());
        assertEquals(86.4, summary.getCollectionRatePercentage());
        assertEquals(285400.0, summary.getMonthlyPayrollDisbursed());
        assertEquals(14, summary.getDefaultersCount());
    }

    @Test
    void testGetInvoices() {
        List<StudentInvoice> invoices = financeService.getInvoices("apex-high");
        assertEquals(5, invoices.size());
        assertEquals("INV-2026-0819", invoices.get(0).getInvoiceNumber());
        assertEquals("PAID", invoices.get(0).getStatus());
    }

    @Test
    void testGenerateInvoice() {
        GenerateInvoiceRequest req = GenerateInvoiceRequest.builder()
                .studentId("APX-2026-999")
                .studentName("Eleanor Vance")
                .grade("Grade 11-A")
                .amountDue(4800.0)
                .dueDate(LocalDate.of(2026, 11, 15))
                .build();

        StudentInvoice inv = financeService.generateInvoice("apex-high", req);
        assertNotNull(inv);
        assertEquals(4800.0, inv.getAmountDue());
        assertEquals("PENDING", inv.getStatus());

        List<StudentInvoice> list = financeService.getInvoices("apex-high");
        assertEquals(6, list.size());
    }

    @Test
    void testRecordPartialPayment() {
        // Liam O'Connor's invoice has amountDue 4500, amountPaid 0
        RecordPaymentRequest req = RecordPaymentRequest.builder()
                .invoiceId("inv-3")
                .studentName("Liam O'Connor")
                .amount(2000.0)
                .paymentMethod("ONLINE")
                .transactionReference("STRIPE-CH_8829")
                .build();

        PaymentReceipt receipt = financeService.recordPayment("apex-high", req, "treasurer@apexhigh.edu");
        assertNotNull(receipt);
        assertTrue(receipt.getReceiptNumber().startsWith("REC-APX-"));

        StudentInvoice updated = financeService.getInvoices("apex-high").stream()
                .filter(i -> i.getId().equals("inv-3"))
                .findFirst()
                .orElseThrow();

        assertEquals(2000.0, updated.getAmountPaid());
        assertEquals(2500.0, updated.getBalance());
        assertEquals("PARTIALLY_PAID", updated.getStatus());
    }

    @Test
    void testRecordFullPayment() {
        // Mateo Alvarez's invoice has amountDue 4500, amountPaid 1500, balance 3000
        RecordPaymentRequest req = RecordPaymentRequest.builder()
                .invoiceId("inv-5")
                .studentName("Mateo Alvarez")
                .amount(3000.0)
                .paymentMethod("BANK_TRANSFER")
                .build();

        financeService.recordPayment("apex-high", req, "treasurer@apexhigh.edu");

        StudentInvoice updated = financeService.getInvoices("apex-high").stream()
                .filter(i -> i.getId().equals("inv-5"))
                .findFirst()
                .orElseThrow();

        assertEquals(4500.0, updated.getAmountPaid());
        assertEquals(0.0, updated.getBalance());
        assertEquals("PAID", updated.getStatus());
    }

    @Test
    void testGetPayrollEntries() {
        List<PayrollEntry> entries = financeService.getPayrollEntries("apex-high");
        assertEquals(2, entries.size());
        assertEquals("October", entries.get(0).getMonth());
        assertEquals(285400.0, entries.get(0).getTotalDisbursed());
    }
}
