package com.gogidix.globalbusinessmanagement.currencyconversion.application.service;

import com.gogidix.globalbusinessmanagement.currencyconversion.domain.model.CurrencyConversion;
import com.gogidix.globalbusinessmanagement.currencyconversion.domain.repository.CurrencyConversionRepository;
import com.gogidix.globalbusinessmanagement.currencyconversion.application.dto.CurrencyConversionRequestDto;
import com.gogidix.globalbusinessmanagement.currencyconversion.application.dto.CurrencyConversionResponseDto;
import com.gogidix.globalbusinessmanagement.currencyconversion.application.mapper.CurrencyConversionMapper;
import com.gogidix.globalbusinessmanagement.currencyconversion.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyConversionService {

    private final CurrencyConversionRepository repository;
    private final CurrencyConversionMapper mapper;

    public CurrencyConversionResponseDto create(CurrencyConversionRequestDto dto) {
        CurrencyConversion entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public CurrencyConversionResponseDto getById(String id) {
        CurrencyConversion entity = repository.findById(id).orElseThrow(() -> new RuntimeException("CurrencyConversion not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<CurrencyConversionResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public CurrencyConversionResponseDto update(String id, CurrencyConversionRequestDto dto) {
        CurrencyConversion entity = repository.findById(id).orElseThrow(() -> new RuntimeException("CurrencyConversion not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
