package com.academicspro.platform.finance.service;

import com.academicspro.platform.finance.dto.FinancialSummaryDto;
import com.academicspro.platform.finance.dto.GenerateInvoiceRequest;
import com.academicspro.platform.finance.dto.RecordPaymentRequest;
import com.academicspro.platform.finance.model.PaymentReceipt;
import com.academicspro.platform.finance.model.PayrollEntry;
import com.academicspro.platform.finance.model.StudentInvoice;
import com.academicspro.platform.tenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FinanceService {

    private final Map<String, List<StudentInvoice>> invoiceStore = new ConcurrentHashMap<>();
    private final Map<String, List<PaymentReceipt>> receiptStore = new ConcurrentHashMap<>();
    private final Map<String, List<PayrollEntry>> payrollStore = new ConcurrentHashMap<>();

    public FinanceService() {
        seedApexHighFinance();
    }

    private void seedApexHighFinance() {
        String tenantId = "apex-high";

        List<StudentInvoice> invoices = List.of(
                StudentInvoice.builder().id("inv-1").tenantId(tenantId).invoiceNumber("INV-2026-0819").studentId("APX-2024-001").studentName("Julian Voss").grade("Grade 11-A").amountDue(4500.0).amountPaid(4500.0).balance(0.0).dueDate(LocalDate.of(2026, 9, 15)).status("PAID").issueDate(LocalDate.of(2026, 8, 25)).build(),
                StudentInvoice.builder().id("inv-2").tenantId(tenantId).invoiceNumber("INV-2026-0820").studentId("APX-2024-002").studentName("Sophia Chen").grade("Grade 11-A").amountDue(4500.0).amountPaid(4500.0).balance(0.0).dueDate(LocalDate.of(2026, 9, 15)).status("PAID").issueDate(LocalDate.of(2026, 8, 25)).build(),
                StudentInvoice.builder().id("inv-3").tenantId(tenantId).invoiceNumber("INV-2026-0821").studentId("APX-2024-003").studentName("Liam O'Connor").grade("Grade 10-B").amountDue(4500.0).amountPaid(0.0).balance(4500.0).dueDate(LocalDate.of(2026, 10, 15)).status("PENDING").issueDate(LocalDate.of(2026, 8, 25)).build(),
                StudentInvoice.builder().id("inv-4").tenantId(tenantId).invoiceNumber("INV-2026-0822").studentId("APX-2024-004").studentName("Amara Patel").grade("Grade 12-A").amountDue(4500.0).amountPaid(4500.0).balance(0.0).dueDate(LocalDate.of(2026, 9, 15)).status("PAID").issueDate(LocalDate.of(2026, 8, 25)).build(),
                StudentInvoice.builder().id("inv-5").tenantId(tenantId).invoiceNumber("INV-2026-0823").studentId("APX-2024-005").studentName("Mateo Alvarez").grade("Grade 9-C").amountDue(4500.0).amountPaid(1500.0).balance(3000.0).dueDate(LocalDate.of(2026, 9, 1)).status("OVERDUE").issueDate(LocalDate.of(2026, 8, 25)).build()
        );

        invoiceStore.put(tenantId, new ArrayList<>(invoices));

        List<PayrollEntry> payroll = List.of(
                PayrollEntry.builder().id("pay-2026-10").tenantId(tenantId).month("October").year(2026).totalDisbursed(285400.0).employeeCount(98).disbursedDate(LocalDate.of(2026, 10, 1)).status("DISBURSED").build(),
                PayrollEntry.builder().id("pay-2026-09").tenantId(tenantId).month("September").year(2026).totalDisbursed(284100.0).employeeCount(97).disbursedDate(LocalDate.of(2026, 9, 1)).status("DISBURSED").build()
        );

        payrollStore.put(tenantId, new ArrayList<>(payroll));
    }

    private String resolveTenant(String tenantId) {
        if (tenantId != null && !tenantId.isBlank()) return tenantId;
        String ctxTenant = TenantContext.getTenantId();
        return (ctxTenant != null && !ctxTenant.isBlank()) ? ctxTenant : "apex-high";
    }

    public FinancialSummaryDto getSummary(String tenantId) {
        String tid = resolveTenant(tenantId);
        return FinancialSummaryDto.builder()
                .tenantId(tid)
                .totalFeeTarget(4500000.0)
                .collectedFees(3890250.0)
                .pendingFees(609750.0)
                .collectionRatePercentage(86.4)
                .monthlyPayrollDisbursed(285400.0)
                .defaultersCount(14)
                .build();
    }

    public List<StudentInvoice> getInvoices(String tenantId) {
        String tid = resolveTenant(tenantId);
        return invoiceStore.getOrDefault(tid, Collections.emptyList());
    }

    public StudentInvoice generateInvoice(String tenantId, GenerateInvoiceRequest request) {
        String tid = resolveTenant(tenantId);
        String invId = "inv-" + UUID.randomUUID().toString().substring(0, 6);
        String invNumber = "INV-2026-" + String.format("%04d", (int)(Math.random() * 9000) + 1000);

        StudentInvoice invoice = StudentInvoice.builder()
                .id(invId)
                .tenantId(tid)
                .invoiceNumber(invNumber)
                .studentId(request.getStudentId())
                .studentName(request.getStudentName())
                .grade(request.getGrade())
                .amountDue(request.getAmountDue())
                .amountPaid(0.0)
                .balance(request.getAmountDue())
                .dueDate(request.getDueDate() != null ? request.getDueDate() : LocalDate.now().plusDays(30))
                .status("PENDING")
                .issueDate(LocalDate.now())
                .build();

        invoiceStore.computeIfAbsent(tid, k -> new ArrayList<>()).add(invoice);
        return invoice;
    }

    public PaymentReceipt recordPayment(String tenantId, RecordPaymentRequest request, String collectedBy) {
        String tid = resolveTenant(tenantId);
        String recId = "rec-" + UUID.randomUUID().toString().substring(0, 6);
        String recNumber = "REC-APX-" + String.format("%04d", (int)(Math.random() * 9000) + 1000);

        PaymentReceipt receipt = PaymentReceipt.builder()
                .id(recId)
                .tenantId(tid)
                .receiptNumber(recNumber)
                .invoiceId(request.getInvoiceId())
                .studentName(request.getStudentName())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .paymentDate(Instant.now())
                .transactionReference(request.getTransactionReference() != null ? request.getTransactionReference() : "TXN-" + System.currentTimeMillis())
                .collectedBy(collectedBy != null ? collectedBy : "finance-officer")
                .build();

        // Update target invoice balance if present
        List<StudentInvoice> list = invoiceStore.getOrDefault(tid, Collections.emptyList());
        for (StudentInvoice inv : list) {
            if (inv.getId().equalsIgnoreCase(request.getInvoiceId()) || inv.getInvoiceNumber().equalsIgnoreCase(request.getInvoiceId())) {
                double newPaid = inv.getAmountPaid() + request.getAmount();
                inv.setAmountPaid(newPaid);
                double newBal = Math.max(0.0, inv.getAmountDue() - newPaid);
                inv.setBalance(newBal);
                if (newBal <= 0) {
                    inv.setStatus("PAID");
                } else {
                    inv.setStatus("PARTIALLY_PAID");
                }
                break;
            }
        }

        receiptStore.computeIfAbsent(tid, k -> new ArrayList<>()).add(receipt);
        return receipt;
    }

    public List<PayrollEntry> getPayrollEntries(String tenantId) {
        String tid = resolveTenant(tenantId);
        return payrollStore.getOrDefault(tid, Collections.emptyList());
    }
}
