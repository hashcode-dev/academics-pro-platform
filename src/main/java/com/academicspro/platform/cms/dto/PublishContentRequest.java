package com.academicspro.platform.cms.dto;

import com.academicspro.platform.cms.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublishContentRequest {
    private HeroSection hero;
    private List<AcademicProgram> programs;
    private List<Testimonial> testimonials;
    private List<CampusFacility> facilities;
    private List<AdmissionStep> admissionSteps;
    private List<SchoolEvent> events;
    private List<SchoolNews> news;
}
