package com.gogidix.ecommerce.payment.application.service;

import com.gogidix.ecommerce.payment.application.dto.*;
import com.gogidix.ecommerce.payment.application.mapper.PaymentMapper;
import com.gogidix.ecommerce.payment.domain.model.Payment;
import com.gogidix.ecommerce.payment.domain.repository.PaymentRepository;
import com.gogidix.ecommerce.payment.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;

    public PaymentService(PaymentRepository repository, PaymentMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PaymentResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public PaymentResponse getById(String id) {
        Payment entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        return mapper.toResponse(entity);
    }

    public PaymentResponse create(CreatePaymentRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Payment entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        Payment saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public PaymentResponse update(String id, UpdatePaymentRequest request) {
        Payment entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        Payment saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found"));
        repository.deleteById(id);
    }
}
