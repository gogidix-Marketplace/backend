package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.mapper;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.response.TenantResponseDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("TenantMapper Tests")
class TenantMapperTest {

    private final TenantMapper mapper = new TenantMapper();

    @Test
    void shouldMapToResponseDto() {
        Tenant tenant = Tenant.builder()
            .id("1").tenantId("t1").name("Test").domain("test.com")
            .status(Tenant.TenantStatus.ACTIVE).plan(Tenant.TenantPlan.FREE)
            .maxUsers(10).maxStorageGB(5)
            .build();

        TenantResponseDto dto = mapper.toResponseDto(tenant);
        assertNotNull(dto);
        assertEquals("1", dto.getId());
        assertEquals("t1", dto.getTenantId());
        assertEquals("Test", dto.getName());
        assertEquals(Tenant.TenantStatus.ACTIVE, dto.getStatus());
        assertTrue(dto.isActive());
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertNull(mapper.toResponseDto(null));
    }
}
