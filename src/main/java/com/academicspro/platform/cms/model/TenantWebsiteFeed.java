package com.academicspro.platform.cms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tenant_website_feeds")
public class TenantWebsiteFeed {
    @Id
    private String id;
    private String tenantId;
    private String tenantSlug;
    private String institutionName;
    private String tagline;
    private String logoUrl;
    private String primaryColor;
    private String contactEmail;
    private String contactPhone;
    private String campusAddress;

    @Builder.Default
    private HeroSection hero = new HeroSection();

    @Builder.Default
    private List<AcademicProgram> programs = new ArrayList<>();

    @Builder.Default
    private List<Testimonial> testimonials = new ArrayList<>();

    @Builder.Default
    private List<CampusFacility> facilities = new ArrayList<>();

    @Builder.Default
    private List<AdmissionStep> admissionSteps = new ArrayList<>();

    @Builder.Default
    private List<SchoolEvent> events = new ArrayList<>();

    @Builder.Default
    private List<SchoolNews> news = new ArrayList<>();

    private int version;
    private Instant publishedAt;
    private String publishedBy;
}
