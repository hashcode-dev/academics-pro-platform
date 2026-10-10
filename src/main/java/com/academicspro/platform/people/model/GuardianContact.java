package com.academicspro.platform.people.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuardianContact {
    private String name;
    private String relationship;
    private String phone;
    private String email;
    private String address;
}
