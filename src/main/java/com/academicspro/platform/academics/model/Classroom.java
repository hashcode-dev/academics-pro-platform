package com.academicspro.platform.academics.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classroom {
    private String id;
    private String tenantId;
    private String campusId;
    private String roomNumber;
    private String name;
    private String roomType; // "LECTURE_HALL", "LABORATORY", "AUDITORIUM", "CLASSROOM"
    private int capacity;
}
