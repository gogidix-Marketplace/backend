package com.gogidix.globalbusinessmanagement.regionalanalytics.application.service;

import com.gogidix.globalbusinessmanagement.regionalanalytics.domain.model.RegionalAnalytics;
import com.gogidix.globalbusinessmanagement.regionalanalytics.domain.repository.RegionalAnalyticsRepository;
import com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto.RegionalAnalyticsRequestDto;
import com.gogidix.globalbusinessmanagement.regionalanalytics.application.dto.RegionalAnalyticsResponseDto;
import com.gogidix.globalbusinessmanagement.regionalanalytics.application.mapper.RegionalAnalyticsMapper;
import com.gogidix.globalbusinessmanagement.regionalanalytics.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegionalAnalyticsService {

    private final RegionalAnalyticsRepository repository;
    private final RegionalAnalyticsMapper mapper;

    public RegionalAnalyticsResponseDto create(RegionalAnalyticsRequestDto dto) {
        RegionalAnalytics entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public RegionalAnalyticsResponseDto getById(String id) {
        RegionalAnalytics entity = repository.findById(id).orElseThrow(() -> new RuntimeException("RegionalAnalytics not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<RegionalAnalyticsResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public RegionalAnalyticsResponseDto update(String id, RegionalAnalyticsRequestDto dto) {
        RegionalAnalytics entity = repository.findById(id).orElseThrow(() -> new RuntimeException("RegionalAnalytics not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
