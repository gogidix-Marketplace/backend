package com.gogidix.digitalmarketing.contentmanagement.application.service;

import com.gogidix.digitalmarketing.contentmanagement.domain.model.ContentPiece;
import com.gogidix.digitalmarketing.contentmanagement.domain.repository.ContentPieceRepository;
import com.gogidix.digitalmarketing.contentmanagement.application.dto.ContentPieceRequestDto;
import com.gogidix.digitalmarketing.contentmanagement.application.dto.ContentPieceResponseDto;
import com.gogidix.digitalmarketing.contentmanagement.application.mapper.ContentPieceMapper;
import com.gogidix.digitalmarketing.contentmanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContentPieceService {

    private final ContentPieceRepository repository;
    private final ContentPieceMapper mapper;

    public ContentPieceResponseDto create(ContentPieceRequestDto dto) {
        ContentPiece entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public ContentPieceResponseDto getById(String id) {
        ContentPiece entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("ContentPiece not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<ContentPieceResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId())
            .stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public ContentPieceResponseDto update(String id, ContentPieceRequestDto dto) {
        ContentPiece entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("ContentPiece not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}