package com.gogidix.ecommerce.checkout.application.mapper;

import com.gogidix.ecommerce.checkout.application.dto.CheckoutDto;
import com.gogidix.ecommerce.checkout.application.dto.CheckoutResponse;
import com.gogidix.ecommerce.checkout.application.dto.CreateCheckoutRequest;
import com.gogidix.ecommerce.checkout.domain.model.Checkout;
import com.gogidix.ecommerce.checkout.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CheckoutMapper {

    public CheckoutDto toDto(Checkout entity) {
        return new CheckoutDto(
            entity.getId(),
            entity.getTenantId(),
            entity.getCustomerId(),
            entity.getCartId(),
            null,
            null,
            entity.getPaymentMethod(),
            entity.getSubtotal(),
            entity.getTaxAmount(),
            entity.getShippingAmount(),
            entity.getTotalAmount(),
            entity.getStatus() != null ? entity.getStatus().name() : null,
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }

    public Checkout toEntity(CreateCheckoutRequest request) {
        Checkout entity = new Checkout();
        entity.setTenantId(RequestContextHolder.getTenantId());
        entity.setCustomerId(RequestContextHolder.getCustomerId());
        entity.setCartId(request.cartId());
        entity.setPaymentMethod(request.paymentMethod());
        return entity;
    }

    public CheckoutResponse toResponse(CheckoutDto dto) {
        return CheckoutResponse.from(dto);
    }
}
