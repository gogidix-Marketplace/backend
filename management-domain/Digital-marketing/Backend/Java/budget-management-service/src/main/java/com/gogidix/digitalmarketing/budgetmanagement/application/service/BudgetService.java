package com.gogidix.digitalmarketing.budgetmanagement.application.service;

import com.gogidix.digitalmarketing.budgetmanagement.domain.model.Budget;
import com.gogidix.digitalmarketing.budgetmanagement.domain.repository.BudgetRepository;
import com.gogidix.digitalmarketing.budgetmanagement.application.dto.BudgetRequestDto;
import com.gogidix.digitalmarketing.budgetmanagement.application.dto.BudgetResponseDto;
import com.gogidix.digitalmarketing.budgetmanagement.application.mapper.BudgetMapper;
import com.gogidix.digitalmarketing.budgetmanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository repository;
    private final BudgetMapper mapper;

    public BudgetResponseDto create(BudgetRequestDto dto) {
        Budget entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public BudgetResponseDto getById(String id) {
        Budget entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Budget not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<BudgetResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId())
            .stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public BudgetResponseDto update(String id, BudgetRequestDto dto) {
        Budget entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Budget not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}