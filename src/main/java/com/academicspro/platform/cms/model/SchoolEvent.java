package com.academicspro.platform.cms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolEvent {
    private String id;
    private String title;
    private String category;
    private LocalDate eventDate;
    private String time;
    private String location;
    private String description;
    private String rsvpUrl;
    private boolean featured;
}
