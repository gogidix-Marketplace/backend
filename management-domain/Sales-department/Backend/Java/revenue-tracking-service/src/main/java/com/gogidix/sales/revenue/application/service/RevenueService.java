package com.gogidix.sales.revenue.application.service;

import com.gogidix.sales.revenue.domain.model.Revenue;
import com.gogidix.sales.revenue.domain.repository.RevenueRepository;
import com.gogidix.sales.revenue.application.dto.RevenueRequestDto;
import com.gogidix.sales.revenue.application.dto.RevenueResponseDto;
import com.gogidix.sales.revenue.application.mapper.RevenueMapper;
import com.gogidix.sales.revenue.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RevenueService {

    private final RevenueRepository repository;
    private final RevenueMapper mapper;

    public RevenueResponseDto create(RevenueRequestDto dto) {
        Revenue entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public RevenueResponseDto getById(String id) {
        Revenue entity = repository.findById(id).orElseThrow(() -> new RuntimeException("Revenue not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<RevenueResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public RevenueResponseDto update(String id, RevenueRequestDto dto) {
        repository.findById(id).orElseThrow(() -> new RuntimeException("Revenue not found: " + id));
        Revenue entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) { repository.deleteById(id); }
}