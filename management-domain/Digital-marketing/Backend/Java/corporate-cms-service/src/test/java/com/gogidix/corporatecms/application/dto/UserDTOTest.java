package com.gogidix.corporatecms.application.dto;

import com.gogidix.corporatecms.application.dto.UserDTO;
import com.gogidix.corporatecms.domain.enums.UserRole;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserDTOTest {

        @Test
    void testBuilder() {
        UserDTO dto = UserDTO.builder()
                        .id("test-id")
            .username("test-username")
            .email("test-email")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .displayName("test-displayName")
            .avatar("test-avatar")
            .phoneNumber("test-phoneNumber")
            .role(UserRole.ADMIN)
            .department("test-department")
            .jobTitle("test-jobTitle")
            .bio("test-bio")
            .enabled(true)
            .emailVerified(true)
            .preferences(Collections.emptyMap())
            .permissions(Collections.emptyList())
            .locale("test-locale")
            .timezone("test-timezone")
            .lastLoginAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-username", dto.getUsername());
        assertEquals("test-email", dto.getEmail());
        assertEquals("test-firstName", dto.getFirstName());
        assertEquals("test-lastName", dto.getLastName());
        assertEquals("test-displayName", dto.getDisplayName());
        assertEquals("test-avatar", dto.getAvatar());
        assertEquals("test-phoneNumber", dto.getPhoneNumber());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-jobTitle", dto.getJobTitle());
        assertEquals("test-bio", dto.getBio());
        assertTrue(dto.getEnabled());
        assertTrue(dto.getEmailVerified());
        assertEquals("test-locale", dto.getLocale());
        assertEquals("test-timezone", dto.getTimezone());
    }

    @Test
    void testSettersAndGetters() {
        UserDTO dto = new UserDTO();
        dto.setId("val-id");
        dto.setUsername("val-username");
        dto.setEmail("val-email");
        dto.setFirstName("val-firstName");
        dto.setLastName("val-lastName");
        dto.setDisplayName("val-displayName");
        dto.setAvatar("val-avatar");
        dto.setPhoneNumber("val-phoneNumber");
        dto.setDepartment("val-department");
        dto.setJobTitle("val-jobTitle");
        dto.setBio("val-bio");
        dto.setEnabled(true);
        dto.setEmailVerified(true);
        dto.setLocale("val-locale");
        dto.setTimezone("val-timezone");
        assertEquals("val-id", dto.getId());
        assertEquals("val-username", dto.getUsername());
        assertEquals("val-email", dto.getEmail());
        assertEquals("val-firstName", dto.getFirstName());
        assertEquals("val-lastName", dto.getLastName());
        assertEquals("val-displayName", dto.getDisplayName());
        assertEquals("val-avatar", dto.getAvatar());
        assertEquals("val-phoneNumber", dto.getPhoneNumber());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-jobTitle", dto.getJobTitle());
        assertEquals("val-bio", dto.getBio());
        assertTrue(dto.getEnabled());
        assertTrue(dto.getEmailVerified());
        assertEquals("val-locale", dto.getLocale());
        assertEquals("val-timezone", dto.getTimezone());
    }

    @Test
    void testEqualsAndHashCode() {
        UserDTO dto1 = UserDTO.builder()
                        .id("test-id")
            .username("test-username")
            .email("test-email")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .displayName("test-displayName")
            .avatar("test-avatar")
            .phoneNumber("test-phoneNumber")
            .role(UserRole.ADMIN)
            .department("test-department")
            .jobTitle("test-jobTitle")
            .bio("test-bio")
            .enabled(true)
            .emailVerified(true)
            .preferences(Collections.emptyMap())
            .permissions(Collections.emptyList())
            .locale("test-locale")
            .timezone("test-timezone")
            .lastLoginAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        UserDTO dto2 = UserDTO.builder()
                        .id("test-id")
            .username("test-username")
            .email("test-email")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .displayName("test-displayName")
            .avatar("test-avatar")
            .phoneNumber("test-phoneNumber")
            .role(UserRole.ADMIN)
            .department("test-department")
            .jobTitle("test-jobTitle")
            .bio("test-bio")
            .enabled(true)
            .emailVerified(true)
            .preferences(Collections.emptyMap())
            .permissions(Collections.emptyList())
            .locale("test-locale")
            .timezone("test-timezone")
            .lastLoginAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UserDTO dto = UserDTO.builder()
                        .id("test-id")
            .username("test-username")
            .email("test-email")
            .firstName("test-firstName")
            .lastName("test-lastName")
            .displayName("test-displayName")
            .avatar("test-avatar")
            .phoneNumber("test-phoneNumber")
            .role(UserRole.ADMIN)
            .department("test-department")
            .jobTitle("test-jobTitle")
            .bio("test-bio")
            .enabled(true)
            .emailVerified(true)
            .preferences(Collections.emptyMap())
            .permissions(Collections.emptyList())
            .locale("test-locale")
            .timezone("test-timezone")
            .lastLoginAt(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}