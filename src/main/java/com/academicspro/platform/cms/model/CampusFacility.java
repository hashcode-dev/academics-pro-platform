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
public class CampusFacility {
    private String id;
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private List<String> features;
}
