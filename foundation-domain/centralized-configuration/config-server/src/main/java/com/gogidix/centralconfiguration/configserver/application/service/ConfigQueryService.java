package com.gogidix.centralconfiguration.configserver.application.service;

import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigHistoryResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.ConfigurationResponseDto;
import com.gogidix.centralconfiguration.configserver.application.dto.response.PagedConfigResponseDto;
import com.gogidix.centralconfiguration.configserver.application.mapper.ConfigMapper;
import com.gogidix.centralconfiguration.configserver.domain.model.Configuration;
import com.gogidix.centralconfiguration.configserver.domain.model.ConfigurationHistory;
import com.gogidix.centralconfiguration.configserver.domain.port.in.GetConfigQuery;
import com.gogidix.centralconfiguration.configserver.domain.port.in.SearchConfigsQuery;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationHistoryRepository;
import com.gogidix.centralconfiguration.configserver.domain.repository.ConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * CQRS Query Handler for Configuration operations.
 * Handles all read operations for configuration entries.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ConfigQueryService {

    private final ConfigurationRepository configRepository;
    private final ConfigurationHistoryRepository historyRepository;
    private final ConfigMapper configMapper;

    private static final String DEFAULT_TENANT_ID = "default";

    /**
     * Get configuration by ID
     */
    public ConfigurationResponseDto getConfigById(Long configId) {
        log.debug("Getting config by ID: {}", configId);

        Configuration config = configRepository.findById(configId)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found: " + configId));

        return configMapper.toResponseDto(config);
    }

    /**
     * Get configuration by composite key
     */
    public ConfigurationResponseDto getConfigByKey(String tenantId, String applicationName,
                                                   String profile, String configKey) {
        log.debug("Getting config: tenant={}, app={}, profile={}, key={}",
                tenantId, applicationName, profile, configKey);

        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        Configuration config = configRepository.findByTenantIdAndApplicationNameAndProfileAndConfigKey(
                effectiveTenantId, applicationName, profile, configKey)
                .orElseThrow(() -> new IllegalArgumentException("Configuration not found"));

        return configMapper.toResponseDto(config);
    }

    /**
     * Get all configurations for an application and profile
     */
    public List<ConfigurationResponseDto> getConfigsByApplicationAndProfile(
            String tenantId, String applicationName, String profile) {
        log.debug("Getting configs: tenant={}, app={}, profile={}", tenantId, applicationName, profile);

        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        List<Configuration> configs = configRepository.findByTenantIdAndApplicationNameAndProfile(
                effectiveTenantId, applicationName, profile);

        return configs.stream()
                .map(configMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Search configurations with filters
     */
    public PagedConfigResponseDto<ConfigurationResponseDto> searchConfigs(SearchConfigsQuery query) {
        log.debug("Searching configs: tenant={}, app={}, page={}",
                query.tenantId(), query.applicationName(), query.page());

        String effectiveTenantId = query.tenantId() != null ? query.tenantId() : DEFAULT_TENANT_ID;

        List<Configuration> configs = configRepository.searchByTenantIdAndApplicationName(
                effectiveTenantId,
                query.applicationName(),
                query.profiles() != null && !query.profiles().isEmpty() ? query.profiles().get(0) : null,
                query.isActive());

        int totalElements = configs.size();
        int totalPages = (int) Math.ceil((double) totalElements / query.size());

        int fromIndex = query.page() * query.size();
        int toIndex = Math.min(fromIndex + query.size(), totalElements);

        List<Configuration> pagedConfigs = configs.subList(
                Math.max(0, fromIndex),
                Math.max(0, toIndex));

        List<ConfigurationResponseDto> responseDtos = pagedConfigs.stream()
                .map(configMapper::toResponseDto)
                .collect(Collectors.toList());

        return PagedConfigResponseDto.<ConfigurationResponseDto>builder()
                .items(responseDtos)
                .page(query.page())
                .size(query.size())
                .totalElements((long) totalElements)
                .totalPages(totalPages)
                .isFirst(query.page() == 0)
                .isLast(query.page() >= totalPages - 1)
                .build();
    }

    /**
     * Get configuration history
     */
    public List<ConfigHistoryResponseDto> getConfigHistory(Long configId) {
        log.debug("Getting config history: configId={}", configId);

        List<ConfigurationHistory> history = historyRepository.findByConfigurationId(configId);

        return history.stream()
                .map(configMapper::toHistoryResponseDto)
                .collect(Collectors.toList());
    }

    /**
     * Get configuration history by application
     */
    public List<ConfigHistoryResponseDto> getConfigHistoryByApplication(
            String tenantId, String applicationName, Integer page, Integer size) {
        log.debug("Getting config history: tenant={}, app={}", tenantId, applicationName);

        String effectiveTenantId = tenantId != null ? tenantId : DEFAULT_TENANT_ID;

        List<ConfigurationHistory> history = historyRepository.findByTenantIdAndApplicationName(
                effectiveTenantId, applicationName, page, size);

        return history.stream()
                .map(configMapper::toHistoryResponseDto)
                .collect(Collectors.toList());
    }
}
