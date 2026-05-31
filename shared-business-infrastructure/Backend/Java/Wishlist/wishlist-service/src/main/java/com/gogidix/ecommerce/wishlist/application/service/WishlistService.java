package com.gogidix.ecommerce.wishlist.application.service;

import com.gogidix.ecommerce.wishlist.application.dto.*;
import com.gogidix.ecommerce.wishlist.application.mapper.WishlistMapper;
import com.gogidix.ecommerce.wishlist.domain.model.Wishlist;
import com.gogidix.ecommerce.wishlist.domain.repository.WishlistRepository;
import com.gogidix.ecommerce.wishlist.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository repository;
    private final WishlistMapper mapper;

    public WishlistService(WishlistRepository repository, WishlistMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<WishlistResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public WishlistResponse getById(String id) {
        Wishlist entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist not found"));
        return mapper.toResponse(entity);
    }

    public WishlistResponse create(CreateWishlistRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Wishlist entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Wishlist saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public WishlistResponse update(String id, UpdateWishlistRequest request) {
        Wishlist entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Wishlist saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist not found"));
        repository.deleteById(id);
    }
}
