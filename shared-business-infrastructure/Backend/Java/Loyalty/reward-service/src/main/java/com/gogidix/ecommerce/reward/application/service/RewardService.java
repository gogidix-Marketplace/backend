package com.gogidix.ecommerce.reward.application.service;

import com.gogidix.ecommerce.reward.application.dto.*;
import com.gogidix.ecommerce.reward.application.mapper.RewardMapper;
import com.gogidix.ecommerce.reward.domain.model.Reward;
import com.gogidix.ecommerce.reward.domain.repository.RewardRepository;
import com.gogidix.ecommerce.reward.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RewardService {

    private final RewardRepository repository;
    private final RewardMapper mapper;

    public RewardService(RewardRepository repository, RewardMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<RewardResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public RewardResponse getById(String id) {
        Reward entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reward not found"));
        return mapper.toResponse(entity);
    }

    public RewardResponse create(CreateRewardRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Reward entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Reward saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public RewardResponse update(String id, UpdateRewardRequest request) {
        Reward entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reward not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Reward saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Reward not found"));
        repository.deleteById(id);
    }
}
