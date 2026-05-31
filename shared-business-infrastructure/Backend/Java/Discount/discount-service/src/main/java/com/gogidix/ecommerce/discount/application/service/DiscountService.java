package com.gogidix.ecommerce.discount.application.service;

import com.gogidix.ecommerce.discount.application.dto.*;
import com.gogidix.ecommerce.discount.application.mapper.DiscountMapper;
import com.gogidix.ecommerce.discount.domain.model.Discount;
import com.gogidix.ecommerce.discount.domain.repository.DiscountRepository;
import com.gogidix.ecommerce.discount.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountService {

    private final DiscountRepository repository;
    private final DiscountMapper mapper;

    public DiscountService(DiscountRepository repository, DiscountMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<DiscountResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public DiscountResponse getById(String id) {
        Discount entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found"));
        return mapper.toResponse(entity);
    }

    public DiscountResponse create(CreateDiscountRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Discount entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Discount saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public DiscountResponse update(String id, UpdateDiscountRequest request) {
        Discount entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Discount saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Discount not found"));
        repository.deleteById(id);
    }
}
