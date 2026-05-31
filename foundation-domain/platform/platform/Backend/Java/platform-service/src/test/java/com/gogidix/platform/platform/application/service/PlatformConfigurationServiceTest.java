package com.gogidix.platform.platform.application.service;

import com.gogidix.platform.platform.application.dto.PlatformConfigDto;
import com.gogidix.platform.platform.domain.model.PlatformConfiguration;
import com.gogidix.platform.platform.domain.port.in.CreatePlatformConfigCommand;
import com.gogidix.platform.platform.domain.port.in.UpdatePlatformConfigCommand;
import com.gogidix.platform.platform.domain.repository.PlatformConfigurationRepository;
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
 * Unit tests for PlatformConfigurationService.
 * Comprehensive test coverage for configuration management operations.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PlatformConfigurationService Tests")
class PlatformConfigurationServiceTest {

    @Mock
    private PlatformConfigurationRepository configurationRepository;

    @Mock
    private AuditService auditService;

    @InjectMocks
    private PlatformConfigurationService configurationService;

    private CreatePlatformConfigCommand createCommand;
    private UpdatePlatformConfigCommand updateCommand;
    private PlatformConfiguration testConfig;

    @BeforeEach
    void setUp() {
        createCommand = CreatePlatformConfigCommand.builder()
            .configKey("max_upload_size")
            .configValue("100MB")
            .configType(PlatformConfiguration.ConfigType.STRING)
            .description("Maximum file upload size")
            .isSensitive(false)
            .isEncrypted(false)
            .environment(PlatformConfiguration.Environment.PROD)
            .tags(new String[]{"upload", "storage"})
            .metadata(Map.of("category", "limits"))
            .build();

        updateCommand = UpdatePlatformConfigCommand.builder()
            .configValue("200MB")
            .description("Updated maximum file upload size")
            .tags(new String[]{"upload", "storage", "updated"})
            .metadata(Map.of("category", "limits", "version", "2"))
            .build();

        testConfig = PlatformConfiguration.builder()
            .id("config-id-123")
            .tenantId("tenant-123")
            .configKey("max_upload_size")
            .configValue("100MB")
            .configType(PlatformConfiguration.ConfigType.STRING)
            .description("Maximum file upload size")
            .isSensitive(false)
            .isEncrypted(false)
            .environment(PlatformConfiguration.Environment.PROD)
            .tags(new String[]{"upload", "storage"})
            .metadata(Map.of("category", "limits"))
            .status(PlatformConfiguration.ConfigStatus.ACTIVE)
            .version(1)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }

    @Test
    @DisplayName("Should create configuration successfully")
    void createConfiguration_Success() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant-123");

            when(configurationRepository.findByConfigKey("max_upload_size"))
                .thenReturn(Optional.empty());
            when(configurationRepository.save(any(PlatformConfiguration.class)))
                .thenReturn(testConfig);

            // Act
            PlatformConfigDto result = configurationService.createConfiguration(createCommand);

            // Assert
            assertThat(result).isNotNull();
            assertThat(result.getConfigKey()).isEqualTo("max_upload_size");
            assertThat(result.getConfigValue()).isEqualTo("100MB");
            assertThat(result.getConfigType()).isEqualTo("STRING");

            verify(configurationRepository).save(any(PlatformConfiguration.class));
            verify(auditService).logEvent(anyString(), eq("CONFIG_CREATED"), eq("PlatformConfiguration"), anyString());
        }
    }

    @Test
    @DisplayName("Should set default environment when not provided")
    void createConfiguration_DefaultEnvironment() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            createCommand.setEnvironment(null);
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant-123");

            when(configurationRepository.findByConfigKey("max_upload_size"))
                .thenReturn(Optional.empty());
            when(configurationRepository.save(any(PlatformConfiguration.class)))
                .thenAnswer(invocation -> {
                    PlatformConfiguration config = invocation.getArgument(0);
                    assertThat(config.getEnvironment()).isEqualTo(PlatformConfiguration.Environment.ALL);
                    return testConfig;
                });

            // Act
            configurationService.createConfiguration(createCommand);

            // Assert - verified in the answer
        }
    }

    @Test
    @DisplayName("Should throw ConflictException when config key exists")
    void createConfiguration_Conflict() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant-123");

            when(configurationRepository.findByConfigKey("max_upload_size"))
                .thenReturn(Optional.of(testConfig));

            // Act & Assert
            assertThatThrownBy(() -> configurationService.createConfiguration(createCommand))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Configuration key already exists");

            verify(configurationRepository, never()).save(any(PlatformConfiguration.class));
        }
    }

    @Test
    @DisplayName("Should update configuration successfully")
    void updateConfiguration_Success() {
        // Arrange
        PlatformConfiguration updatedConfig = PlatformConfiguration.builder()
            .id("config-id-123")
            .tenantId("tenant-123")
            .configKey("max_upload_size")
            .configValue("200MB")
            .configType(PlatformConfiguration.ConfigType.STRING)
            .description("Updated maximum file upload size")
            .isSensitive(false)
            .isEncrypted(false)
            .environment(PlatformConfiguration.Environment.PROD)
            .tags(new String[]{"upload", "storage", "updated"})
            .metadata(Map.of("category", "limits", "version", "2"))
            .status(PlatformConfiguration.ConfigStatus.ACTIVE)
            .version(2)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        when(configurationRepository.findById("config-id-123"))
            .thenReturn(Optional.of(testConfig));
        when(configurationRepository.save(any(PlatformConfiguration.class)))
            .thenReturn(updatedConfig);

        // Act
        PlatformConfigDto result = configurationService.updateConfiguration("config-id-123", updateCommand);

        // Assert
        assertThat(result.getConfigValue()).isEqualTo("200MB");
        verify(configurationRepository).save(any(PlatformConfiguration.class));
        verify(auditService).logEvent(anyString(), eq("CONFIG_UPDATED"), eq("PlatformConfiguration"), eq("config-id-123"));
    }

    @Test
    @DisplayName("Should throw NotFoundException when updating non-existent config")
    void updateConfiguration_NotFound() {
        // Arrange
        when(configurationRepository.findById("nonexistent"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> configurationService.updateConfiguration("nonexistent", updateCommand))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Configuration not found");

        verify(configurationRepository, never()).save(any(PlatformConfiguration.class));
    }

    @Test
    @DisplayName("Should get configuration by key")
    void getConfigurationByKey_Success() {
        // Arrange
        when(configurationRepository.findByConfigKey("max_upload_size"))
            .thenReturn(Optional.of(testConfig));

        // Act
        PlatformConfigDto result = configurationService.getConfigurationByKey("max_upload_size");

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getConfigKey()).isEqualTo("max_upload_size");
        assertThat(result.getConfigValue()).isEqualTo("100MB");

        verify(configurationRepository).findByConfigKey("max_upload_size");
    }

    @Test
    @DisplayName("Should throw NotFoundException when config not found by key")
    void getConfigurationByKey_NotFound() {
        // Arrange
        when(configurationRepository.findByConfigKey("nonexistent"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> configurationService.getConfigurationByKey("nonexistent"))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Configuration not found");
    }

    @Test
    @DisplayName("Should get all configurations for tenant")
    void getAllConfigurations_Success() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant-123");

            PlatformConfiguration config2 = PlatformConfiguration.builder()
                .id("config-id-456")
                .tenantId("tenant-123")
                .configKey("timeout")
                .configValue("30000")
                .configType(PlatformConfiguration.ConfigType.NUMBER)
                .description("Request timeout in milliseconds")
                .status(PlatformConfiguration.ConfigStatus.ACTIVE)
                .version(1)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

            when(configurationRepository.findByTenantId("tenant-123"))
                .thenReturn(Arrays.asList(testConfig, config2));

            // Act
            List<PlatformConfigDto> result = configurationService.getAllConfigurations();

            // Assert
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getConfigKey()).isEqualTo("max_upload_size");
            assertThat(result.get(1).getConfigKey()).isEqualTo("timeout");

            verify(configurationRepository).findByTenantId("tenant-123");
        }
    }

    @Test
    @DisplayName("Should mask sensitive configuration values")
    void getAllConfigurations_SensitiveMasked() {
        try (MockedStatic<RequestContext> requestContext = mockStatic(RequestContext.class)) {
            // Arrange
            requestContext.when(RequestContext::getTenantIdFromThreadLocal)
                .thenReturn("tenant-123");

            testConfig.setSensitive(true);
            when(configurationRepository.findByTenantId("tenant-123"))
                .thenReturn(Collections.singletonList(testConfig));

            // Act
            List<PlatformConfigDto> result = configurationService.getAllConfigurations();

            // Assert
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getConfigValue()).isEqualTo("***HIDDEN***");
        }
    }

    @Test
    @DisplayName("Should delete configuration successfully")
    void deleteConfiguration_Success() {
        // Arrange
        when(configurationRepository.findById("config-id-123"))
            .thenReturn(Optional.of(testConfig));
        doNothing().when(configurationRepository).delete(any(PlatformConfiguration.class));

        // Act
        configurationService.deleteConfiguration("config-id-123");

        // Assert
        verify(configurationRepository).delete(testConfig);
        verify(auditService).logEvent(anyString(), eq("CONFIG_DELETED"), eq("PlatformConfiguration"), eq("config-id-123"));
    }

    @Test
    @DisplayName("Should throw NotFoundException when deleting non-existent config")
    void deleteConfiguration_NotFound() {
        // Arrange
        when(configurationRepository.findById("nonexistent"))
            .thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> configurationService.deleteConfiguration("nonexistent"))
            .isInstanceOf(NotFoundException.class)
            .hasMessageContaining("Configuration not found");

        verify(configurationRepository, never()).delete(any(PlatformConfiguration.class));
    }
}
