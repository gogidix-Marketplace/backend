package com.gogidix.centralconfiguration.configserver.application.service;

import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigHistoryResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigurationResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.PagedConfigResponseDto;
import com.gogidix.centralconfiguration.configserver.application.mapper.ConfigMapper;
import com.gogidix.centralconfiguration.configserver.domain.model.Configuration;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationHistory;
import com.gogidix.centralconfiguration.configserver.domain.port.in.SearchConfigsQuery;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationHistoryRepository;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfigQueryService Tests")
class ConfigQueryServiceTest {

    @Mock
    private ConfigurationRepository configRepository;

    @Mock
    private ConfigurationHistoryRepository historyRepository;

    @Mock
    private ConfigMapper configMapper;

    @InjectMocks
    private ConfigQueryService configQueryService;

    private Configuration testConfiguration;
    private ConfigurationHistory testHistory;
    private ConfigurationResponseDto responseDto;
    private ConfigHistoryResponseDto historyResponseDto;

    @BeforeEach
    void setUp() {
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
                .createdAt(LocalDateTime.now())
                .build();

        testHistory = ConfigurationHistory.builder()
                .id(1L)
                .configurationId(1L)
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .oldValue("20000")
                .newValue("30000")
                .version(1)
                .changeType("UPDATE")
                .changedBy("admin")
                .createdAt(LocalDateTime.now())
                .build();

        responseDto = ConfigurationResponseDto.builder()
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

        historyResponseDto = ConfigHistoryResponseDto.builder()
                .id(1L)
                .configurationId(1L)
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .oldValue("20000")
                .newValue("30000")
                .version(1)
                .changeType("UPDATE")
                .changedBy("admin")
                .build();
    }

    @Test
    @DisplayName("Should get configuration by ID successfully")
    void getConfigById_Success() {
        // Given
        when(configRepository.findById(1L)).thenReturn(Optional.of(testConfiguration));
        when(configMapper.toResponseDto(testConfiguration)).thenReturn(responseDto);

        // When
        ConfigurationResponseDto result = configQueryService.getConfigById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getApplicationName()).isEqualTo("payment-service");
        assertThat(result.getConfigKey()).isEqualTo("database.timeout");

        verify(configRepository).findById(1L);
        verify(configMapper).toResponseDto(testConfiguration);
    }

    @Test
    @DisplayName("Should throw exception when configuration by ID not found")
    void getConfigById_NotFound_ThrowsException() {
        // Given
        when(configRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> configQueryService.getConfigById(999L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Configuration not found");

        verify(configRepository).findById(999L);
        verify(configMapper, never()).toResponseDto(any());
    }

    @Test
    @DisplayName("Should get configuration by composite key successfully")
    void getConfigByKey_Success() {
        // Given
        when(configRepository.findByTenantIdAndApplicationNameAndProfileAndConfigKey(
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(testConfiguration));
        when(configMapper.toResponseDto(testConfiguration)).thenReturn(responseDto);

        // When
        ConfigurationResponseDto result = configQueryService.getConfigByKey(
                "tenant-1", "payment-service", "prod", "database.timeout");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getTenantId()).isEqualTo("tenant-1");
        assertThat(result.getApplicationName()).isEqualTo("payment-service");
        assertThat(result.getProfile()).isEqualTo("prod");
        assertThat(result.getConfigKey()).isEqualTo("database.timeout");

        verify(configRepository).findByTenantIdAndApplicationNameAndProfileAndConfigKey(
                "tenant-1", "payment-service", "prod", "database.timeout");
    }

    @Test
    @DisplayName("Should use default tenant when tenantId is null for get by key")
    void getConfigByKey_NullTenantId_UsesDefault() {
        // Given
        when(configRepository.findByTenantIdAndApplicationNameAndProfileAndConfigKey(
                eq("default"), anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(testConfiguration));
        when(configMapper.toResponseDto(testConfiguration)).thenReturn(responseDto);

        // When
        configQueryService.getConfigByKey(null, "payment-service", "prod", "database.timeout");

        // Then
        verify(configRepository).findByTenantIdAndApplicationNameAndProfileAndConfigKey(
                eq("default"), anyString(), anyString(), anyString());
    }

    @Test
    @DisplayName("Should throw exception when configuration by key not found")
    void getConfigByKey_NotFound_ThrowsException() {
        // Given
        when(configRepository.findByTenantIdAndApplicationNameAndProfileAndConfigKey(
                anyString(), anyString(), anyString(), anyString()))
                .thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> configQueryService.getConfigByKey(
                "tenant-1", "unknown-app", "prod", "unknown.key"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Configuration not found");
    }

    @Test
    @DisplayName("Should get configurations by application and profile")
    void getConfigsByApplicationAndProfile_Success() {
        // Given
        List<Configuration> configs = Arrays.asList(
                testConfiguration,
                Configuration.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .applicationName("payment-service")
                        .profile("prod")
                        .configKey("database.pool.size")
                        .configValue("50")
                        .build()
        );

        when(configRepository.findByTenantIdAndApplicationNameAndProfile(
                anyString(), anyString(), anyString())).thenReturn(configs);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(responseDto);

        // When
        List<ConfigurationResponseDto> result = configQueryService.getConfigsByApplicationAndProfile(
                "tenant-1", "payment-service", "prod");

        // Then
        assertThat(result).hasSize(2);
        verify(configRepository).findByTenantIdAndApplicationNameAndProfile(
                "tenant-1", "payment-service", "prod");
    }

    @Test
    @DisplayName("Should return empty list when no configurations found for application")
    void getConfigsByApplicationAndProfile_EmptyList() {
        // Given
        when(configRepository.findByTenantIdAndApplicationNameAndProfile(
                anyString(), anyString(), anyString())).thenReturn(List.of());

        // When
        List<ConfigurationResponseDto> result = configQueryService.getConfigsByApplicationAndProfile(
                "tenant-1", "unknown-app", "prod");

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Should search configurations with pagination")
    void searchConfigs_Success() {
        // Given
        List<Configuration> configs = Arrays.asList(
                testConfiguration,
                Configuration.builder()
                        .id(2L)
                        .tenantId("tenant-1")
                        .applicationName("payment-service")
                        .profile("prod")
                        .configKey("another.key")
                        .configValue("value2")
                        .build()
        );

        SearchConfigsQuery query = SearchConfigsQuery.builder()
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profiles(List.of("prod"))
                .isActive(true)
                .page(0)
                .size(20)
                .sortBy("createdAt")
                .sortDirection("DESC")
                .build();

        when(configRepository.searchByTenantIdAndApplicationName(
                anyString(), anyString(), anyString(), anyBoolean())).thenReturn(configs);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(responseDto);

        // When
        PagedConfigResponseDto<ConfigurationResponseDto> result = configQueryService.searchConfigs(query);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getPage()).isEqualTo(0);
        assertThat(result.getSize()).isEqualTo(20);
        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.getIsFirst()).isTrue();
        assertThat(result.getIsLast()).isTrue();
    }

    @Test
    @DisplayName("Should calculate pagination correctly")
    void searchConfigs_Pagination_CalculatesCorrectly() {
        // Given - Create 25 configs to test pagination
        List<Configuration> configs = Arrays.asList(
                testConfiguration,
                Configuration.builder().id(2L).tenantId("tenant-1").build(),
                Configuration.builder().id(3L).tenantId("tenant-1").build(),
                Configuration.builder().id(4L).tenantId("tenant-1").build(),
                Configuration.builder().id(5L).tenantId("tenant-1").build()
        );

        SearchConfigsQuery query = SearchConfigsQuery.builder()
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .page(1)
                .size(2)
                .sortBy("createdAt")
                .sortDirection("DESC")
                .build();

        when(configRepository.searchByTenantIdAndApplicationName(
                anyString(), anyString(), any(), any())).thenReturn(configs);
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(responseDto);

        // When
        PagedConfigResponseDto<ConfigurationResponseDto> result = configQueryService.searchConfigs(query);

        // Then
        assertThat(result.getTotalElements()).isEqualTo(5);
        assertThat(result.getTotalPages()).isEqualTo(3);
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getIsFirst()).isFalse();
        assertThat(result.getIsLast()).isFalse();
    }

    @Test
    @DisplayName("Should get configuration history by config ID")
    void getConfigHistory_Success() {
        // Given
        List<ConfigurationHistory> historyList = Arrays.asList(
                testHistory,
                ConfigurationHistory.builder()
                        .id(2L)
                        .configurationId(1L)
                        .changeType("CREATE")
                        .changedBy("admin")
                        .build()
        );

        when(historyRepository.findByConfigurationId(1L)).thenReturn(historyList);
        when(configMapper.toHistoryResponseDto(any(ConfigurationHistory.class))).thenReturn(historyResponseDto);

        // When
        List<ConfigHistoryResponseDto> result = configQueryService.getConfigHistory(1L);

        // Then
        assertThat(result).hasSize(2);
        verify(historyRepository).findByConfigurationId(1L);
    }

    @Test
    @DisplayName("Should get configuration history by application")
    void getConfigHistoryByApplication_Success() {
        // Given
        List<ConfigurationHistory> historyList = List.of(testHistory);

        when(historyRepository.findByTenantIdAndApplicationName(
                anyString(), anyString(), anyInt(), anyInt())).thenReturn(historyList);
        when(configMapper.toHistoryResponseDto(any(ConfigurationHistory.class))).thenReturn(historyResponseDto);

        // When
        List<ConfigHistoryResponseDto> result = configQueryService.getConfigHistoryByApplication(
                "tenant-1", "payment-service", 0, 20);

        // Then
        assertThat(result).hasSize(1);
        verify(historyRepository).findByTenantIdAndApplicationName(
                "tenant-1", "payment-service", 0, 20);
    }

    @Test
    @DisplayName("Should use default tenant when searching with null tenant ID")
    void searchConfigs_NullTenantId_UsesDefault() {
        // Given
        SearchConfigsQuery query = SearchConfigsQuery.builder()
                .tenantId(null)
                .applicationName("payment-service")
                .page(0)
                .size(20)
                .build();

        when(configRepository.searchByTenantIdAndApplicationName(
                eq("default"), anyString(), any(), any()))
                .thenReturn(List.of(testConfiguration));
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(responseDto);

        // When
        configQueryService.searchConfigs(query);

        // Then
        verify(configRepository).searchByTenantIdAndApplicationName(
                eq("default"), anyString(), any(), any());
    }

    @Test
    @DisplayName("Should handle empty search results")
    void searchConfigs_EmptyResults() {
        // Given
        SearchConfigsQuery query = SearchConfigsQuery.builder()
                .tenantId("tenant-1")
                .applicationName("unknown-app")
                .page(0)
                .size(20)
                .build();

        when(configRepository.searchByTenantIdAndApplicationName(
                anyString(), anyString(), any(), any())).thenReturn(List.of());

        // When
        PagedConfigResponseDto<ConfigurationResponseDto> result = configQueryService.searchConfigs(query);

        // Then
        assertThat(result.getItems()).isEmpty();
        assertThat(result.getTotalElements()).isEqualTo(0);
        assertThat(result.getTotalPages()).isEqualTo(0);
        assertThat(result.getIsFirst()).isTrue();
        assertThat(result.getIsLast()).isTrue();
    }

    @Test
    @DisplayName("Should use first profile when multiple profiles provided")
    void searchConfigs_MultipleProfiles_UsesFirst() {
        // Given
        List<String> profiles = Arrays.asList("prod", "staging", "dev");
        SearchConfigsQuery query = SearchConfigsQuery.builder()
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profiles(profiles)
                .page(0)
                .size(20)
                .build();

        when(configRepository.searchByTenantIdAndApplicationName(
                anyString(), anyString(), eq("prod"), any()))
                .thenReturn(List.of(testConfiguration));
        when(configMapper.toResponseDto(any(Configuration.class))).thenReturn(responseDto);

        // When
        configQueryService.searchConfigs(query);

        // Then
        verify(configRepository).searchByTenantIdAndApplicationName(
                anyString(), anyString(), eq("prod"), any());
    }
}
