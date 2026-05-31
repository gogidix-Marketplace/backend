package com.gogidix.centralconfiguration.configserver.application.service;

import com.gogidix.centralconfiguration.configserver.application.dto.request.CreateConfigRequestDto;
import com.gogidix.centralconfiguration.configserver.application.dto.request.UpdateConfigRequestDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigurationResponseDto;
import com.gogidix.centralconfiguration.configserver.application.mapper.ConfigMapper;
import com.gogidix.centralconfiguration.configserver.domain.model.Configuration;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationHistory;
import com.gogidix.centralconfiguration.configserver.domain.port.in.CreateConfigCommand;
import com.gogidix.centralconfiguration.configserver.domain.port.in.UpdateConfigCommand;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationHistoryRepository;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationRepository;
import com.gogidix.centralconfiguration.configserver.infrastructure.messaging.kafka.ConfigEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfigCommandService Tests")
class ConfigCommandServiceTest {

    @Mock
    private ConfigurationRepository configRepository;

    @Mock
    private ConfigurationHistoryRepository historyRepository;

    @Mock
    private ConfigMapper configMapper;

    @Mock
    private ConfigEventPublisher eventPublisher;

    @InjectMocks
    private ConfigCommandService configCommandService;

    private CreateConfigRequestDto createRequestDto;
    private UpdateConfigRequestDto updateRequestDto;
    private Configuration testConfiguration;
    private CreateConfigCommand createCommand;
    private UpdateConfigCommand updateCommand;

    @BeforeEach
    void setUp() {
        createRequestDto = CreateConfigRequestDto.builder()
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .configValue("30000")
                .isEncrypted(false)
                .description("Database connection timeout")
                .build();

        updateRequestDto = UpdateConfigRequestDto.builder()
                .configValue("60000")
                .isEncrypted(false)
                .description("Updated timeout")
                .changeReason("Performance optimization")
                .build();

        testConfiguration = Configuration.builder()
                .id(1L)
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .configValue("30000")
                .isEncrypted(false)
                .description("Database connection timeout")
                .version(1)
                .isActive(true)
                .createdBy("admin")
                .build();

        createCommand = new CreateConfigCommand(
                "tenant-1", "payment-service", "prod", "database.timeout",
                "30000", false, "Database connection timeout", "admin"
        );

        updateCommand = new UpdateConfigCommand(
                1L, "tenant-1", "60000", false, "Updated timeout", "admin", "Performance optimization"
        );
    }

    @Test
    @DisplayName("Should create configuration successfully")
    void createConfig_Success() {
        // Given
        when(configRepository.existsByTenantIdAndApplicationNameAndProfileAndConfigKey(
                any(), any(), any(), any())).thenReturn(false);
        when(configMapper.toCreateCommand(any(), any(), any())).thenReturn(createCommand);
        when(configRepository.save(any(Configuration.class))).thenReturn(testConfiguration);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(createResponseDto());

        // When
        ConfigurationResponseDto result = configCommandService.createConfig(
                createRequestDto, "tenant-1", "admin");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getApplicationName()).isEqualTo("payment-service");
        assertThat(result.getConfigKey()).isEqualTo("database.timeout");

        verify(configRepository).save(any(Configuration.class));
        verify(historyRepository).save(any(ConfigurationHistory.class));
        verify(eventPublisher).publishConfigCreated(any(Configuration.class));
    }

    @Test
    @DisplayName("Should throw exception when creating duplicate configuration")
    void createConfig_DuplicateKey_ThrowsException() {
        // Given
        when(configRepository.existsByTenantIdAndApplicationNameAndProfileAndConfigKey(
                any(), any(), any(), any())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> configCommandService.createConfig(
                createRequestDto, "tenant-1", "admin"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Configuration already exists");

        verify(configRepository, never()).save(any(Configuration.class));
        verify(eventPublisher, never()).publishConfigCreated(any(Configuration.class));
    }

    @Test
    @DisplayName("Should use default tenant when tenantId is null")
    void createConfig_NullTenantId_UsesDefault() {
        // Given
        when(configRepository.existsByTenantIdAndApplicationNameAndProfileAndConfigKey(
                eq("default"), any(), any(), any())).thenReturn(false);
        when(configMapper.toCreateCommand(any(), eq("default"), any())).thenReturn(createCommand);
        when(configRepository.save(any(Configuration.class))).thenReturn(testConfiguration);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(createResponseDto());

        // When
        configCommandService.createConfig(createRequestDto, null, "admin");

        // Then
        verify(configRepository).save(any(Configuration.class));
    }

    @Test
    @DisplayName("Should use default user when userId is null")
    void createConfig_NullUserId_UsesDefault() {
        // Given
        when(configRepository.existsByTenantIdAndApplicationNameAndProfileAndConfigKey(
                any(), any(), any(), any())).thenReturn(false);
        when(configMapper.toCreateCommand(any(), any(), eq("system"))).thenReturn(createCommand);
        when(configRepository.save(any(Configuration.class))).thenReturn(testConfiguration);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(createResponseDto());

        // When
        configCommandService.createConfig(createRequestDto, "tenant-1", null);

        // Then
        verify(configRepository).save(any(Configuration.class));
    }

    @Test
    @DisplayName("Should update configuration successfully")
    void updateConfig_Success() {
        // Given
        when(configRepository.findById(anyLong())).thenReturn(Optional.of(testConfiguration));
        when(configMapper.toUpdateCommand(anyLong(), any(), any(), any(), any(), any(), any()))
                .thenReturn(updateCommand);
        when(configRepository.save(any(Configuration.class))).thenReturn(testConfiguration);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(createResponseDto());

        // When
        ConfigurationResponseDto result = configCommandService.updateConfig(
                1L, updateRequestDto, "tenant-1", "admin");

        // Then
        assertThat(result).isNotNull();
        verify(configRepository).save(any(Configuration.class));
        verify(historyRepository).save(any(ConfigurationHistory.class));
        verify(eventPublisher).publishConfigUpdated(any(Configuration.class), anyString());
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent configuration")
    void updateConfig_NotFound_ThrowsException() {
        // Given
        when(configRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> configCommandService.updateConfig(
                999L, updateRequestDto, "tenant-1", "admin"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Configuration not found");

        verify(configRepository, never()).save(any(Configuration.class));
    }

    @Test
    @DisplayName("Should throw exception when updating config from different tenant")
    void updateConfig_DifferentTenant_ThrowsException() {
        // Given
        Configuration otherTenantConfig = Configuration.builder()
                .id(1L)
                .tenantId("other-tenant")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .configValue("30000")
                .build();

        when(configRepository.findById(anyLong())).thenReturn(Optional.of(otherTenantConfig));

        // When & Then
        assertThatThrownBy(() -> configCommandService.updateConfig(
                1L, updateRequestDto, "tenant-1", "admin"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Access denied");

        verify(configRepository, never()).save(any(Configuration.class));
    }

    @Test
    @DisplayName("Should delete configuration successfully")
    void deleteConfig_Success() {
        // Given
        when(configRepository.findById(anyLong())).thenReturn(Optional.of(testConfiguration));

        // When
        configCommandService.deleteConfig(1L, "tenant-1", "admin");

        // Then
        verify(configRepository).delete(testConfiguration);
        verify(historyRepository).save(any(ConfigurationHistory.class));
        verify(eventPublisher).publishConfigDeleted(any(Configuration.class));
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent configuration")
    void deleteConfig_NotFound_ThrowsException() {
        // Given
        when(configRepository.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> configCommandService.deleteConfig(
                999L, "tenant-1", "admin"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Configuration not found");

        verify(configRepository, never()).delete(any(Configuration.class));
    }

    @Test
    @DisplayName("Should throw exception when deleting config from different tenant")
    void deleteConfig_DifferentTenant_ThrowsException() {
        // Given
        Configuration otherTenantConfig = Configuration.builder()
                .id(1L)
                .tenantId("other-tenant")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .configValue("30000")
                .build();

        when(configRepository.findById(anyLong())).thenReturn(Optional.of(otherTenantConfig));

        // When & Then
        assertThatThrownBy(() -> configCommandService.deleteConfig(
                1L, "tenant-1", "admin"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Access denied");

        verify(configRepository, never()).delete(any(Configuration.class));
    }

    @Test
    @DisplayName("Should create history entry with CREATE type on config creation")
    void createConfig_CreatesHistoryEntry() {
        // Given
        when(configRepository.existsByTenantIdAndApplicationNameAndProfileAndConfigKey(
                any(), any(), any(), any())).thenReturn(false);
        when(configMapper.toCreateCommand(any(), any(), any())).thenReturn(createCommand);
        when(configRepository.save(any(Configuration.class))).thenReturn(testConfiguration);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(createResponseDto());

        // When
        configCommandService.createConfig(createRequestDto, "tenant-1", "admin");

        // Then
        verify(historyRepository).save(argThat(history ->
                history.getConfigurationId().equals(testConfiguration.getId()) &&
                        history.getChangeType().equals("CREATE") &&
                        history.getOldValue() == null &&
                        history.getNewValue().equals(testConfiguration.getConfigValue())
        ));
    }

    @Test
    @DisplayName("Should create history entry with UPDATE type on config update")
    void updateConfig_CreatesHistoryEntry() {
        // Given
        String oldValue = testConfiguration.getConfigValue();
        when(configRepository.findById(anyLong())).thenReturn(Optional.of(testConfiguration));
        when(configMapper.toUpdateCommand(anyLong(), any(), any(), any(), any(), any(), any()))
                .thenReturn(updateCommand);
        when(configRepository.save(any(Configuration.class))).thenReturn(testConfiguration);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(createResponseDto());

        // When
        configCommandService.updateConfig(1L, updateRequestDto, "tenant-1", "admin");

        // Then
        verify(historyRepository).save(argThat(history ->
                history.getChangeType().equals("UPDATE") &&
                        history.getOldValue().equals(oldValue)
        ));
    }

    @Test
    @DisplayName("Should create history entry with DELETE type on config deletion")
    void deleteConfig_CreatesHistoryEntry() {
        // Given
        when(configRepository.findById(anyLong())).thenReturn(Optional.of(testConfiguration));

        // When
        configCommandService.deleteConfig(1L, "tenant-1", "admin");

        // Then
        verify(historyRepository).save(argThat(history ->
                history.getChangeType().equals("DELETE") &&
                        history.getNewValue() == null &&
                        history.getChangeReason().equals("Configuration deleted")
        ));
    }

    @Test
    @DisplayName("Should publish events for all operations")
    void configOperations_PublishEvents() {
        // Given
        when(configRepository.existsByTenantIdAndApplicationNameAndProfileAndConfigKey(
                any(), any(), any(), any())).thenReturn(false);
        when(configMapper.toCreateCommand(any(), any(), any())).thenReturn(createCommand);
        when(configRepository.findById(anyLong())).thenReturn(Optional.of(testConfiguration));
        when(configMapper.toUpdateCommand(anyLong(), any(), any(), any(), any(), any(), any()))
                .thenReturn(updateCommand);
        when(configRepository.save(any(Configuration.class))).thenReturn(testConfiguration);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(createResponseDto());

        // When - Create
        configCommandService.createConfig(createRequestDto, "tenant-1", "admin");
        verify(eventPublisher).publishConfigCreated(any(Configuration.class));

        // When - Update
        configCommandService.updateConfig(1L, updateRequestDto, "tenant-1", "admin");
        verify(eventPublisher).publishConfigUpdated(any(Configuration.class), anyString());

        // When - Delete
        configCommandService.deleteConfig(1L, "tenant-1", "admin");
        verify(eventPublisher).publishConfigDeleted(any(Configuration.class));
    }

    private ConfigurationResponseDto createResponseDto() {
        return ConfigurationResponseDto.builder()
                .id(1L)
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .configValue("30000")
                .isEncrypted(false)
                .description("Database connection timeout")
                .version(1)
                .isActive(true)
                .createdBy("admin")
                .build();
    }
}
