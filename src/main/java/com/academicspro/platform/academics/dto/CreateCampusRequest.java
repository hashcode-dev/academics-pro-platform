package com.academicspro.platform.academics.dto;

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
public class CreateCampusRequest {
    @NotBlank(message = "Campus name is required")
    private String name;

    @NotBlank(message = "Campus code is required")
    private String code;

    @NotBlank(message = "Address is required")
    private String address;

    private String contactPhone;
    private String contactEmail;

    @Positive(message = "Capacity must be greater than zero")
    private int studentCapacity;

    private String gradesOffered;
}
