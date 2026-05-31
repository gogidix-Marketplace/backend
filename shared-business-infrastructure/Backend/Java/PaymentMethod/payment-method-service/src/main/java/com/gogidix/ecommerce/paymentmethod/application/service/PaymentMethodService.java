package com.gogidix.ecommerce.paymentmethod.application.service;

import com.gogidix.ecommerce.paymentmethod.application.dto.CreatePaymentMethodRequest;
import com.gogidix.ecommerce.paymentmethod.application.dto.UpdatePaymentMethodRequest;
import com.gogidix.ecommerce.paymentmethod.application.dto.PaymentMethodResponse;
import com.gogidix.ecommerce.paymentmethod.application.mapper.PaymentMethodMapper;
import com.gogidix.ecommerce.paymentmethod.domain.model.PaymentMethod;
import com.gogidix.ecommerce.paymentmethod.domain.port.out.PaymentMethodRepositoryPort;
import com.gogidix.ecommerce.paymentmethod.domain.port.out.PaymentMethodEventPublisher;
import com.gogidix.ecommerce.paymentmethod.shared.exception.PaymentMethodNotFoundException;
import com.gogidix.ecommerce.paymentmethod.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.paymentmethod.shared.requestcontext.RequestContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentMethodService {

    private final PaymentMethodRepositoryPort repositoryPort;
    private final PaymentMethodEventPublisher eventPublisher;
    private final PaymentMethodMapper mapper;

    public PaymentMethodService(PaymentMethodRepositoryPort repositoryPort, PaymentMethodEventPublisher eventPublisher, PaymentMethodMapper mapper) {
        this.repositoryPort = repositoryPort;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    public PaymentMethodResponse create(CreatePaymentMethodRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        PaymentMethod entity = mapper.toEntity(request);
        entity.setTenantId(ctx.getTenantId());
        PaymentMethod saved = repositoryPort.save(entity);
        eventPublisher.publishCreated(saved);
        return mapper.toResponse(saved);
    }

    public PaymentMethodResponse getById(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        PaymentMethod entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new PaymentMethodNotFoundException("PaymentMethod not found with id: " + id));
        return mapper.toResponse(entity);
    }

    public List<PaymentMethodResponse> getList(int page, int size) {
        RequestContext ctx = RequestContextHolder.getContext();
        Page<PaymentMethod> result = repositoryPort.findByTenantId(ctx.getTenantId(), PageRequest.of(page, size));
        return result.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public PaymentMethodResponse update(String id, UpdatePaymentMethodRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        PaymentMethod entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new PaymentMethodNotFoundException("PaymentMethod not found with id: " + id));
        mapper.updateFromRequest(entity, request);
        PaymentMethod updated = repositoryPort.save(entity);
        eventPublisher.publishUpdated(updated);
        return mapper.toResponse(updated);
    }

    public void delete(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        PaymentMethod entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new PaymentMethodNotFoundException("PaymentMethod not found with id: " + id));
        repositoryPort.deleteById(id);
        eventPublisher.publishDeleted(entity);
    }
}