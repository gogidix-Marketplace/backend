package com.gogidix.globalbusinessmanagement.kafkaingestion.application.service;

import com.gogidix.globalbusinessmanagement.kafkaingestion.domain.model.IngestionJob;
import com.gogidix.globalbusinessmanagement.kafkaingestion.domain.repository.IngestionJobRepository;
import com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto.IngestionJobRequestDto;
import com.gogidix.globalbusinessmanagement.kafkaingestion.application.dto.IngestionJobResponseDto;
import com.gogidix.globalbusinessmanagement.kafkaingestion.application.mapper.IngestionJobMapper;
import com.gogidix.globalbusinessmanagement.kafkaingestion.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class IngestionJobService {

    private final IngestionJobRepository repository;
    private final IngestionJobMapper mapper;

    public IngestionJobResponseDto create(IngestionJobRequestDto dto) {
        IngestionJob entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public IngestionJobResponseDto getById(String id) {
        IngestionJob entity = repository.findById(id).orElseThrow(() -> new RuntimeException("IngestionJob not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<IngestionJobResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public IngestionJobResponseDto update(String id, IngestionJobRequestDto dto) {
        IngestionJob entity = repository.findById(id).orElseThrow(() -> new RuntimeException("IngestionJob not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
