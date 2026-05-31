package com.gogidix.digitalmarketing.seo.application.service;

import com.gogidix.digitalmarketing.seo.domain.model.Keyword;
import com.gogidix.digitalmarketing.seo.domain.repository.KeywordRepository;
import com.gogidix.digitalmarketing.seo.application.dto.KeywordRequestDto;
import com.gogidix.digitalmarketing.seo.application.dto.KeywordResponseDto;
import com.gogidix.digitalmarketing.seo.application.mapper.KeywordMapper;
import com.gogidix.digitalmarketing.seo.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository repository;
    private final KeywordMapper mapper;

    public KeywordResponseDto create(KeywordRequestDto dto) {
        Keyword entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public KeywordResponseDto getById(String id) {
        Keyword entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Keyword not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<KeywordResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId())
            .stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public KeywordResponseDto update(String id, KeywordRequestDto dto) {
        Keyword entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Keyword not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}