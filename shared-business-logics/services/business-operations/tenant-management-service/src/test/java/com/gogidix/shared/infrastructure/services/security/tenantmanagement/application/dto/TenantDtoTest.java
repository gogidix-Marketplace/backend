package com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto;

import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.CreateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.request.UpdateTenantRequestDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.application.dto.response.TenantResponseDto;
import com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model.Tenant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tenant DTO Tests")
class TenantDtoTest {

    @Nested
    @DisplayName("CreateTenantRequestDto")
    class CreateDtoTests {

        @Test
        void shouldBuildAndAccess() {
            CreateTenantRequestDto dto = CreateTenantRequestDto.builder()
                .tenantId("t1").name("Test").domain("test.com")
                .logoUrl("logo.png").status(Tenant.TenantStatus.ACTIVE)
                .plan(Tenant.TenantPlan.PROFESSIONAL).trialEndsAt(LocalDateTime.now().plusDays(14))
                .settings(Map.of("k","v")).features(Map.of("f",true))
                .primaryContactEmail("e@t.com").primaryContactName("Admin")
                .maxUsers(100L).maxStorageGB(50L).build();
            assertEquals("t1", dto.getTenantId());
            assertEquals("Test", dto.getName());
            assertEquals(Tenant.TenantPlan.PROFESSIONAL, dto.getPlan());
        }

        @Test
        void shouldSetFields() {
            CreateTenantRequestDto dto = new CreateTenantRequestDto();
            dto.setTenantId("t1");
            dto.setName("Test");
            assertEquals("t1", dto.getTenantId());
        }

        @Test
        void shouldBeEqualWithSameFields() {
            CreateTenantRequestDto d1 = CreateTenantRequestDto.builder().tenantId("t1").name("Test").build();
            CreateTenantRequestDto d2 = CreateTenantRequestDto.builder().tenantId("t1").name("Test").build();
            assertEquals(d1, d2);
        }

        @Test
        void shouldHaveToString() {
            assertNotNull(CreateTenantRequestDto.builder().tenantId("t1").build().toString());
        }
    }

    @Nested
    @DisplayName("UpdateTenantRequestDto")
    class UpdateDtoTests {

        @Test
        void shouldBuildAndAccess() {
            UpdateTenantRequestDto dto = UpdateTenantRequestDto.builder()
                .name("New").logoUrl("new.png").primaryContactEmail("new@t.com")
                .primaryContactName("New Admin").maxUsers(200L).maxStorageGB(100L)
                .settings(Map.of("k","v")).features(Map.of("f",true)).build();
            assertEquals("New", dto.getName());
            assertEquals(200, dto.getMaxUsers());
        }

        @Test
        void shouldBeEqualWithSameFields() {
            UpdateTenantRequestDto d1 = UpdateTenantRequestDto.builder().name("Test").build();
            UpdateTenantRequestDto d2 = UpdateTenantRequestDto.builder().name("Test").build();
            assertEquals(d1, d2);
        }
    }

    @Nested
    @DisplayName("TenantResponseDto")
    class ResponseDtoTests {

        @Test
        void shouldBuildAndAccess() {
            TenantResponseDto dto = TenantResponseDto.builder()
                .id("1").tenantId("t1").name("Test").domain("test.com")
                .logoUrl("logo.png").status(Tenant.TenantStatus.ACTIVE)
                .plan(Tenant.TenantPlan.FREE).trialEndsAt(LocalDateTime.now())
                .settings(Map.of("k","v")).features(Map.of("f",true))
                .primaryContactEmail("e@t.com").primaryContactName("Admin")
                .maxUsers(10L).maxStorageGB(5L).active(true)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            assertEquals("1", dto.getId());
            assertTrue(dto.isActive());
        }

        @Test
        void shouldBeEqualWithSameFields() {
            TenantResponseDto d1 = TenantResponseDto.builder().id("1").tenantId("t1").build();
            TenantResponseDto d2 = TenantResponseDto.builder().id("1").tenantId("t1").build();
            assertEquals(d1, d2);
        }

        @Test
        void shouldNotBeEqualToNull() {
            assertNotEquals(null, TenantResponseDto.builder().build());
        }

        @Test
        void shouldBeEqualToSelf() {
            TenantResponseDto d = TenantResponseDto.builder().build();
            assertEquals(d, d);
        }

        @Test
        void shouldHaveToString() {
            assertNotNull(TenantResponseDto.builder().tenantId("t1").build().toString());
        }
    }
}
