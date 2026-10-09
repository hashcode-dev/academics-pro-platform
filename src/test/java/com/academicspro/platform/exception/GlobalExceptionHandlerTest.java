package com.academicspro.platform.exception;

import com.academicspro.platform.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleIllegalArgument() {
        ResponseEntity<ApiResponse<Void>> response = handler.handleIllegalArgument(new IllegalArgumentException("Invalid argument provided"));
        assertEquals(400, response.getStatusCode().value());
        assertFalse(response.getBody().isSuccess());
        assertEquals("BAD_REQUEST", response.getBody().getErrorCode());
        assertEquals("Invalid argument provided", response.getBody().getMessage());
    }

    @Test
    void testHandleGenericException() {
        ResponseEntity<ApiResponse<Void>> response = handler.handleGenericException(new RuntimeException("Something failed"));
        assertEquals(500, response.getStatusCode().value());
        assertFalse(response.getBody().isSuccess());
        assertEquals("INTERNAL_SERVER_ERROR", response.getBody().getErrorCode());
    }
}
