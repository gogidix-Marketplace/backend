package com.gogidix.ecommerce.cart.application.service;

import com.gogidix.ecommerce.cart.application.dto.AddItemRequest;
import com.gogidix.ecommerce.cart.application.dto.CartResponse;
import com.gogidix.ecommerce.cart.application.dto.CreateCartRequest;
import com.gogidix.ecommerce.cart.application.dto.UpdateItemQuantityRequest;
import com.gogidix.ecommerce.cart.application.mapper.CartMapper;
import com.gogidix.ecommerce.cart.domain.model.Cart;
import com.gogidix.ecommerce.cart.domain.model.CartItem;
import com.gogidix.ecommerce.cart.domain.repository.CartRepository;
import com.gogidix.ecommerce.cart.shared.requestcontext.RequestContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartService(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    public CartResponse getCart(String cartId) {
        String tenantId = RequestContextHolder.getTenantId();
        Cart cart = cartRepository.findByTenantIdAndCartId(tenantId, cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse getActiveCart(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();
        Cart cart = cartRepository.findByTenantIdAndCustomerIdAndStatus(
            tenantId, customerId, Cart.CartStatus.ACTIVE)
            .orElseGet(() -> {
                Cart newCart = Cart.create(tenantId, customerId, "USD", "web");
                return cartRepository.save(newCart);
            });
        return cartMapper.toCartResponse(cart);
    }

    @Transactional
    public CartResponse createCart(CreateCartRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Cart cart = Cart.create(tenantId, request.customerId(), request.currency(),
                request.channelId() != null ? request.channelId() : "web");
        Cart saved = cartRepository.save(cart);
        return cartMapper.toCartResponse(saved);
    }

    @Transactional
    public CartResponse addItem(String cartId, AddItemRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Cart cart = cartRepository.findByTenantIdAndCartId(tenantId, cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (cart.getStatus() != Cart.CartStatus.ACTIVE) {
            throw new IllegalStateException("Cart is not active");
        }

        CartItem item = cartMapper.toCartItem(request);
        cart.addItem(item);
        Cart saved = cartRepository.save(cart);
        return cartMapper.toCartResponse(saved);
    }

    @Transactional
    public CartResponse removeItem(String cartId, String sku) {
        String tenantId = RequestContextHolder.getTenantId();
        Cart cart = cartRepository.findByTenantIdAndCartId(tenantId, cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.removeItem(sku);
        Cart saved = cartRepository.save(cart);
        return cartMapper.toCartResponse(saved);
    }

    @Transactional
    public CartResponse updateItemQuantity(String cartId, String sku, UpdateItemQuantityRequest request) {
        String tenantId = RequestContextHolder.getTenantId();
        Cart cart = cartRepository.findByTenantIdAndCartId(tenantId, cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));

        if (request.quantity() <= 0) {
            cart.removeItem(sku);
        } else {
            cart.updateItemQuantity(sku, request.quantity());
        }
        Cart saved = cartRepository.save(cart);
        return cartMapper.toCartResponse(saved);
    }

    @Transactional
    public CartResponse clearCart(String cartId) {
        String tenantId = RequestContextHolder.getTenantId();
        Cart cart = cartRepository.findByTenantIdAndCartId(tenantId, cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.clear();
        Cart saved = cartRepository.save(cart);
        return cartMapper.toCartResponse(saved);
    }

    @Transactional
    public CartResponse lockCart(String cartId) {
        String tenantId = RequestContextHolder.getTenantId();
        Cart cart = cartRepository.findByTenantIdAndCartId(tenantId, cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.lock();
        Cart saved = cartRepository.save(cart);
        return cartMapper.toCartResponse(saved);
    }

    @Transactional
    public CartResponse convertCart(String cartId) {
        String tenantId = RequestContextHolder.getTenantId();
        Cart cart = cartRepository.findByTenantIdAndCartId(tenantId, cartId)
            .orElseThrow(() -> new IllegalArgumentException("Cart not found"));
        cart.convert();
        Cart saved = cartRepository.save(cart);
        return cartMapper.toCartResponse(saved);
    }

    public List<CartResponse> getCustomerCarts(String customerId) {
        String tenantId = RequestContextHolder.getTenantId();
        List<Cart> carts = cartRepository.findByTenantIdAndCustomerId(tenantId, customerId);
        return cartMapper.toCartResponseList(carts);
    }

    @Transactional
    public void deleteExpiredCarts() {
        String tenantId = RequestContextHolder.getTenantId();
        cartRepository.deleteByTenantIdAndExpiresAtBefore(tenantId, java.time.Instant.now());
    }
}
