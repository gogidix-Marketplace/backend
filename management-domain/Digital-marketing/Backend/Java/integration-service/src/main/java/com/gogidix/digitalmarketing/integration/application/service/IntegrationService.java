package com.gogidix.digitalmarketing.integration.application.service;

import com.gogidix.digitalmarketing.integration.domain.model.Integration;
import com.gogidix.digitalmarketing.integration.domain.repository.IntegrationRepository;
import com.gogidix.digitalmarketing.integration.application.dto.IntegrationRequestDto;
import com.gogidix.digitalmarketing.integration.application.dto.IntegrationResponseDto;
import com.gogidix.digitalmarketing.integration.application.mapper.IntegrationMapper;
import com.gogidix.digitalmarketing.integration.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IntegrationService {

    private final IntegrationRepository repository;
    private final IntegrationMapper mapper;

    public IntegrationResponseDto create(IntegrationRequestDto dto) {
        Integration entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public IntegrationResponseDto getById(String id) {
        Integration entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Integration not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<IntegrationResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId())
            .stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public IntegrationResponseDto update(String id, IntegrationRequestDto dto) {
        Integration entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Integration not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}