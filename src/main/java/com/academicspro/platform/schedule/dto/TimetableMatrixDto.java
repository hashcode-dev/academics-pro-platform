package com.academicspro.platform.schedule.dto;

import com.academicspro.platform.schedule.model.TimetableSlot;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableMatrixDto {
    private String tenantId;
    private int totalSlots;
    private int totalConflicts;
    private boolean conflictFree;
    private List<TimetableSlot> slots;
}
