package com.gogidix.globalbusinessmanagement.scheduledreport.application.service;

import com.gogidix.globalbusinessmanagement.scheduledreport.domain.model.ScheduledReport;
import com.gogidix.globalbusinessmanagement.scheduledreport.domain.repository.ScheduledReportRepository;
import com.gogidix.globalbusinessmanagement.scheduledreport.application.dto.ScheduledReportRequestDto;
import com.gogidix.globalbusinessmanagement.scheduledreport.application.dto.ScheduledReportResponseDto;
import com.gogidix.globalbusinessmanagement.scheduledreport.application.mapper.ScheduledReportMapper;
import com.gogidix.globalbusinessmanagement.scheduledreport.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledReportService {

    private final ScheduledReportRepository repository;
    private final ScheduledReportMapper mapper;

    public ScheduledReportResponseDto create(ScheduledReportRequestDto dto) {
        ScheduledReport entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public ScheduledReportResponseDto getById(String id) {
        ScheduledReport entity = repository.findById(id).orElseThrow(() -> new RuntimeException("ScheduledReport not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<ScheduledReportResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public ScheduledReportResponseDto update(String id, ScheduledReportRequestDto dto) {
        ScheduledReport entity = repository.findById(id).orElseThrow(() -> new RuntimeException("ScheduledReport not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
