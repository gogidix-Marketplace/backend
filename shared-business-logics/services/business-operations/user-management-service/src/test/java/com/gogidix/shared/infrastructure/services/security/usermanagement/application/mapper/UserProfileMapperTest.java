package com.gogidix.shared.infrastructure.services.security.usermanagement.application.mapper;

import com.gogidix.shared.servicediscovery.config.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.usermanagement.application.dto.response.UserProfileResponseDto;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("UserProfileMapper Tests")
class UserProfileMapperTest {

    private final UserProfileMapper mapper = new UserProfileMapper();

    @Test
    void shouldMapToResponseDto() {
        UserProfile profile = UserProfile.builder()
            .id("p1")
            .userId("u1")
            .tenantId(TenantId.of("t1"))
            .firstName("John")
            .lastName("Doe")
            .displayName("John Doe")
            .email("john@test.com")
            .phoneNumber("+123")
            .avatarUrl("http://avatar")
            .bio("Hello")
            .address(UserProfile.Address.builder().city("NYC").country("US").build())
            .preferences(UserProfile.UserPreferences.builder().language("en").build())
            .metadata(Map.of("key", "val"))
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        UserProfileResponseDto dto = mapper.toResponseDto(profile);

        assertNotNull(dto);
        assertEquals("p1", dto.getId());
        assertEquals("u1", dto.getUserId());
        assertEquals("t1", dto.getTenantId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("john@test.com", dto.getEmail());
        assertNotNull(dto.getAddress());
        assertNotNull(dto.getPreferences());
    }

    @Test
    void shouldReturnNullForNullInput() {
        assertNull(mapper.toResponseDto(null));
    }

    @Test
    void shouldHandleNullTenantId() {
        UserProfile profile = UserProfile.builder()
            .id("p1")
            .userId("u1")
            .build();

        UserProfileResponseDto dto = mapper.toResponseDto(profile);
        assertNotNull(dto);
        assertNull(dto.getTenantId());
    }
}
