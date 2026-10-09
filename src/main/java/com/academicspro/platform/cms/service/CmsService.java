package com.academicspro.platform.cms.service;

import com.academicspro.platform.cms.dto.CmsPublishResponse;
import com.academicspro.platform.cms.dto.PublishContentRequest;
import com.academicspro.platform.cms.model.*;
import com.academicspro.platform.tenancy.TenantContext;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CmsService {

    private final Map<String, TenantWebsiteFeed> feedStore = new ConcurrentHashMap<>();

    public CmsService() {
        seedApexHighFeed();
        seedOakridgeFeed();
    }

    private void seedApexHighFeed() {
        String tenantId = "apex-high";

        HeroSection hero = HeroSection.builder()
                .headline("Empowering Tomorrow's Pioneers, Thinkers & Leaders")
                .subhead("An elite K-12 preparatory academy offering International Baccalaureate (IB) Diploma and AP Capstone pathways in Cambridge, MA.")
                .primaryCtaText("Book a Campus Tour")
                .primaryCtaLink("/school#admissions")
                .secondaryCtaText("Explore Academic Curricula")
                .secondaryCtaLink("/school#academics")
                .badgeText("Top 1% Global STEM & Humanities Ranking")
                .badgeHighlight("100% IB Diploma Pass Rate")
                .backgroundImageUrl("/images/school-hero.jpg")
                .statsHighlights(List.of(
                        "98.4% Placement to Top 30 Global Universities",
                        "1:8 Faculty-to-Student Mentorship Ratio",
                        "45+ Advanced Research Labs & Creative Studios"
                ))
                .build();

        List<AcademicProgram> programs = List.of(
                AcademicProgram.builder()
                        .id("prog-early")
                        .name("Early Learning & Discovery Atelier")
                        .code("PROG-EY")
                        .level("Early Childhood")
                        .ageGroup("Ages 3–5")
                        .description("Play-based inquiry grounded in Reggio Emilia and Montessori philosophies fostering curiosity and foundational literacy.")
                        .highlights(List.of("Bilingual Immersion", "Nature Exploratorium", "Hands-on Sensorimotor Workshops"))
                        .accreditation("NAEYC Certified")
                        .coordinatorName("Elena Rostova")
                        .badgeColor("#007f57")
                        .build(),
                AcademicProgram.builder()
                        .id("prog-my")
                        .name("Middle Years IB Exploration")
                        .code("PROG-MYP")
                        .level("Middle School")
                        .ageGroup("Grades 6–8")
                        .description("Rigorous interdisciplinary curriculum designed to cultivate global mindedness, critical inquiry, and analytical thought.")
                        .highlights(List.of("Design Thinking Labs", "Community Action Projects", "Model United Nations"))
                        .accreditation("IB World School MYP")
                        .coordinatorName("Dr. Marcus Brody")
                        .badgeColor("#004bca")
                        .build(),
                AcademicProgram.builder()
                        .id("prog-diploma")
                        .name("Senior IB Diploma & AP Capstone")
                        .code("PROG-IBDP")
                        .level("Senior Secondary")
                        .ageGroup("Grades 9–12")
                        .description("World-renowned pre-university standard emphasizing deep academic research, Theory of Knowledge (TOK), and extended essays.")
                        .highlights(List.of("100% Pass Rate", "Average Score 38.6 / 45", "Direct Admissions Mentorship"))
                        .accreditation("IB World School DP & AP College Board")
                        .coordinatorName("Dr. Arthur Vance")
                        .badgeColor("#5b21b6")
                        .build()
        );

        List<Testimonial> testimonials = List.of(
                Testimonial.builder()
                        .id("tst-1")
                        .authorName("Julian Voss")
                        .role("Alumnus, Class of 2024")
                        .cohortYear("2024")
                        .quote("Apex High's robotics atelier and faculty mentorship gave me the confidence to publish independent AI research before college.")
                        .universityBadge("MIT '28 - EECS")
                        .avatarUrl("/avatars/julian.jpg")
                        .rating(5.0)
                        .build(),
                Testimonial.builder()
                        .id("tst-2")
                        .authorName("Dr. Catherine Sterling")
                        .role("Parent of 11th Grader")
                        .cohortYear("2026")
                        .quote("The individual attention each scholar receives is unparalleled. The balance of academic rigor and compassionate care is extraordinary.")
                        .universityBadge("Parent Association Co-Chair")
                        .avatarUrl("/avatars/catherine.jpg")
                        .rating(5.0)
                        .build(),
                Testimonial.builder()
                        .id("tst-3")
                        .authorName("Maya Chen")
                        .role("Senior Scholar")
                        .cohortYear("2025")
                        .quote("Between competitive debate, chamber orchestra, and AP Calculus, every day challenges me to grow beyond my expectations.")
                        .universityBadge("Stanford '29 Admit")
                        .avatarUrl("/avatars/maya.jpg")
                        .rating(5.0)
                        .build()
        );

        List<CampusFacility> facilities = List.of(
                CampusFacility.builder()
                        .id("fac-stem")
                        .name("Turing Quantum & Robotics Atelier")
                        .category("STEM")
                        .description("State-of-the-art makerspace equipped with laser cutters, 6-axis robotic arms, 3D printers, and NVIDIA RTX clusters.")
                        .imageUrl("/facilities/turing-lab.jpg")
                        .features(List.of("Rapid Prototyping", "CAD Workstations", "Autonomous Drones Bay"))
                        .build(),
                CampusFacility.builder()
                        .id("fac-aquatics")
                        .name("Olympic Aquatics & Athletic Pavilion")
                        .category("Athletics")
                        .description("50-meter indoor heated competition pool, dual hardwood basketball courts, and sports science performance gym.")
                        .imageUrl("/facilities/aquatics.jpg")
                        .features(List.of("FINA Compliant", "Hydrotherapy Suite", "Biomechanical Tracking"))
                        .build(),
                CampusFacility.builder()
                        .id("fac-theater")
                        .name("Blackbox & Symphony Auditorium")
                        .category("Arts")
                        .description("550-seat acoustically engineered theater designed for musical theater, orchestra recitals, and public debates.")
                        .imageUrl("/facilities/theater.jpg")
                        .features(List.of("Steinway Grand Pianos", "DMX Lighting Grid", "Audio Recording Studio"))
                        .build()
        );

        List<AdmissionStep> admissionSteps = List.of(
                AdmissionStep.builder()
                        .stepNumber(1)
                        .title("Submit Online Application")
                        .deadline("Rolling until Dec 15")
                        .description("Complete parent questionnaire and upload previous two academic transcripts via our secure portal.")
                        .requirements(List.of("Transcripts", "Birth Certificate", "Teacher Recommendations"))
                        .build(),
                AdmissionStep.builder()
                        .stepNumber(2)
                        .title("Scholar Assessment & Inquiry Day")
                        .deadline("January 10–25")
                        .description("Prospective students participate in collaborative classroom sessions and cognitive problem-solving ateliers.")
                        .requirements(List.of("On-site Assessment", "Creative Essay", "Math Diagnostic"))
                        .build(),
                AdmissionStep.builder()
                        .stepNumber(3)
                        .title("Family Leadership Interview")
                        .deadline("February 1–15")
                        .description("Personal 30-minute conversation with our Academic Dean to align educational values and extracurricular passions.")
                        .requirements(List.of("Family Dialogue", "Student Passions Pitch"))
                        .build(),
                AdmissionStep.builder()
                        .stepNumber(4)
                        .title("Decision & Enrollment")
                        .deadline("March 10")
                        .description("Formal admission offers issued with merit scholarship notifications and comprehensive onboarding guide.")
                        .requirements(List.of("Deposit", "Health Forms", "Course Selection"))
                        .build()
        );

        List<SchoolEvent> events = new ArrayList<>(List.of(
                SchoolEvent.builder()
                        .id("evt-1")
                        .title("Fall Open Campus & Innovation Showcase")
                        .category("Open Day")
                        .eventDate(LocalDate.of(2026, 10, 24))
                        .time("09:00 AM – 01:00 PM EST")
                        .location("Main Cambridge Campus, Auditorium A")
                        .description("Tour campus facilities, attend live lab demos, and meet Academic Heads of Department.")
                        .rsvpUrl("/school/rsvp/open-day")
                        .featured(true)
                        .build(),
                SchoolEvent.builder()
                        .id("evt-2")
                        .title("New England Regional Robotics Invitational")
                        .category("Academic Olympiad")
                        .eventDate(LocalDate.of(2026, 11, 14))
                        .time("10:00 AM – 05:00 PM EST")
                        .location("Apex Innovation Annex")
                        .description("Hosting 28 regional schools competing in autonomous robotics challenges.")
                        .rsvpUrl("/school/rsvp/robotics")
                        .featured(false)
                        .build()
        ));

        List<SchoolNews> news = List.of(
                SchoolNews.builder()
                        .id("news-1")
                        .title("Apex High Students Win 1st Place at Harvard Model Congress")
                        .slug("apex-high-harvard-model-congress-win")
                        .publishedDate(LocalDate.of(2026, 9, 28))
                        .summary("Our debate delegation swept gavel awards across Senate, Supreme Court, and National Security Council committees.")
                        .body("Over the past weekend, fifteen Apex High delegates competed against 600 peers from across the country...")
                        .author("Communications Office")
                        .tags(List.of("Debate", "Excellence", "Student Life"))
                        .imageUrl("/news/hmc-win.jpg")
                        .build()
        );

        TenantWebsiteFeed feed = TenantWebsiteFeed.builder()
                .id("feed-" + tenantId)
                .tenantId(tenantId)
                .tenantSlug("apex-high")
                .institutionName("Apex High School")
                .tagline("Shaping World-Class Thinkers and Compassionate Global Leaders")
                .logoUrl("/logos/apex-high.svg")
                .primaryColor("#004bca")
                .contactEmail("admissions@apexhigh.edu")
                .contactPhone("+1 (555) 382-9011")
                .campusAddress("742 Evergreen Crest, Cambridge, MA 02138")
                .hero(hero)
                .programs(new ArrayList<>(programs))
                .testimonials(new ArrayList<>(testimonials))
                .facilities(new ArrayList<>(facilities))
                .admissionSteps(new ArrayList<>(admissionSteps))
                .events(events)
                .news(new ArrayList<>(news))
                .version(1)
                .publishedAt(Instant.now())
                .publishedBy("system-init")
                .build();

        feedStore.put(tenantId, feed);
        feedStore.put("apex-high", feed);
    }

    private void seedOakridgeFeed() {
        String tenantId = "oakridge-academy";

        HeroSection hero = HeroSection.builder()
                .headline("Inspiring Minds, Cultivating Character")
                .subhead("Premier International Baccalaureate continuum school nurturing creative confidence and ethical leadership.")
                .primaryCtaText("Schedule a Private Tour")
                .primaryCtaLink("/school#admissions")
                .secondaryCtaText("View IB Results")
                .secondaryCtaLink("/school#academics")
                .badgeText("Cognia & IB World School Accredited")
                .badgeHighlight("99.1% Graduation Rate")
                .backgroundImageUrl("/images/oakridge-hero.jpg")
                .statsHighlights(List.of(
                        "100% University Placement",
                        "1:9 Student-Faculty Ratio",
                        "Over 30 Extracurricular Clubs"
                ))
                .build();

        TenantWebsiteFeed feed = TenantWebsiteFeed.builder()
                .id("feed-" + tenantId)
                .tenantId(tenantId)
                .tenantSlug("oakridge-academy")
                .institutionName("Oakridge International Academy")
                .tagline("Excellence in Inquiry-Based Learning and Innovation")
                .logoUrl("/logos/oakridge.svg")
                .primaryColor("#007f57")
                .contactEmail("contact@oakridge.edu")
                .contactPhone("+1 (555) 749-1022")
                .campusAddress("120 Academy Way, Austin, TX 78701")
                .hero(hero)
                .programs(new ArrayList<>())
                .testimonials(new ArrayList<>())
                .facilities(new ArrayList<>())
                .admissionSteps(new ArrayList<>())
                .events(new ArrayList<>())
                .news(new ArrayList<>())
                .version(1)
                .publishedAt(Instant.now())
                .publishedBy("system-init")
                .build();

        feedStore.put(tenantId, feed);
    }

    private String resolveTenant(String tenantId) {
        if (tenantId != null && !tenantId.isBlank()) return tenantId;
        String ctxTenant = TenantContext.getTenantId();
        return (ctxTenant != null && !ctxTenant.isBlank()) ? ctxTenant : "apex-high";
    }

    public Optional<TenantWebsiteFeed> getWebsiteFeedBySlug(String slug) {
        if (slug == null || slug.isBlank()) return Optional.empty();
        return Optional.ofNullable(feedStore.get(slug.toLowerCase()));
    }

    public TenantWebsiteFeed getWebsiteFeedByTenantId(String tenantId) {
        String tid = resolveTenant(tenantId);
        TenantWebsiteFeed feed = feedStore.get(tid);
        if (feed == null) {
            feed = feedStore.get("apex-high");
        }
        return feed;
    }

    public CmsPublishResponse publishFeed(String tenantId, PublishContentRequest request, String publishedBy) {
        String tid = resolveTenant(tenantId);
        TenantWebsiteFeed existing = feedStore.get(tid);

        if (existing == null) {
            existing = feedStore.get("apex-high");
        }

        if (request.getHero() != null) {
            existing.setHero(request.getHero());
        }
        if (request.getPrograms() != null) {
            existing.setPrograms(new ArrayList<>(request.getPrograms()));
        }
        if (request.getTestimonials() != null) {
            existing.setTestimonials(new ArrayList<>(request.getTestimonials()));
        }
        if (request.getFacilities() != null) {
            existing.setFacilities(new ArrayList<>(request.getFacilities()));
        }
        if (request.getAdmissionSteps() != null) {
            existing.setAdmissionSteps(new ArrayList<>(request.getAdmissionSteps()));
        }
        if (request.getEvents() != null) {
            existing.setEvents(new ArrayList<>(request.getEvents()));
        }
        if (request.getNews() != null) {
            existing.setNews(new ArrayList<>(request.getNews()));
        }

        existing.setVersion(existing.getVersion() + 1);
        existing.setPublishedAt(Instant.now());
        existing.setPublishedBy(publishedBy != null ? publishedBy : "admin");

        feedStore.put(tid, existing);
        feedStore.put(existing.getTenantSlug(), existing);

        return CmsPublishResponse.builder()
                .tenantId(tid)
                .tenantSlug(existing.getTenantSlug())
                .version(existing.getVersion())
                .publishedAt(existing.getPublishedAt())
                .status("PUBLISHED")
                .message("Website feed successfully compiled and published for " + existing.getInstitutionName())
                .build();
    }

    public List<AcademicProgram> getPrograms(String tenantId) {
        TenantWebsiteFeed feed = getWebsiteFeedByTenantId(tenantId);
        return feed != null ? feed.getPrograms() : Collections.emptyList();
    }

    public List<Testimonial> getTestimonials(String tenantId) {
        TenantWebsiteFeed feed = getWebsiteFeedByTenantId(tenantId);
        return feed != null ? feed.getTestimonials() : Collections.emptyList();
    }

    public List<SchoolEvent> getEvents(String tenantId) {
        TenantWebsiteFeed feed = getWebsiteFeedByTenantId(tenantId);
        return feed != null ? feed.getEvents() : Collections.emptyList();
    }

    public SchoolEvent addEvent(String tenantId, SchoolEvent event) {
        String tid = resolveTenant(tenantId);
        TenantWebsiteFeed feed = getWebsiteFeedByTenantId(tid);
        if (event.getId() == null || event.getId().isBlank()) {
            event.setId("evt-" + UUID.randomUUID().toString().substring(0, 8));
        }
        if (feed.getEvents() == null) {
            feed.setEvents(new ArrayList<>());
        }
        feed.getEvents().add(event);
        return event;
    }

    public HeroSection updateHero(String tenantId, HeroSection hero, String updatedBy) {
        String tid = resolveTenant(tenantId);
        TenantWebsiteFeed feed = getWebsiteFeedByTenantId(tid);
        feed.setHero(hero);
        feed.setVersion(feed.getVersion() + 1);
        feed.setPublishedAt(Instant.now());
        feed.setPublishedBy(updatedBy != null ? updatedBy : "admin");
        return hero;
    }
}
