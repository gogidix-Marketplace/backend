package com.gogidix.ecommerce.paymentgateway.application.service;

import com.gogidix.ecommerce.paymentgateway.application.dto.CreatePaymentGatewayRequest;
import com.gogidix.ecommerce.paymentgateway.application.dto.UpdatePaymentGatewayRequest;
import com.gogidix.ecommerce.paymentgateway.application.dto.PaymentGatewayResponse;
import com.gogidix.ecommerce.paymentgateway.application.mapper.PaymentGatewayMapper;
import com.gogidix.ecommerce.paymentgateway.domain.model.PaymentGateway;
import com.gogidix.ecommerce.paymentgateway.domain.port.out.PaymentGatewayRepositoryPort;
import com.gogidix.ecommerce.paymentgateway.domain.port.out.PaymentGatewayEventPublisher;
import com.gogidix.ecommerce.paymentgateway.shared.exception.PaymentGatewayNotFoundException;
import com.gogidix.ecommerce.paymentgateway.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.paymentgateway.shared.requestcontext.RequestContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentGatewayService {

    private final PaymentGatewayRepositoryPort repositoryPort;
    private final PaymentGatewayEventPublisher eventPublisher;
    private final PaymentGatewayMapper mapper;

    public PaymentGatewayService(PaymentGatewayRepositoryPort repositoryPort, PaymentGatewayEventPublisher eventPublisher, PaymentGatewayMapper mapper) {
        this.repositoryPort = repositoryPort;
        this.eventPublisher = eventPublisher;
        this.mapper = mapper;
    }

    public PaymentGatewayResponse create(CreatePaymentGatewayRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        PaymentGateway entity = mapper.toEntity(request);
        entity.setTenantId(ctx.getTenantId());
        PaymentGateway saved = repositoryPort.save(entity);
        eventPublisher.publishCreated(saved);
        return mapper.toResponse(saved);
    }

    public PaymentGatewayResponse getById(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        PaymentGateway entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new PaymentGatewayNotFoundException("PaymentGateway not found with id: " + id));
        return mapper.toResponse(entity);
    }

    public List<PaymentGatewayResponse> getList(int page, int size) {
        RequestContext ctx = RequestContextHolder.getContext();
        Page<PaymentGateway> result = repositoryPort.findByTenantId(ctx.getTenantId(), PageRequest.of(page, size));
        return result.stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    public PaymentGatewayResponse update(String id, UpdatePaymentGatewayRequest request) {
        RequestContext ctx = RequestContextHolder.getContext();
        PaymentGateway entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new PaymentGatewayNotFoundException("PaymentGateway not found with id: " + id));
        mapper.updateFromRequest(entity, request);
        PaymentGateway updated = repositoryPort.save(entity);
        eventPublisher.publishUpdated(updated);
        return mapper.toResponse(updated);
    }

    public void delete(String id) {
        RequestContext ctx = RequestContextHolder.getContext();
        PaymentGateway entity = repositoryPort.findByIdAndTenantId(id, ctx.getTenantId())
            .orElseThrow(() -> new PaymentGatewayNotFoundException("PaymentGateway not found with id: " + id));
        repositoryPort.deleteById(id);
        eventPublisher.publishDeleted(entity);
    }
}