package com.academicspro.platform.cms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Testimonial {
    private String id;
    private String authorName;
    private String role;
    private String cohortYear;
    private String quote;
    private String universityBadge;
    private String avatarUrl;
    private double rating;
}
