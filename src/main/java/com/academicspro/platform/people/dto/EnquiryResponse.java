package com.academicspro.platform.people.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnquiryResponse {
    private String confirmationNumber;
    private String status;
    private String message;
    private Instant submittedAt;
}
