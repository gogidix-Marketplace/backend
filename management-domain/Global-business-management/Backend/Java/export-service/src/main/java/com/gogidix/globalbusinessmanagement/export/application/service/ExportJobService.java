package com.gogidix.globalbusinessmanagement.export.application.service;

import com.gogidix.globalbusinessmanagement.export.domain.model.ExportJob;
import com.gogidix.globalbusinessmanagement.export.domain.repository.ExportJobRepository;
import com.gogidix.globalbusinessmanagement.export.application.dto.ExportJobRequestDto;
import com.gogidix.globalbusinessmanagement.export.application.dto.ExportJobResponseDto;
import com.gogidix.globalbusinessmanagement.export.application.mapper.ExportJobMapper;
import com.gogidix.globalbusinessmanagement.export.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportJobService {

    private final ExportJobRepository repository;
    private final ExportJobMapper mapper;

    public ExportJobResponseDto create(ExportJobRequestDto dto) {
        ExportJob entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public ExportJobResponseDto getById(String id) {
        ExportJob entity = repository.findById(id).orElseThrow(() -> new RuntimeException("ExportJob not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<ExportJobResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public ExportJobResponseDto update(String id, ExportJobRequestDto dto) {
        ExportJob entity = repository.findById(id).orElseThrow(() -> new RuntimeException("ExportJob not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
