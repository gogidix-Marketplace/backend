package com.gogidix.ecommerce.checkout.domain.service;

import com.gogidix.ecommerce.checkout.shared.requestcontext.RequestContextHolder;
import com.gogidix.ecommerce.checkout.domain.model.Checkout;
import com.gogidix.ecommerce.checkout.domain.repository.CheckoutRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final CheckoutRepository checkoutRepository;

    public Checkout getCheckout(String checkoutId) {
        String tenantId = RequestContextHolder.getTenantId();
        return checkoutRepository.findByTenantIdAndCheckoutId(tenantId, checkoutId).orElse(null);
    }

    @Transactional
    public Checkout initiateCheckout(String customerId, String cartId) {
        String tenantId = RequestContextHolder.getTenantId();
        Checkout checkout = new Checkout();
        checkout.setTenantId(tenantId);
        checkout.setCheckoutId(UUID.randomUUID().toString());
        checkout.setCustomerId(customerId);
        checkout.setCartId(cartId);
        checkout.setStatus(Checkout.CheckoutStatus.INITIATED);
        checkout.setCreatedAt(Instant.now());
        return checkoutRepository.save(checkout);
    }

    @Transactional
    public Checkout updateShipping(String checkoutId, Checkout.ShippingAddress address) {
        Checkout checkout = getCheckout(checkoutId);
        if (checkout != null) {
            checkout.setShippingAddress(address);
            checkout.setStatus(Checkout.CheckoutStatus.SHIPPING_INFO);
            checkout.setUpdatedAt(Instant.now());
            return checkoutRepository.save(checkout);
        }
        return null;
    }

    @Transactional
    public Checkout updatePayment(String checkoutId, String paymentMethod) {
        Checkout checkout = getCheckout(checkoutId);
        if (checkout != null) {
            checkout.setPaymentMethod(paymentMethod);
            checkout.setStatus(Checkout.CheckoutStatus.PAYMENT_PENDING);
            checkout.setUpdatedAt(Instant.now());
            return checkoutRepository.save(checkout);
        }
        return null;
    }

    @Transactional
    public Checkout completeCheckout(String checkoutId, String orderId) {
        Checkout checkout = getCheckout(checkoutId);
        if (checkout != null) {
            checkout.setStatus(Checkout.CheckoutStatus.COMPLETED);
            checkout.setOrderId(orderId);
            checkout.setCompletedAt(Instant.now());
            checkout.setUpdatedAt(Instant.now());
            return checkoutRepository.save(checkout);
        }
        return null;
    }

    @Transactional
    public Checkout failCheckout(String checkoutId, String reason) {
        Checkout checkout = getCheckout(checkoutId);
        if (checkout != null) {
            checkout.setStatus(Checkout.CheckoutStatus.FAILED);
            checkout.setUpdatedAt(Instant.now());
            return checkoutRepository.save(checkout);
        }
        return null;
    }
}
