package com.gogidix.digitalmarketing.brandmanagement.application.service;

import com.gogidix.digitalmarketing.brandmanagement.domain.model.BrandAsset;
import com.gogidix.digitalmarketing.brandmanagement.domain.repository.BrandAssetRepository;
import com.gogidix.digitalmarketing.brandmanagement.application.dto.BrandAssetRequestDto;
import com.gogidix.digitalmarketing.brandmanagement.application.dto.BrandAssetResponseDto;
import com.gogidix.digitalmarketing.brandmanagement.application.mapper.BrandAssetMapper;
import com.gogidix.digitalmarketing.brandmanagement.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandAssetService {

    private final BrandAssetRepository repository;
    private final BrandAssetMapper mapper;

    public BrandAssetResponseDto create(BrandAssetRequestDto dto) {
        BrandAsset entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public BrandAssetResponseDto getById(String id) {
        BrandAsset entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("BrandAsset not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<BrandAssetResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId())
            .stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public BrandAssetResponseDto update(String id, BrandAssetRequestDto dto) {
        BrandAsset entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("BrandAsset not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}