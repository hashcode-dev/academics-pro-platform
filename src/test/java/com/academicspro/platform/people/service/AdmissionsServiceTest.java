package com.academicspro.platform.people.service;

import com.academicspro.platform.people.dto.EnquiryResponse;
import com.academicspro.platform.people.dto.SubmitEnquiryRequest;
import com.academicspro.platform.people.model.AdmissionsEnquiry;
import com.academicspro.platform.tenancy.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AdmissionsServiceTest {

    private AdmissionsService admissionsService;

    @BeforeEach
    void setUp() {
        admissionsService = new AdmissionsService();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testSubmitEnquiry() {
        SubmitEnquiryRequest request = SubmitEnquiryRequest.builder()
                .parentName("Claire Underwood")
                .email("c.underwood@dc.gov")
                .phone("+1 (555) 202-9988")
                .studentName("Frank Underwood Jr.")
                .gradeApplyingFor("Grade 9")
                .enquiryType("open_day")
                .message("Looking forward to the campus tour.")
                .gdprConsent(true)
                .build();

        EnquiryResponse response = admissionsService.submitEnquiry("apex-high", request);
        assertNotNull(response);
        assertEquals("SUCCESS", response.getStatus());
        assertTrue(response.getConfirmationNumber().startsWith("ENQ-APX-"));

        List<AdmissionsEnquiry> list = admissionsService.getEnquiries("apex-high");
        assertEquals(1, list.size());
        assertEquals("Claire Underwood", list.get(0).getParentName());
    }
}
