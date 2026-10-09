package com.academicspro.platform.tracing;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class CorrelationTraceFilterTest {

    private final CorrelationTraceFilter filter = new CorrelationTraceFilter();

    @Test
    void testExistingCorrelationIdPreserved() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(CorrelationTraceFilter.CORRELATION_ID_HEADER, "corr-12345");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);

        assertEquals("corr-12345", response.getHeader(CorrelationTraceFilter.CORRELATION_ID_HEADER));
    }

    @Test
    void testMissingCorrelationIdGenerated() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilterInternal(request, response, chain);

        String generated = response.getHeader(CorrelationTraceFilter.CORRELATION_ID_HEADER);
        assertNotNull(generated);
        assertFalse(generated.isBlank());
    }
}
