package com.gogidix.shared.infrastructure.services.security.usermanagement.infrastructure.persistence;

import com.gogidix.shared.infrastructure.core.tenancy.model.TenantId;
import com.gogidix.shared.infrastructure.services.security.usermanagement.domain.model.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserProfileRepositoryAdapter Tests")
class UserProfileRepositoryAdapterTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileRepositoryAdapter adapter;

    private UserProfile testProfile;

    @BeforeEach
    void setUp() {
        testProfile = UserProfile.builder()
            .id("p1")
            .userId("u1")
            .tenantId(TenantId.of("t1"))
            .firstName("John")
            .build();
    }

    @Nested
    @DisplayName("Save")
    class SaveTests {

        @Test
        void shouldSaveProfile() {
            when(userProfileRepository.save(any())).thenReturn(testProfile);
            UserProfile saved = adapter.save(testProfile);
            assertNotNull(saved);
            assertEquals("p1", saved.getId());
        }
    }

    @Nested
    @DisplayName("FindByIdAndTenantId")
    class FindByIdTests {

        @Test
        void shouldFindProfile() {
            when(userProfileRepository.findById("u1")).thenReturn(Optional.of(testProfile));
            Optional<UserProfile> found = adapter.findByIdAndTenantId("u1", "t1");
            assertTrue(found.isPresent());
            assertEquals("u1", found.get().getUserId());
        }

        @Test
        void shouldReturnEmptyWhenNotFound() {
            when(userProfileRepository.findById("missing")).thenReturn(Optional.empty());
            Optional<UserProfile> found = adapter.findByIdAndTenantId("missing", "t1");
            assertTrue(found.isEmpty());
        }

        @Test
        void shouldReturnEmptyWhenTenantMismatch() {
            when(userProfileRepository.findById("u1")).thenReturn(Optional.of(testProfile));
            Optional<UserProfile> found = adapter.findByIdAndTenantId("u1", "wrong-tenant");
            assertTrue(found.isEmpty());
        }
    }

    @Nested
    @DisplayName("FindByTenantId")
    class FindByTenantTests {

        @Test
        void shouldFindProfilesByTenant() {
            when(userProfileRepository.findByTenantId_Value("t1"))
                .thenReturn(List.of(testProfile));
            List<UserProfile> profiles = adapter.findByTenantId("t1");
            assertEquals(1, profiles.size());
        }
    }

    @Nested
    @DisplayName("Delete")
    class DeleteTests {

        @Test
        void shouldDeleteProfile() {
            adapter.delete(testProfile);
            verify(userProfileRepository).delete(testProfile);
        }
    }

    @Nested
    @DisplayName("ExistsByIdAndTenantId")
    class ExistsTests {

        @Test
        void shouldCheckExistence() {
            when(userProfileRepository.existsByIdAndTenantId_Value("u1", "t1")).thenReturn(true);
            assertTrue(adapter.existsByIdAndTenantId("u1", "t1"));
        }
    }
}
