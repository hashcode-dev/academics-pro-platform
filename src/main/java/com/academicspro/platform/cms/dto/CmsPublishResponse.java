package com.academicspro.platform.cms.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CmsPublishResponse {
    private String tenantId;
    private String tenantSlug;
    private int version;
    private Instant publishedAt;
    private String status;
    private String message;
}
