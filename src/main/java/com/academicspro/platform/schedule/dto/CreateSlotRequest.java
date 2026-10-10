package com.academicspro.platform.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSlotRequest {
    @NotBlank(message = "Day is required")
    private String day;

    @Positive(message = "Period must be positive")
    private int period;

    @NotBlank(message = "Time range is required")
    private String time;

    @NotBlank(message = "Subject is required")
    private String subject;

    private String subjectCode;
    private String teacherId;

    @NotBlank(message = "Teacher name is required")
    private String teacherName;

    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotBlank(message = "Section is required")
    private String section;
}
