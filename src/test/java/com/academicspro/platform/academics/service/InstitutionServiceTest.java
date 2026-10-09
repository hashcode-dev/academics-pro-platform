package com.academicspro.platform.academics.service;

import com.academicspro.platform.academics.dto.CreateCampusRequest;
import com.academicspro.platform.academics.dto.InstitutionOverviewDto;
import com.academicspro.platform.academics.model.*;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InstitutionServiceTest {

    private InstitutionService institutionService;

    @BeforeEach
    void setUp() {
        institutionService = new InstitutionService();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetCampusesApexHigh() {
        List<Campus> campuses = institutionService.getCampuses("apex-high");
        assertNotNull(campuses);
        assertEquals(2, campuses.size());
        assertEquals("Main Cambridge Campus", campuses.get(0).getName());
        assertEquals("CAM-MAIN", campuses.get(0).getCode());
    }

    @Test
    void testGetCampusesOakridge() {
        List<Campus> campuses = institutionService.getCampuses("oakridge-academy");
        assertNotNull(campuses);
        assertEquals(1, campuses.size());
        assertEquals("Oakridge International Campus", campuses.get(0).getName());
    }

    @Test
    void testGetCampusesFallbackToTenantContext() {
        TenantContext.set(TenantContextData.builder().tenantId("oakridge-academy").build());
        List<Campus> campuses = institutionService.getCampuses(null);
        assertEquals(1, campuses.size());
        assertEquals("Oakridge International Campus", campuses.get(0).getName());
    }

    @Test
    void testGetCampusesDefaultFallback() {
        TenantContext.clear();
        List<Campus> campuses = institutionService.getCampuses("");
        assertEquals(2, campuses.size());
    }

    @Test
    void testCreateCampus() {
        CreateCampusRequest request = CreateCampusRequest.builder()
                .name("Apex Science & Tech Pavilion")
                .code("APEX-TECH")
                .address("750 Technology Square, Cambridge, MA")
                .contactPhone("+1 555-019-2831")
                .contactEmail("tech@apexhigh.edu")
                .studentCapacity(300)
                .gradesOffered("9-12")
                .build();

        Campus created = institutionService.createCampus("apex-high", request);
        assertNotNull(created);
        assertNotNull(created.getId());
        assertEquals("Apex Science & Tech Pavilion", created.getName());
        assertTrue(created.isActive());

        List<Campus> updated = institutionService.getCampuses("apex-high");
        assertEquals(3, updated.size());
    }

    @Test
    void testGetAcademicYears() {
        List<AcademicYear> years = institutionService.getAcademicYears("apex-high");
        assertNotNull(years);
        assertEquals(1, years.size());
        assertEquals("Academic Year 2026–2027", years.get(0).getName());
        assertEquals("CURRENT", years.get(0).getStatus());
        assertEquals(2, years.get(0).getTerms().size());
    }

    @Test
    void testGetCurrentAcademicYearFound() {
        Optional<AcademicYear> currentYear = institutionService.getCurrentAcademicYear("apex-high");
        assertTrue(currentYear.isPresent());
        assertEquals("Academic Year 2026–2027", currentYear.get().getName());
    }

    @Test
    void testGetCurrentAcademicYearNotFound() {
        Optional<AcademicYear> currentYear = institutionService.getCurrentAcademicYear("non-existent-tenant");
        assertFalse(currentYear.isPresent());
    }

    @Test
    void testGetClassGrades() {
        List<ClassGrade> classes = institutionService.getClassGrades("apex-high");
        assertNotNull(classes);
        assertEquals(3, classes.size());
        assertEquals("Grade 10", classes.get(0).getName());
        assertEquals(2, classes.get(0).getSections().size());
    }

    @Test
    void testGetSubjects() {
        List<Subject> subjects = institutionService.getSubjects("apex-high");
        assertNotNull(subjects);
        assertEquals(5, subjects.size());
        assertTrue(subjects.stream().anyMatch(s -> "MATH-BC".equals(s.getCode())));
    }

    @Test
    void testGetClassrooms() {
        List<Classroom> classrooms = institutionService.getClassrooms("apex-high");
        assertNotNull(classrooms);
        assertEquals(4, classrooms.size());
        assertTrue(classrooms.stream().anyMatch(c -> "Lab 301".equals(c.getRoomNumber())));
    }

    @Test
    void testGetOverviewApexHigh() {
        InstitutionOverviewDto overview = institutionService.getOverview("apex-high");
        assertNotNull(overview);
        assertEquals("apex-high", overview.getTenantId());
        assertEquals(2, overview.getTotalCampuses());
        assertEquals(1750, overview.getTotalStudentCapacity()); // 1400 + 350
        assertEquals("Academic Year 2026–2027", overview.getCurrentAcademicYear());
        assertEquals("Fall Term 1", overview.getCurrentTerm());
        assertEquals(3, overview.getTotalClasses());
        assertEquals(5, overview.getTotalSections());
        assertEquals(5, overview.getTotalSubjects());
        assertEquals(4, overview.getTotalClassrooms());
    }

    @Test
    void testGetOverviewOakridge() {
        InstitutionOverviewDto overview = institutionService.getOverview("oakridge-academy");
        assertNotNull(overview);
        assertEquals("oakridge-academy", overview.getTenantId());
        assertEquals(1, overview.getTotalCampuses());
        assertEquals(1000, overview.getTotalStudentCapacity());
        assertEquals("2026–2027", overview.getCurrentAcademicYear()); // fallback
    }
}
