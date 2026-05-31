package com.gogidix.ecommerce.paymentmethod.application.service;

import com.gogidix.ecommerce.paymentmethod.application.dto.*;
import com.gogidix.ecommerce.paymentmethod.application.mapper.PaymentMethodMapper;
import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import com.gogidix.ecommerce.paymentmethod.domain.repository.PaymentMethodRepository;
import com.gogidix.ecommerce.paymentmethod.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepository repository;
    private final PaymentMethodMapper mapper;

    public PaymentMethodService(PaymentMethodRepository repository, PaymentMethodMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PaymentMethodResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public PaymentMethodResponse getById(String id) {
        PaymentMethod entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentMethod not found"));
        return mapper.toResponse(entity);
    }

    public PaymentMethodResponse create(CreatePaymentMethodRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        PaymentMethod entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        PaymentMethod saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public PaymentMethodResponse update(String id, UpdatePaymentMethodRequest request) {
        PaymentMethod entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentMethod not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        PaymentMethod saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentMethod not found"));
        repository.deleteById(id);
    }
}
