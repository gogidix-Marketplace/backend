package com.gogidix.globalbusinessmanagement.localization.application.service;

import com.gogidix.globalbusinessmanagement.localization.domain.model.LocalizationEntry;
import com.gogidix.globalbusinessmanagement.localization.domain.repository.LocalizationEntryRepository;
import com.gogidix.globalbusinessmanagement.localization.application.dto.LocalizationEntryRequestDto;
import com.gogidix.globalbusinessmanagement.localization.application.dto.LocalizationEntryResponseDto;
import com.gogidix.globalbusinessmanagement.localization.application.mapper.LocalizationEntryMapper;
import com.gogidix.globalbusinessmanagement.localization.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalizationEntryService {

    private final LocalizationEntryRepository repository;
    private final LocalizationEntryMapper mapper;

    public LocalizationEntryResponseDto create(LocalizationEntryRequestDto dto) {
        LocalizationEntry entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public LocalizationEntryResponseDto getById(String id) {
        LocalizationEntry entity = repository.findById(id).orElseThrow(() -> new RuntimeException("LocalizationEntry not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<LocalizationEntryResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId()).stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public LocalizationEntryResponseDto update(String id, LocalizationEntryRequestDto dto) {
        LocalizationEntry entity = repository.findById(id).orElseThrow(() -> new RuntimeException("LocalizationEntry not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}
