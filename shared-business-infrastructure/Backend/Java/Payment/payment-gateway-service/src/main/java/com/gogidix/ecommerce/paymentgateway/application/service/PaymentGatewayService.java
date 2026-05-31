package com.gogidix.ecommerce.paymentgateway.application.service;

import com.gogidix.ecommerce.paymentgateway.application.dto.*;
import com.gogidix.ecommerce.paymentgateway.application.mapper.PaymentGatewayMapper;
import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import com.gogidix.ecommerce.paymentgateway.domain.repository.PaymentGatewayRepository;
import com.gogidix.ecommerce.paymentgateway.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentGatewayService {

    private final PaymentGatewayRepository repository;
    private final PaymentGatewayMapper mapper;

    public PaymentGatewayService(PaymentGatewayRepository repository, PaymentGatewayMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public List<PaymentGatewayResponse> getAll() {
        String tenantId = RequestContextHolder.getTenantId();
        return repository.findByTenantIdAndIsActive(tenantId, true)
                .stream().map(mapper::toResponse).toList();
    }

    public PaymentGatewayResponse getById(String id) {
        PaymentGateway entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentGateway not found"));
        return mapper.toResponse(entity);
    }

    public PaymentGatewayResponse create(CreatePaymentGatewayRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        PaymentGateway entity = mapper.toEntity(request);
        entity.setTenantId(tenantId);
        PaymentGateway saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public PaymentGatewayResponse update(String id, UpdatePaymentGatewayRequest request) {
        PaymentGateway entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentGateway not found"));
        mapper.updateFromRequest(entity, request);
        entity.updateTimestamp();
        PaymentGateway saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    public void delete(String id) {
        repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("PaymentGateway not found"));
        repository.deleteById(id);
    }
}
