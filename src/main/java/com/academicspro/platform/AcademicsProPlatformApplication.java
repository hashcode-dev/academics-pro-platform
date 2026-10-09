package com.academicspro.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the Academics Pro SaaS backend platform and API gateway.
 *
 * <p>Placed in the root package {@code com.academicspro.platform} so that Spring Boot's
 * component scanning discovers every sub-package (config, controller, dto, exception,
 * security, tenancy, tracing) automatically.
 */
@SpringBootApplication
public class AcademicsProPlatformApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcademicsProPlatformApplication.class, args);
    }
}

