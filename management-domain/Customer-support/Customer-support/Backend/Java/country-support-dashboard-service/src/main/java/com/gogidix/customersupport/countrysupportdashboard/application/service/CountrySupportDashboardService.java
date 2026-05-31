package com.gogidix.customersupport.countrysupportdashboard.application.service;

import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsRequestDto;
import com.gogidix.customersupport.countrysupportdashboard.application.dto.CountrySpecificMetricsResponseDto;
import com.gogidix.customersupport.countrysupportdashboard.application.dto.RegionalTicketStatsResponseDto;
import com.gogidix.customersupport.countrysupportdashboard.application.mapper.CountrySpecificMetricsMapper;
import com.gogidix.customersupport.countrysupportdashboard.application.mapper.RegionalTicketStatsMapper;
import com.gogidix.customersupport.countrysupportdashboard.domain.model.CountrySpecificMetrics;
import com.gogidix.customersupport.countrysupportdashboard.domain.model.RegionalTicketStats;
import com.gogidix.customersupport.countrysupportdashboard.domain.repository.CountrySpecificMetricsRepository;
import com.gogidix.customersupport.countrysupportdashboard.domain.repository.RegionalTicketStatsRepository;
import com.gogidix.customersupport.countrysupportdashboard.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountrySupportDashboardService {

    private final CountrySpecificMetricsRepository countrySpecificMetricsRepository;
    private final RegionalTicketStatsRepository regionalTicketStatsRepository;
    private final CountrySpecificMetricsMapper countrySpecificMetricsMapper;
    private final RegionalTicketStatsMapper regionalTicketStatsMapper;

    public List<CountrySpecificMetricsResponseDto> getAllCountryMetrics() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all country specific metrics for tenant: {}", tenantId);
        return countrySpecificMetricsRepository.findByTenantId(tenantId).stream()
                .map(countrySpecificMetricsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public CountrySpecificMetricsResponseDto getCountryMetricsById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching country metrics with id: {} for tenant: {}", id, tenantId);
        CountrySpecificMetrics metrics = countrySpecificMetricsRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Country specific metrics not found with id: " + id));
        return countrySpecificMetricsMapper.toResponseDto(metrics);
    }

    public List<CountrySpecificMetricsResponseDto> getMetricsByCountryCode(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching country metrics for country code: {} and tenant: {}", countryCode, tenantId);
        return countrySpecificMetricsRepository.findByTenantIdAndCountryCode(tenantId, countryCode).stream()
                .map(countrySpecificMetricsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<CountrySpecificMetricsResponseDto> getMetricsByCountryCodeAndDateRange(
            String countryCode, LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching country metrics for country: {} from {} to {} for tenant: {}",
                countryCode, startDate, endDate, tenantId);
        return countrySpecificMetricsRepository.findByTenantIdAndCountryCodeAndMetricDateBetween(
                tenantId, countryCode, startDate, endDate).stream()
                .map(countrySpecificMetricsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public CountrySpecificMetricsResponseDto getLatestMetricsByCountryCode(String countryCode) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest country metrics for country: {} and tenant: {}", countryCode, tenantId);
        return countrySpecificMetricsRepository.findFirstByTenantIdAndCountryCodeOrderByMetricDateDesc(tenantId, countryCode)
                .map(countrySpecificMetricsMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("No metrics found for country code: " + countryCode));
    }

    public List<CountrySpecificMetricsResponseDto> getMetricsByRegion(String region) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching country metrics for region: {} and tenant: {}", region, tenantId);
        return countrySpecificMetricsRepository.findByTenantIdAndRegion(tenantId, region).stream()
                .map(countrySpecificMetricsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CountrySpecificMetricsResponseDto createMetrics(CountrySpecificMetricsRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Creating new country specific metrics for tenant: {}", tenantId);

        CountrySpecificMetrics metrics = countrySpecificMetricsMapper.toEntity(request, tenantId);
        CountrySpecificMetrics saved = countrySpecificMetricsRepository.save(metrics);
        log.info("Created country specific metrics with id: {} for tenant: {}", saved.getId(), tenantId);
        return countrySpecificMetricsMapper.toResponseDto(saved);
    }

    @Transactional
    public CountrySpecificMetricsResponseDto updateMetrics(String id, CountrySpecificMetricsRequestDto request) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Updating country metrics with id: {} for tenant: {}", id, tenantId);

        CountrySpecificMetrics existing = countrySpecificMetricsRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Country specific metrics not found with id: " + id));

        countrySpecificMetricsMapper.updateEntityFromDto(request, existing);
        CountrySpecificMetrics updated = countrySpecificMetricsRepository.save(existing);
        log.info("Updated country specific metrics with id: {} for tenant: {}", id, tenantId);
        return countrySpecificMetricsMapper.toResponseDto(updated);
    }

    @Transactional
    public void deleteMetrics(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Deleting country metrics with id: {} for tenant: {}", id, tenantId);

        if (!countrySpecificMetricsRepository.existsById(id)) {
            throw new IllegalArgumentException("Country specific metrics not found with id: " + id);
        }
        countrySpecificMetricsRepository.deleteByTenantIdAndId(tenantId, id);
        log.info("Deleted country specific metrics with id: {} for tenant: {}", id, tenantId);
    }

    public List<String> getDistinctCountryCodes() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching distinct country codes for tenant: {}", tenantId);
        return countrySpecificMetricsRepository.findDistinctCountryCodesByTenantId(tenantId);
    }

    public List<String> getDistinctRegions() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching distinct regions for tenant: {}", tenantId);
        return countrySpecificMetricsRepository.findDistinctRegionsByTenantId(tenantId);
    }

    public List<RegionalTicketStatsResponseDto> getAllRegionalStats() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching all regional ticket stats for tenant: {}", tenantId);
        return regionalTicketStatsRepository.findByTenantIdOrderByStatDateDesc(tenantId).stream()
                .map(regionalTicketStatsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public RegionalTicketStatsResponseDto getRegionalStatsById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional stats with id: {} for tenant: {}", id, tenantId);
        RegionalTicketStats stats = regionalTicketStatsRepository.findByTenantIdAndId(tenantId, id)
                .orElseThrow(() -> new IllegalArgumentException("Regional ticket stats not found with id: " + id));
        return regionalTicketStatsMapper.toResponseDto(stats);
    }

    public List<RegionalTicketStatsResponseDto> getStatsByRegion(String regionName) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional stats for region: {} and tenant: {}", regionName, tenantId);
        return regionalTicketStatsRepository.findByTenantIdAndRegionName(tenantId, regionName).stream()
                .map(regionalTicketStatsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public RegionalTicketStatsResponseDto getLatestStatsByRegion(String regionName) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching latest regional stats for region: {} and tenant: {}", regionName, tenantId);
        return regionalTicketStatsRepository.findFirstByTenantIdAndRegionNameOrderByStatDateDesc(tenantId, regionName)
                .map(regionalTicketStatsMapper::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("No stats found for region: " + regionName));
    }

    public List<RegionalTicketStatsResponseDto> getStatsByDateRange(LocalDate startDate, LocalDate endDate) {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching regional stats from {} to {} for tenant: {}", startDate, endDate, tenantId);
        return regionalTicketStatsRepository.findByTenantIdAndStatDateBetween(tenantId, startDate, endDate).stream()
                .map(regionalTicketStatsMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<String> getDistinctRegionNames() {
        String tenantId = RequestContextHolder.getTenantId();
        log.debug("Fetching distinct region names for tenant: {}", tenantId);
        return regionalTicketStatsRepository.findDistinctRegionNamesByTenantId(tenantId);
    }
}
