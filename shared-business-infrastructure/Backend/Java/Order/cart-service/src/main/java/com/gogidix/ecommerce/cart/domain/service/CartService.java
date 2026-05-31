package com.gogidix.ecommerce.cart.domain.service;

import com.gogidix.ecommerce.cart.domain.model.Cart;
import com.gogidix.ecommerce.cart.domain.model.Cart.CartItem;
import com.gogidix.ecommerce.cart.domain.model.Cart.CartStatus;
import com.gogidix.ecommerce.cart.domain.repository.CartRepository;
import com.gogidix.ecommerce.cart.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public Cart getCart(String cartId) {
        String tenantId = RequestContextHolder.getTenantId();
        return cartRepository.findByTenantIdAndCartId(tenantId, cartId).orElse(null);
    }

    public Cart getActiveCart(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();
        return cartRepository.findByTenantIdAndCustomerIdAndStatus(tenantId, customerId, CartStatus.ACTIVE)
                .orElseGet(() -> createCart(customerId));
    }

    @Transactional
    public Cart createCart(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();
        String userId = RequestContextHolder.getUserId();

        Cart cart = new Cart();
        cart.setTenantId(tenantId);
        cart.setCartId(UUID.randomUUID().toString());
        cart.setCustomerId(customerId);
        cart.setCartType(Cart.CartType.CUSTOMER);
        cart.setStatus(CartStatus.ACTIVE);
        cart.setSubtotal(BigDecimal.ZERO);
        cart.setTaxAmount(BigDecimal.ZERO);
        cart.setDiscountAmount(BigDecimal.ZERO);
        cart.setTotalAmount(BigDecimal.ZERO);
        cart.setCreatedAt(Instant.now());
        cart.setExpiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60));
        cart.setCreatedBy(userId);
        cart.setUpdatedBy(userId);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart addItem(String cartId, CartItem item) {
        Cart cart = getCart(cartId);
        if (cart == null) return null;

        item.setItemId(UUID.randomUUID().toString());
        item.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        cart.getItems().add(item);

        recalculateTotals(cart);
        cart.setUpdatedAt(Instant.now());
        cart.markAsUpdated();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItem(String cartId, String itemId, Integer quantity) {
        Cart cart = getCart(cartId);
        if (cart == null) return null;

        cart.getItems().stream()
                .filter(i -> i.getItemId().equals(itemId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setLineTotal(item.getUnitPrice().multiply(BigDecimal.valueOf(quantity)));
                });

        recalculateTotals(cart);
        cart.setUpdatedAt(Instant.now());
        cart.markAsUpdated();
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItem(String cartId, String itemId) {
        Cart cart = getCart(cartId);
        if (cart == null) return null;

        cart.getItems().removeIf(i -> i.getItemId().equals(itemId));
        recalculateTotals(cart);
        cart.setUpdatedAt(Instant.now());
        cart.markAsUpdated();
        return cartRepository.save(cart);
    }

    private void recalculateTotals(Cart cart) {
        BigDecimal subtotal = cart.getItems().stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setSubtotal(subtotal);
        cart.setTotalAmount(subtotal.add(cart.getTaxAmount()).subtract(cart.getDiscountAmount()));
    }
}
