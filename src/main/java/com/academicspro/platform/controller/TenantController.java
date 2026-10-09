package com.academicspro.platform.controller;

import com.academicspro.platform.dto.ApiResponse;
import com.academicspro.platform.dto.TenantInfoDto;
import com.academicspro.platform.tenancy.TenantContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/v1/tenants")
public class TenantController {

    private static final Map<String, TenantInfoDto> TENANT_STORE = new ConcurrentHashMap<>();

    static {
        TenantInfoDto apex = TenantInfoDto.builder()
                .tenantId("apex-high")
                .institutionId("inst-apex-high")
                .name("Apex High School")
                .slug("apex-high")
                .tagline("Shaping World-Class Thinkers and Compassionate Global Leaders")
                .logoUrl("/logos/apex-high.svg")
                .primaryColor("#004bca")
                .contactEmail("admissions@apexhigh.edu")
                .phone("+1 (555) 382-9011")
                .address("742 Evergreen Crest, Cambridge, MA 02138")
                .establishedYear(1982)
                .studentCount(1250)
                .facultyCount(98)
                .passRatePercentage(98.4)
                .accreditations(List.of("IB World School", "Cambridge Assessment International", "New England CIS", "STEM Certified Excellence"))
                .curricula(List.of("International Baccalaureate (IB)", "Advanced Placement (AP)", "IGCSE / A-Levels"))
                .build();

        TenantInfoDto oakridge = TenantInfoDto.builder()
                .tenantId("oakridge-academy")
                .institutionId("inst-oakridge")
                .name("Oakridge International Academy")
                .slug("oakridge-academy")
                .tagline("Excellence in Inquiry-Based Learning and Innovation")
                .logoUrl("/logos/oakridge.svg")
                .primaryColor("#007f57")
                .contactEmail("contact@oakridge.edu")
                .phone("+1 (555) 749-1022")
                .address("120 Academy Way, Austin, TX 78701")
                .establishedYear(1996)
                .studentCount(890)
                .facultyCount(74)
                .passRatePercentage(99.1)
                .accreditations(List.of("IB World School", "Cognia Accredited"))
                .curricula(List.of("IB Primary & Middle Years", "IB Diploma Programme"))
                .build();

        TENANT_STORE.put("apex-high", apex);
        TENANT_STORE.put("oakridge-academy", oakridge);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TenantInfoDto>>> listTenants() {
        return ResponseEntity.ok(ApiResponse.ok(List.copyOf(TENANT_STORE.values())));
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<TenantInfoDto>> getCurrentTenant() {
        String tenantId = TenantContext.getTenantId();
        if (tenantId == null || !TENANT_STORE.containsKey(tenantId)) {
            tenantId = "apex-high";
        }
        return ResponseEntity.ok(ApiResponse.ok(TENANT_STORE.get(tenantId)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<TenantInfoDto>> getTenantBySlug(@PathVariable String slug) {
        TenantInfoDto tenant = TENANT_STORE.get(slug.toLowerCase());
        if (tenant == null) {
            return ResponseEntity.status(404).body(ApiResponse.error("TENANT_NOT_FOUND", "Tenant not found: " + slug));
        }
        return ResponseEntity.ok(ApiResponse.ok(tenant));
    }
}
