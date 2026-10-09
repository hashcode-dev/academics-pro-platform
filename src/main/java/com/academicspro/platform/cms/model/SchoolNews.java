package com.academicspro.platform.cms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolNews {
    private String id;
    private String title;
    private String slug;
    private LocalDate publishedDate;
    private String summary;
    private String body;
    private String author;
    private List<String> tags;
    private String imageUrl;
}
