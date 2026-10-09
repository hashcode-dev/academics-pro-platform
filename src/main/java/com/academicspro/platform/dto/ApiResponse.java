package com.academicspro.platform.dto;

import com.academicspro.platform.tenancy.TenantContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.MDC;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    @Builder.Default
    private String timestamp = Instant.now().toString();
    private String correlationId;
    private String tenantId;

    public static <T> ApiResponse<T> ok(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .correlationId(MDC.get("correlationId"))
                .tenantId(TenantContext.getTenantId())
                .build();
    }

    public static <T> ApiResponse<T> ok(T data) {
        return ok(data, "Operation completed successfully");
    }

    public static <T> ApiResponse<T> error(String errorCode, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .errorCode(errorCode)
                .message(message)
                .correlationId(MDC.get("correlationId"))
                .tenantId(TenantContext.getTenantId())
                .build();
    }
}
