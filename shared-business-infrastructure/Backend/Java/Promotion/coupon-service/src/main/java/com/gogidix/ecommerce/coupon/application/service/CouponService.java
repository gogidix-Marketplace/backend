package com.gogidix.ecommerce.coupon.application.service;

import com.gogidix.ecommerce.coupon.application.dto.*;
import com.gogidix.ecommerce.coupon.application.mapper.CouponMapper;
import com.gogidix.ecommerce.coupon.domain.model.Coupon;
import com.gogidix.ecommerce.coupon.domain.repository.CouponRepository;
import com.gogidix.ecommerce.coupon.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {

    private final CouponRepository repository;
    private final CouponMapper mapper;

    public CouponService(CouponRepository repository, CouponMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<CouponResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public CouponResponse getById(String id) {
        Coupon entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
        return mapper.toResponse(entity);
    }

    public CouponResponse create(CreateCouponRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Coupon entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Coupon saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public CouponResponse update(String id, UpdateCouponRequest request) {
        Coupon entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Coupon saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon not found"));
        repository.deleteById(id);
    }
}
