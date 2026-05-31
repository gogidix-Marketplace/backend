package com.gogidix.platform.platform.application.service;

import com.gogidix.platform.platform.application.dto.FeatureFlagDto;
import com.gogidix.platform.platform.domain.model.FeatureFlag;
import com.gogidix.platform.platform.domain.port.in.CreateFeatureFlagCommand;
import com.gogidix.platform.platform.domain.repository.FeatureFlagRepository;
import com.gogidix.shared.exceptions.ConflictException;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.security.context.RequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FeatureFlagService.
 * Tests cover all service methods with 80%+ coverage target.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("FeatureFlagService Tests")
class FeatureFlagServiceTest {

    @Mock
    private FeatureFlagRepository featureFlagRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private FeatureFlagService featureFlagService;

    private CreateFeatureFlagCommand createCommand;
    private FeatureFlag testFlag;

    @BeforeEach
    void setUp() {
        createCommand = CreateFeatureFlagCommand.builder()
            .featureKey("new_dashboard_v2")
            .featureName("New Dashboard V2")
            .description("New dashboard interface")
            .featureType(FeatureFlag.FeatureType.FEATURE)
            .enabled(true)
            .allowedTenants(new String[]{"tenant1", "tenant2"})
            .deniedTenants(new String[]{})
            .userSegments(Arrays.asList("beta", "internal"))
            .rolloutRules(Map.of("percentage", 50))
            .requiresOptIn(false)
            .build();

        testFlag = FeatureFlag.builder()
            .id("flag-id-123")
            .tenantId("tenant-123")
            .featureKey("new_dashboard_v2")
            .featureName("New Dashboard V2")
            .description("New dashboard interface")
            .featureType(FeatureFlag.FeatureType.FEATURE)
            .isEnabled(true)
            .allowedTenants(new String[]{"tenant1", "tenant2"})
            .deniedTenants(new String[]{})
            .userSegments(Arrays.asList("beta", "internal"))
            .rolloutRules(Map.of("percentage", 50))
            .requiresOptIn(false)
            .rolloutPercentage(100)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Should create feature flag successfully")
    void createFeatureFlag_Success() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant-123");

            when(featureFlagRepository.findByFeatureKey("new_dashboard_v2"))
                .thenReturn(Optional.empty());
            when(featureFlagRepository.save(any(FeatureFlag.class)))
                .thenReturn(testFlag);

            // Act
            FeatureFlagDto result = featureFlagService.createFeatureFlag(createCommand);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getFeatureKey()).isEqualTo("new_dashboard_v2");
            assertThat(result.getFeatureName()).isEqualTo("New Dashboard V2");
            assertThat(result.isEnabled()).isTrue();

            verify(featureFlagRepository).save(any(FeatureFlag.class));
            verify(auditService).logEvent(anyString(), eq("FEATURE_FLAG_CREATED"), eq("FeatureFlag"), anyString());
        }
    }

    @Test
    @DisplayName("Should throw ConflictException when feature key already exists")
    void createFeatureFlag_Conflict() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant-123");

            when(featureFlagRepository.findByFeatureKey("new_dashboard_v2"))
                .thenReturn(Optional.of(testFlag));

            // Act & Assert
            assertThatThrownBy(() -> featureFlagService.createFeatureFlag(createCommand))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Feature flag key already exists");

            verify(featureFlagRepository, never()).save(any(FeatureFlag.class));
            verify(auditService, never()).logEvent(anyString(), anyString(), anyString(), anyString());
        }
    }

    @Test
    @DisplayName("Should get feature flag by key")
    void getFeatureFlag_Success() {
        // Arrange
        when(featureFlagRepository.findByFeatureKey("new_dashboard_v2"))
            .thenReturn(Optional.of(testFlag));

        // Act
        FeatureFlagDto result = featureFlagService.getFeatureFlag("new_dashboard_v2");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getFeatureKey()).isEqualTo("new_dashboard_v2");
        assertThat(result.getFeatureName()).isEqualTo("New Dashboard V2");

        verify(featureFlagRepository).findByFeatureKey("new_dashboard_v2");
    }

    @Test
    @DisplayName("Should throw NotFoundException when feature flag not found")
    void getFeatureFlag_NotFound() {
        // Arrange
        when(featureFlagRepository.findByFeatureKey("nonexistent"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> featureFlagService.getFeatureFlag("nonexistent"))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Feature flag not found");

        verify(featureFlagRepository).findByFeatureKey("nonexistent");
    }

    @Test
    @DisplayName("Should return true when feature is enabled")
    void isFeatureEnabled_True() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant1");

            testFlag.setEnabled(true);
            when(featureFlagRepository.findByFeatureKey("new_dashboard_v2"))
                .thenReturn(Optional.of(testFlag));

            // Act
            boolean result = featureFlagService.isFeatureEnabled("new_dashboard_v2", "user123");

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Should return false when feature is disabled")
    void isFeatureEnabled_False() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant1");

            testFlag.setEnabled(false);
            when(featureFlagRepository.findByFeatureKey("new_dashboard_v2"))
                .thenReturn(Optional.of(testFlag));

            // Act
            boolean result = featureFlagService.isFeatureEnabled("new_dashboard_v2", "user123");

            // Assert
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("Should return false when tenant is denied")
    void isFeatureEnabled_DeniedTenant() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("denied-tenant");

            testFlag.setEnabled(true);
            testFlag.setDeniedTenants(new String[]{"denied-tenant"});
            when(featureFlagRepository.findByFeatureKey("new_dashboard_v2"))
                .thenReturn(Optional.of(testFlag));

            // Act
            boolean result = featureFlagService.isFeatureEnabled("new_dashboard_v2", "user123");

            // Assert
            assertThat(result).isFalse();
        }
    }

    @Test
    @DisplayName("Should return true when tenant is allowed")
    void isFeatureEnabled_AllowedTenant() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant1");

            testFlag.setEnabled(true);
            testFlag.setAllowedTenants(new String[]{"tenant1"});
            when(featureFlagRepository.findByFeatureKey("new_dashboard_v2"))
                .thenReturn(Optional.of(testFlag));

            // Act
            boolean result = featureFlagService.isFeatureEnabled("new_dashboard_v2", "user123");

            // Assert
            assertThat(result).isTrue();
        }
    }

    @Test
    @DisplayName("Should get all feature flags for tenant")
    void getAllFeatureFlags_Success() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant-123");

            FeatureFlag flag2 = FeatureFlag.builder()
                .id("flag-id-456")
                .tenantId("tenant-123")
                .featureKey("another_feature")
                .featureName("Another Feature")
                .featureType(FeatureFlag.FeatureType.FEATURE)
                .isEnabled(false)
                .rolloutPercentage(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            when(featureFlagRepository.findByTenantId("tenant-123"))
                .thenReturn(Arrays.asList(testFlag, flag2));

            // Act
            List<FeatureFlagDto> result = featureFlagService.getAllFeatureFlags();

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getFeatureKey()).isEqualTo("new_dashboard_v2");
            assertThat(result.get(1).getFeatureKey()).isEqualTo("another_feature");

            verify(featureFlagRepository).findByTenantId("tenant-123");
        }
    }

    @Test
    @DisplayName("Should toggle feature flag to enabled")
    void toggleFeatureFlag_Enable() {
        // Arrange
        testFlag.setEnabled(false);
        when(featureFlagRepository.findById("flag-id-123"))
            .thenReturn(Optional.of(testFlag));
        when(featureFlagRepository.save(any(FeatureFlag.class)))
            .thenReturn(testFlag);

        // Act
        FeatureFlagDto result = featureFlagService.toggleFeatureFlag("flag-id-123", true);

        // Assert
        assertThat(result.isEnabled()).isTrue();
        verify(featureFlagRepository).save(any(FeatureFlag.class));
        verify(auditService).logEvent(anyString(), eq("FEATURE_FLAG_TOGGLED"), eq("FeatureFlag"), eq("flag-id-123"));
    }

    @Test
    @DisplayName("Should toggle feature flag to disabled")
    void toggleFeatureFlag_Disable() {
        // Arrange
        testFlag.setEnabled(true);
        when(featureFlagRepository.findById("flag-id-123"))
            .thenReturn(Optional.of(testFlag));
        when(featureFlagRepository.save(any(FeatureFlag.class)))
            .thenReturn(testFlag);

        // Act
        FeatureFlagDto result = featureFlagService.toggleFeatureFlag("flag-id-123", false);

        // Assert
        assertThat(result.isEnabled()).isFalse();
        verify(featureFlagRepository).save(any(FeatureFlag.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException when toggling non-existent flag")
    void toggleFeatureFlag_NotFound() {
        // Arrange
        when(featureFlagRepository.findById("nonexistent"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> featureFlagService.toggleFeatureFlag("nonexistent", true))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Feature flag not found");

        verify(featureFlagRepository, never()).save(any(FeatureFlag.class));
    }

    @Test
    @DisplayName("Should delete feature flag successfully")
    void deleteFeatureFlag_Success() {
        // Arrange
        when(featureFlagRepository.findById("flag-id-123"))
            .thenReturn(Optional.of(testFlag));
        doNothing().when(featureFlagRepository).delete(any(FeatureFlag.class));

        // Act
        featureFlagService.deleteFeatureFlag("flag-id-123");

        // Assert
        verify(featureFlagRepository).delete(testFlag);
        verify(auditService).logEvent(anyString(), eq("FEATURE_FLAG_DELETED"), eq("FeatureFlag"), eq("flag-id-123"));
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting non-existent flag")
    void deleteFeatureFlag_NotFound() {
        // Arrange
        when(featureFlagRepository.findById("nonexistent"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> featureFlagService.deleteFeatureFlag("nonexistent"))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Feature flag not found");

        verify(featureFlagRepository, never()).delete(any(FeatureFlag.class));
    }
}
