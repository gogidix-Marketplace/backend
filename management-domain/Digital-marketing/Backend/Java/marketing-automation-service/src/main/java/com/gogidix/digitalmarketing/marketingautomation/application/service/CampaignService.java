package com.gogidix.digitalmarketing.marketingautomation.application.service;

import com.gogidix.digitalmarketing.marketingautomation.domain.model.Campaign;
import com.gogidix.digitalmarketing.marketingautomation.domain.repository.CampaignRepository;
import com.gogidix.digitalmarketing.marketingautomation.application.dto.CampaignRequestDto;
import com.gogidix.digitalmarketing.marketingautomation.application.dto.CampaignResponseDto;
import com.gogidix.digitalmarketing.marketingautomation.application.mapper.CampaignMapper;
import com.gogidix.digitalmarketing.marketingautomation.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository repository;
    private final CampaignMapper mapper;

    public CampaignResponseDto create(CampaignRequestDto dto) {
        Campaign entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public CampaignResponseDto getById(String id) {
        Campaign entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Campaign not found: " + id));
        return mapper.toResponseDto(entity);
    }

    public List<CampaignResponseDto> getAll() {
        return repository.findByTenantId(RequestContextHolder.getTenantId())
            .stream().map(mapper::toResponseDto).collect(Collectors.toList());
    }

    public CampaignResponseDto update(String id, CampaignRequestDto dto) {
        Campaign entity = repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Campaign not found: " + id));
        entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toResponseDto(entity);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}