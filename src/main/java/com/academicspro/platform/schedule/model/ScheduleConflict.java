package com.academicspro.platform.schedule.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleConflict {
    private String type;
    private String message;
    private String day;
    private int period;
    private List<String> conflictingSlotIds;
}
