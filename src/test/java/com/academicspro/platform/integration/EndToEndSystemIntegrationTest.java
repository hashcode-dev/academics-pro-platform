package com.academicspro.platform.integration;

import com.academicspro.platform.academics.model.Campus;
import com.academicspro.platform.academics.service.InstitutionService;
import com.academicspro.platform.cms.dto.CmsPublishResponse;
import com.academicspro.platform.cms.dto.PublishContentRequest;
import com.academicspro.platform.cms.model.HeroSection;
import com.academicspro.platform.cms.model.TenantWebsiteFeed;
import com.academicspro.platform.cms.service.CmsService;
import com.academicspro.platform.finance.dto.GenerateInvoiceRequest;
import com.academicspro.platform.finance.dto.RecordPaymentRequest;
import com.academicspro.platform.finance.model.PaymentReceipt;
import com.academicspro.platform.finance.model.StudentInvoice;
import com.academicspro.platform.finance.service.FinanceService;
import com.academicspro.platform.people.dto.CreateStudentRequest;
import com.academicspro.platform.people.dto.EnquiryResponse;
import com.academicspro.platform.people.dto.SubmitEnquiryRequest;
import com.academicspro.platform.people.model.StudentRecord;
import com.academicspro.platform.people.service.AdmissionsService;
import com.academicspro.platform.people.service.StudentService;
import com.academicspro.platform.schedule.dto.CreateSlotRequest;
import com.academicspro.platform.schedule.dto.TimetableMatrixDto;
import com.academicspro.platform.schedule.model.TimetableSlot;
import com.academicspro.platform.schedule.service.TimetableService;
import com.academicspro.platform.security.JwtUtil;
import com.academicspro.platform.security.Role;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("End-to-End System Packaging & Cross-Domain Lifecycle Integration Tests")
class EndToEndSystemIntegrationTest {

    private InstitutionService institutionService;
    private CmsService cmsService;
    private StudentService studentService;
    private AdmissionsService admissionsService;
    private TimetableService timetableService;
    private FinanceService financeService;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        TenantContext.set(TenantContextData.builder().tenantId("apex-high").institutionId("inst-apex").build());
        institutionService = new InstitutionService();
        cmsService = new CmsService();
        studentService = new StudentService();
        admissionsService = new AdmissionsService();
        timetableService = new TimetableService();
        financeService = new FinanceService();
        jwtUtil = new JwtUtil("my-test-secret-key-1234567890-must-be-long-enough", 3600000);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    @DisplayName("E2E Security & Identity: JWT issuance, claims extraction, and role resolution")
    void testSecurityAndIdentityLifecycle() {
        String token = jwtUtil.generateToken(
                "usr-001",
                "admin@apexhigh.edu",
                "Dr. Arthur Vance",
                "apex-high",
                "inst-apex",
                Set.of(Role.SCHOOL_ADMIN)
        );
        assertNotNull(token);

        TenantContextData extracted = jwtUtil.validateAndExtract(token);
        assertNotNull(extracted);
        assertEquals("admin@apexhigh.edu", extracted.getUserEmail());
        assertEquals("apex-high", extracted.getTenantId());
        assertTrue(extracted.hasRole(Role.SCHOOL_ADMIN));
    }

    @Test
    @DisplayName("E2E Multi-Tenancy Isolation: Scope integrity between distinct tenants")
    void testMultiTenancyIsolation() {
        List<Campus> apexCampuses = institutionService.getCampuses("apex-high");
        assertFalse(apexCampuses.isEmpty());
        assertEquals("Main Cambridge Campus", apexCampuses.get(0).getName());

        List<Campus> oakridgeCampuses = institutionService.getCampuses("oakridge-academy");
        assertFalse(oakridgeCampuses.isEmpty());
        assertEquals("Oakridge International Campus", oakridgeCampuses.get(0).getName());

        // Context clearing
        TenantContext.clear();
        assertNull(TenantContext.getTenantId());
    }

    @Test
    @DisplayName("E2E Student Admission to Enrollment Lifecycle: Inquiry -> Enrollment -> Roster")
    void testStudentLifecycleFlow() {
        // 1. Submit Admissions Inquiry
        SubmitEnquiryRequest enquiryReq = SubmitEnquiryRequest.builder()
                .parentName("Katherine Hayes")
                .email("katherine@hayes.org")
                .phone("+1 555 492 8820")
                .studentName("Alexander Hayes")
                .gradeApplyingFor("Grade 11")
                .enquiryType("admissions_tour")
                .message("Inquiring about IB DP Capstone enrollment.")
                .gdprConsent(true)
                .build();

        EnquiryResponse submittedEnquiry = admissionsService.submitEnquiry("apex-high", enquiryReq);
        assertNotNull(submittedEnquiry.getConfirmationNumber());
        assertEquals("SUCCESS", submittedEnquiry.getStatus());

        // 2. Formal Enrollment into Student Database
        CreateStudentRequest studentReq = CreateStudentRequest.builder()
                .name("Alexander Hayes")
                .grade("Grade 11")
                .section("A")
                .rollNumber("11-A-09")
                .guardianName("Katherine Hayes")
                .guardianPhone("+1 555 492 8820")
                .guardianEmail("katherine@hayes.org")
                .gpa(3.95)
                .build();

        StudentRecord enrolled = studentService.createStudent("apex-high", studentReq);
        assertNotNull(enrolled.getId());
        assertEquals("ACTIVE", enrolled.getStatus());

        // 3. Verify in Active Roster
        Optional<StudentRecord> retrieved = studentService.getStudentById("apex-high", enrolled.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Alexander Hayes", retrieved.get().getName());
    }

    @Test
    @DisplayName("E2E Timetable Conflict Prevention: Double-booking validation engine")
    void testTimetableEngineIntegrity() {
        // 1. Existing schedule matrix should be conflict-free
        TimetableMatrixDto matrix = timetableService.getMatrix("apex-high");
        assertTrue(matrix.isConflictFree());
        assertEquals(0, matrix.getTotalConflicts());

        // 2. Schedule non-conflicting period
        CreateSlotRequest validSlot = CreateSlotRequest.builder()
                .day("Wednesday")
                .period(3)
                .time("10:40 - 11:35")
                .subject("Advanced Robotics")
                .subjectCode("STEM-ROB-01")
                .teacherName("Kenji Sato")
                .roomNumber("Turing Lab")
                .section("11-A")
                .build();

        TimetableSlot scheduled = timetableService.addSlot("apex-high", validSlot);
        assertNotNull(scheduled.getId());
        assertFalse(scheduled.isHasConflict());

        // 3. Attempting double-booking must flag conflict
        CreateSlotRequest conflictSlot = CreateSlotRequest.builder()
                .day("Monday")
                .period(1)
                .time("08:30 - 09:25")
                .subject("Linear Algebra")
                .subjectCode("MATH-LA")
                .teacherName("Sarah Lin") // Teacher Sarah Lin is already assigned on Monday period 1
                .roomNumber("Room 401")
                .section("12-A")
                .build();

        TimetableSlot conflicted = timetableService.addSlot("apex-high", conflictSlot);
        assertTrue(conflicted.isHasConflict());
        assertNotNull(conflicted.getConflictReason());
    }

    @Test
    @DisplayName("E2E Tuition Billing & Payment Ledger: Invoicing -> Payment -> Balance Reconciliation")
    void testFinancialReconciliationLifecycle() {
        // 1. Generate Invoice
        GenerateInvoiceRequest invReq = GenerateInvoiceRequest.builder()
                .studentId("APX-2024-001")
                .studentName("Julian Voss")
                .grade("Grade 11-A")
                .amountDue(8500.0)
                .dueDate(LocalDate.now().plusDays(30))
                .build();

        StudentInvoice created = financeService.generateInvoice("apex-high", invReq);
        assertEquals("PENDING", created.getStatus());
        assertEquals(8500.0, created.getAmountDue());

        // 2. Record Partial Payment Receipt
        RecordPaymentRequest paymentReq = RecordPaymentRequest.builder()
                .invoiceId(created.getId())
                .studentName("Julian Voss")
                .amount(5000.0)
                .paymentMethod("ONLINE_BANKING")
                .transactionReference("TXN-PAY-849201")
                .build();

        PaymentReceipt receipt = financeService.recordPayment("apex-high", paymentReq, "bursar@apexhigh.edu");
        assertNotNull(receipt.getId());
        assertNotNull(receipt.getReceiptNumber());

        // 3. Verify Invoice Balance is Reconciled
        StudentInvoice updated = financeService.getInvoices("apex-high").stream()
                .filter(i -> i.getId().equals(created.getId()))
                .findFirst()
                .orElseThrow();

        assertEquals("PARTIALLY_PAID", updated.getStatus());
        assertEquals(5000.0, updated.getAmountPaid());
        assertEquals(3500.0, updated.getBalance());
    }

    @Test
    @DisplayName("E2E Content CMS Studio: Feed compilation, versioning and atomic publication")
    void testContentCmsLifecycle() {
        Optional<TenantWebsiteFeed> feedOpt = cmsService.getWebsiteFeedBySlug("apex-high");
        assertTrue(feedOpt.isPresent());
        TenantWebsiteFeed feed = feedOpt.get();
        assertEquals("Apex High School", feed.getInstitutionName());
        int currentVersion = feed.getVersion();

        // Publish live update with new Hero
        HeroSection newHero = HeroSection.builder()
                .headline("Inspiring Academic Rigor & Ethical Leadership")
                .subhead("Premier Pre-K through Grade 12 educational journey in Cambridge.")
                .primaryCtaText("Explore Campus")
                .primaryCtaLink("/school#explore")
                .build();

        PublishContentRequest publishRequest = PublishContentRequest.builder()
                .hero(newHero)
                .programs(feed.getPrograms())
                .testimonials(feed.getTestimonials())
                .facilities(feed.getFacilities())
                .admissionSteps(feed.getAdmissionSteps())
                .events(feed.getEvents())
                .news(feed.getNews())
                .build();

        CmsPublishResponse response = cmsService.publishFeed("apex-high", publishRequest, "director@apexhigh.edu");
        assertNotNull(response);
        assertEquals("PUBLISHED", response.getStatus());
        assertEquals(currentVersion + 1, response.getVersion());

        // Verify updated feed
        TenantWebsiteFeed updatedFeed = cmsService.getWebsiteFeedBySlug("apex-high").orElseThrow();
        assertEquals(currentVersion + 1, updatedFeed.getVersion());
        assertEquals("Inspiring Academic Rigor & Ethical Leadership", updatedFeed.getHero().getHeadline());
    }
}
