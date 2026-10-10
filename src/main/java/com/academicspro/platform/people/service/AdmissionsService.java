package com.academicspro.platform.people.service;

import com.academicspro.platform.people.dto.EnquiryResponse;
import com.academicspro.platform.people.dto.SubmitEnquiryRequest;
import com.academicspro.platform.people.model.AdmissionsEnquiry;
import com.academicspro.platform.tenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AdmissionsService {

    private final Map<String, List<AdmissionsEnquiry>> enquiryStore = new ConcurrentHashMap<>();

    private String resolveTenant(String tenantId) {
        if (tenantId != null && !tenantId.isBlank()) return tenantId;
        String ctxTenant = TenantContext.getTenantId();
        return (ctxTenant != null && !ctxTenant.isBlank()) ? ctxTenant : "apex-high";
    }

    public EnquiryResponse submitEnquiry(String tenantId, SubmitEnquiryRequest request) {
        String tid = resolveTenant(tenantId);
        int refNum = (int)(Math.random() * 9000) + 1000;
        String enquiryNumber = "ENQ-APX-" + refNum;

        AdmissionsEnquiry enquiry = AdmissionsEnquiry.builder()
                .id("enq-" + UUID.randomUUID().toString().substring(0, 8))
                .tenantId(tid)
                .enquiryNumber(enquiryNumber)
                .parentName(request.getParentName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .studentName(request.getStudentName())
                .gradeApplyingFor(request.getGradeApplyingFor())
                .enquiryType(request.getEnquiryType() != null ? request.getEnquiryType() : "general")
                .message(request.getMessage())
                .status("RECEIVED")
                .submittedAt(Instant.now())
                .build();

        enquiryStore.computeIfAbsent(tid, k -> new ArrayList<>()).add(enquiry);

        return EnquiryResponse.builder()
                .confirmationNumber(enquiryNumber)
                .status("SUCCESS")
                .message("Admissions enquiry received. Our Dean of Admissions will contact you within 24 hours.")
                .submittedAt(enquiry.getSubmittedAt())
                .build();
    }

    public List<AdmissionsEnquiry> getEnquiries(String tenantId) {
        String tid = resolveTenant(tenantId);
        return enquiryStore.getOrDefault(tid, Collections.emptyList());
    }
}
