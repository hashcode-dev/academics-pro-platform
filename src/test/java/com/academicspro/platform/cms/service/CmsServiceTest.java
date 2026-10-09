package com.academicspro.platform.cms.service;

import com.academicspro.platform.cms.dto.CmsPublishResponse;
import com.academicspro.platform.cms.dto.PublishContentRequest;
import com.academicspro.platform.cms.model.*;
import com.academicspro.platform.tenancy.TenantContext;
import com.academicspro.platform.tenancy.TenantContextData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CmsServiceTest {

    private CmsService cmsService;

    @BeforeEach
    void setUp() {
        cmsService = new CmsService();
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void testGetWebsiteFeedBySlugApexHigh() {
        Optional<TenantWebsiteFeed> feed = cmsService.getWebsiteFeedBySlug("apex-high");
        assertTrue(feed.isPresent());
        assertEquals("Apex High School", feed.get().getInstitutionName());
        assertNotNull(feed.get().getHero());
        assertEquals("Empowering Tomorrow's Pioneers, Thinkers & Leaders", feed.get().getHero().getHeadline());
        assertEquals(3, feed.get().getPrograms().size());
        assertEquals(3, feed.get().getTestimonials().size());
        assertEquals(3, feed.get().getFacilities().size());
        assertEquals(4, feed.get().getAdmissionSteps().size());
        assertEquals(2, feed.get().getEvents().size());
        assertEquals(1, feed.get().getNews().size());
    }

    @Test
    void testGetWebsiteFeedBySlugNotFound() {
        Optional<TenantWebsiteFeed> feed = cmsService.getWebsiteFeedBySlug("non-existent");
        assertFalse(feed.isPresent());

        Optional<TenantWebsiteFeed> nullFeed = cmsService.getWebsiteFeedBySlug(null);
        assertFalse(nullFeed.isPresent());

        Optional<TenantWebsiteFeed> emptyFeed = cmsService.getWebsiteFeedBySlug("");
        assertFalse(emptyFeed.isPresent());
    }

    @Test
    void testGetWebsiteFeedByTenantIdFallback() {
        TenantWebsiteFeed feed = cmsService.getWebsiteFeedByTenantId("unknown-tenant");
        assertNotNull(feed);
        assertEquals("Apex High School", feed.getInstitutionName());
    }

    @Test
    void testGetWebsiteFeedByTenantIdOakridge() {
        TenantWebsiteFeed feed = cmsService.getWebsiteFeedByTenantId("oakridge-academy");
        assertNotNull(feed);
        assertEquals("Oakridge International Academy", feed.getInstitutionName());
    }

    @Test
    void testPublishFeedAllSections() {
        HeroSection newHero = HeroSection.builder()
                .headline("Redefining Future Education")
                .subhead("New vision statement")
                .primaryCtaText("Apply Now")
                .primaryCtaLink("/apply")
                .build();

        AcademicProgram prog = AcademicProgram.builder()
                .id("prog-stem")
                .name("Advanced Robotics & AI")
                .build();

        Testimonial test = Testimonial.builder()
                .id("tst-new")
                .authorName("Alex Mercer")
                .build();

        CampusFacility fac = CampusFacility.builder()
                .id("fac-new")
                .name("Planetarium")
                .build();

        AdmissionStep step = AdmissionStep.builder()
                .stepNumber(1)
                .title("Submit Info")
                .build();

        SchoolEvent event = SchoolEvent.builder()
                .id("evt-hack")
                .title("Annual Hackathon")
                .eventDate(LocalDate.of(2026, 12, 1))
                .build();

        SchoolNews news = SchoolNews.builder()
                .id("news-new")
                .title("New Science Wing Opens")
                .build();

        PublishContentRequest request = PublishContentRequest.builder()
                .hero(newHero)
                .programs(List.of(prog))
                .testimonials(List.of(test))
                .facilities(List.of(fac))
                .admissionSteps(List.of(step))
                .events(List.of(event))
                .news(List.of(news))
                .build();

        CmsPublishResponse response = cmsService.publishFeed("apex-high", request, "admin-user");
        assertNotNull(response);
        assertEquals("PUBLISHED", response.getStatus());
        assertEquals("apex-high", response.getTenantId());
        assertEquals(2, response.getVersion());

        TenantWebsiteFeed updated = cmsService.getWebsiteFeedBySlug("apex-high").orElseThrow();
        assertEquals("Redefining Future Education", updated.getHero().getHeadline());
        assertEquals(1, updated.getPrograms().size());
        assertEquals("Advanced Robotics & AI", updated.getPrograms().get(0).getName());
        assertEquals(1, updated.getTestimonials().size());
        assertEquals(1, updated.getFacilities().size());
        assertEquals(1, updated.getAdmissionSteps().size());
        assertEquals(1, updated.getEvents().size());
        assertEquals(1, updated.getNews().size());
        assertEquals("admin-user", updated.getPublishedBy());
    }

    @Test
    void testGetPrograms() {
        List<AcademicProgram> progs = cmsService.getPrograms("apex-high");
        assertNotNull(progs);
        assertEquals(3, progs.size());
    }

    @Test
    void testGetTestimonials() {
        List<Testimonial> testimonials = cmsService.getTestimonials("apex-high");
        assertNotNull(testimonials);
        assertEquals(3, testimonials.size());
    }

    @Test
    void testGetEvents() {
        List<SchoolEvent> events = cmsService.getEvents("apex-high");
        assertNotNull(events);
        assertEquals(2, events.size());
    }

    @Test
    void testAddEventAutoId() {
        SchoolEvent newEvent = SchoolEvent.builder()
                .title("Spring Arts Gala")
                .category("Arts Gala")
                .eventDate(LocalDate.of(2027, 4, 15))
                .time("06:00 PM")
                .location("Symphony Auditorium")
                .description("Annual orchestral performance")
                .build();

        SchoolEvent added = cmsService.addEvent("apex-high", newEvent);
        assertNotNull(added.getId());
        assertTrue(added.getId().startsWith("evt-"));

        List<SchoolEvent> events = cmsService.getEvents("apex-high");
        assertEquals(3, events.size());
    }

    @Test
    void testUpdateHero() {
        HeroSection updatedHero = HeroSection.builder()
                .headline("Global Excellence in Cambridge")
                .subhead("New Subtitle")
                .build();

        HeroSection result = cmsService.updateHero("apex-high", updatedHero, "editor-1");
        assertNotNull(result);
        assertEquals("Global Excellence in Cambridge", result.getHeadline());

        TenantWebsiteFeed feed = cmsService.getWebsiteFeedBySlug("apex-high").orElseThrow();
        assertEquals("Global Excellence in Cambridge", feed.getHero().getHeadline());
        assertEquals("editor-1", feed.getPublishedBy());
    }
}
