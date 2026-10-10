package com.academicspro.platform.finance.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.finance.dto.FinancialSummaryDto;
import com.academicspro.platform.finance.dto.GenerateInvoiceRequest;
import com.academicspro.platform.finance.dto.RecordPaymentRequest;
import com.academicspro.platform.finance.model.PaymentReceipt;
import com.academicspro.platform.finance.model.PayrollEntry;
import com.academicspro.platform.finance.model.StudentInvoice;
import com.academicspro.platform.finance.service.FinanceService;
import com.academicspro.platform.security.RequireRole;
import com.academicspro.platform.security.Role;
import com.academicspro.platform.tenancy.TenantContext;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance")
@RequiredArgsConstructor
public class FinanceController {

    private final FinanceService financeService;

    @GetMapping("/summary")
    public ResponseEntity<ApiResponse<FinancialSummaryDto>> getSummary(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(financeService.getSummary(effectiveTenant)));
    }

    @GetMapping("/invoices")
    public ResponseEntity<ApiResponse<List<StudentInvoice>>> getInvoices(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(financeService.getInvoices(effectiveTenant)));
    }

    @PostMapping("/invoices")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN, Role.FINANCE_OFFICER})
    public ResponseEntity<ApiResponse<StudentInvoice>> generateInvoice(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @Valid @RequestBody GenerateInvoiceRequest request) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        StudentInvoice invoice = financeService.generateInvoice(effectiveTenant, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(invoice));
    }

    @PostMapping("/payments")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN, Role.FINANCE_OFFICER})
    public ResponseEntity<ApiResponse<PaymentReceipt>> recordPayment(
            @RequestParam(value = "tenantId", required = false) String tenantId,
            @Valid @RequestBody RecordPaymentRequest request) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        String user = TenantContext.getUserId();
        PaymentReceipt receipt = financeService.recordPayment(effectiveTenant, request, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(receipt));
    }

    @GetMapping("/payroll")
    @RequireRole({Role.SUPER_ADMIN, Role.SCHOOL_ADMIN, Role.FINANCE_OFFICER})
    public ResponseEntity<ApiResponse<List<PayrollEntry>>> getPayrollEntries(
            @RequestParam(value = "tenantId", required = false) String tenantId) {
        String effectiveTenant = (tenantId != null && !tenantId.isBlank()) ? tenantId : TenantContext.getTenantId();
        return ResponseEntity.ok(ApiResponse.ok(financeService.getPayrollEntries(effectiveTenant)));
    }
}
