package com.gogidix.globalbusinessmanagement.reportbuilder.application.service;

import com.gogidix.globalbusinessmanagement.reportbuilder.domain.model.Report;
import com.gogidix.globalbusinessmanagement.reportbuilder.domain.repository.ReportRepository;
import com.gogidix.globalbusinessmanagement.reportbuilder.application.dto.ReportRequestDto;
import com.gogidix.globalbusinessmanagement.reportbuilder.application.dto.ReportResponseDto;
import com.gogidix.globalbusinessmanagement.reportbuilder.application.mapper.ReportMapper;
import com.gogidix.globalbusinessmanagement.reportbuilder.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository repository;
    private final ReportMapper mapper;

    public ReportResponseDto create(ReportRequestDto dto) {
        Report entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public ReportResponseDto getById(String id) {
        Report entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Report not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<ReportResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public ReportResponseDto update(String id, ReportRequestDto dto) {
        Report entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Report not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
