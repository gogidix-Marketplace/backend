package com.gogidix.ecommerce.giftcard.application.service;

import com.gogidix.ecommerce.giftcard.application.dto.*;
import com.gogidix.ecommerce.giftcard.application.mapper.GiftCardMapper;
import com.gogidix.ecommerce.giftcard.domain.model.GiftCard;
import com.gogidix.ecommerce.giftcard.domain.repository.GiftCardRepository;
import com.gogidix.ecommerce.giftcard.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiftCardService {

    private final GiftCardRepository repository;
    private final GiftCardMapper mapper;

    public GiftCardService(GiftCardRepository repository, GiftCardMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<GiftCardResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public GiftCardResponse getById(String id) {
        GiftCard entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GiftCard not found"));
        return mapper.toResponse(entity);
    }

    public GiftCardResponse create(CreateGiftCardRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        GiftCard entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        GiftCard saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public GiftCardResponse update(String id, UpdateGiftCardRequest request) {
        GiftCard entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GiftCard not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        GiftCard saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("GiftCard not found"));
        repository.deleteById(id);
    }
}
