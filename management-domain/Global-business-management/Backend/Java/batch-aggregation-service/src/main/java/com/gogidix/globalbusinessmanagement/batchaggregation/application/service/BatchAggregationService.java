package com.gogidix.globalbusinessmanagement.batchaggregation.application.service;

import com.gogidix.globalbusinessmanagement.batchaggregation.domain.model.BatchAggregation;
import com.gogidix.globalbusinessmanagement.batchaggregation.domain.repository.BatchAggregationRepository;
import com.gogidix.globalbusinessmanagement.batchaggregation.application.dto.BatchAggregationRequestDto;
import com.gogidix.globalbusinessmanagement.batchaggregation.application.dto.BatchAggregationResponseDto;
import com.gogidix.globalbusinessmanagement.batchaggregation.application.mapper.BatchAggregationMapper;
import com.gogidix.globalbusinessmanagement.batchaggregation.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatchAggregationService {

    private final BatchAggregationRepository repository;
    private final BatchAggregationMapper mapper;

    public BatchAggregationResponseDto create(BatchAggregationRequestDto dto) {
        BatchAggregation entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public BatchAggregationResponseDto getById(String id) {
        BatchAggregation entity = repository.findById(id).orElseThrow(() -> new RuntimeException("BatchAggregation not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<BatchAggregationResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public BatchAggregationResponseDto update(String id, BatchAggregationRequestDto dto) {
        BatchAggregation entity = repository.findById(id).orElseThrow(() -> new RuntimeException("BatchAggregation not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
