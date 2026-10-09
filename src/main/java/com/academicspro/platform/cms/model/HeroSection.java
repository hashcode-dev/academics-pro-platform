package com.academicspro.platform.cms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeroSection {
    private String headline;
    private String subhead;
    private String primaryCtaText;
    private String primaryCtaLink;
    private String secondaryCtaText;
    private String secondaryCtaLink;
    private String badgeText;
    private String badgeHighlight;
    private String backgroundImageUrl;
    private List<String> statsHighlights;
}
