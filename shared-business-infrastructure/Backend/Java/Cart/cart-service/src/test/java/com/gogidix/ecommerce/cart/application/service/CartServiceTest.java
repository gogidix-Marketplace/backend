package com.gogidix.ecommerce.cart.application.service;

import com.gogidix.ecommerce.cart.application.dto.AddItemRequest;
import com.gogidix.ecommerce.cart.application.dto.CartResponse;
import com.gogidix.ecommerce.cart.application.dto.CreateCartRequest;
import com.gogidix.ecommerce.cart.application.dto.UpdateItemQuantityRequest;
import com.gogidix.ecommerce.cart.application.mapper.CartMapper;
import com.gogidix.ecommerce.cart.domain.model.Cart;
import com.gogidix.ecommerce.cart.domain.model.CartItem;
import com.gogidix.ecommerce.cart.domain.repository.CartRepository;
import com.gogidix.ecommerce.cart.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.cart.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock private CartRepository cartRepository;
    @Mock private CartMapper cartMapper;
    private CartService service;

    @BeforeEach void setUp() {
        RequestContextHolder.set(RequestContext.builder().tenantId("t1").build());
        service = new CartService(cartRepository, cartMapper);
    }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private Cart buildCart() {
        Cart cart = Cart.create("t1", "cust1", "USD", "web");
        return cart;
    }
    private CartResponse buildResponse() { return mock(CartResponse.class); }

    @Test void getCart_found() {
        Cart cart = buildCart();
        when(cartRepository.findByTenantIdAndCartId("t1", "cart1")).thenReturn(Optional.of(cart));
        when(cartMapper.toCartResponse(cart)).thenReturn(buildResponse());
        assertThat(service.getCart("cart1")).isNotNull();
    }
    @Test void getCart_notFound() {
        when(cartRepository.findByTenantIdAndCartId("t1", "missing")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.getCart("missing")).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void getActiveCart_existing() {
        Cart cart = buildCart();
        when(cartRepository.findByTenantIdAndCustomerIdAndStatus("t1", "cust1", Cart.CartStatus.ACTIVE))
            .thenReturn(Optional.of(cart));
        when(cartMapper.toCartResponse(cart)).thenReturn(buildResponse());
        assertThat(service.getActiveCart("cust1")).isNotNull();
    }
    @Test void getActiveCart_createsNew() {
        when(cartRepository.findByTenantIdAndCustomerIdAndStatus("t1", "cust1", Cart.CartStatus.ACTIVE))
            .thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(buildResponse());
        assertThat(service.getActiveCart("cust1")).isNotNull();
        verify(cartRepository).save(any(Cart.class));
    }
    @Test void testCreateCart() {
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(buildResponse());
        CreateCartRequest req = new CreateCartRequest("cust1", "USD", "web");
        assertThat(service.createCart(req)).isNotNull();
    }
    @Test void addItem_success() {
        Cart cart = buildCart();
        when(cartRepository.findByTenantIdAndCartId("t1", "cart1")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
        CartItem item = CartItem.create("sku1", "prod1", "Product", 2, BigDecimal.TEN, "USD");
        when(cartMapper.toCartItem(any(AddItemRequest.class))).thenReturn(item);
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(buildResponse());
        AddItemRequest req = new AddItemRequest("sku1", "prod1", "Product", 2, BigDecimal.TEN, "USD", null);
        assertThat(service.addItem("cart1", req)).isNotNull();
    }
    @Test void addItem_cartNotFound() {
        when(cartRepository.findByTenantIdAndCartId("t1", "x")).thenReturn(Optional.empty());
        AddItemRequest req = new AddItemRequest("sku1", "prod1", "Product", 1, BigDecimal.TEN, "USD", null);
        assertThatThrownBy(() -> service.addItem("x", req)).isInstanceOf(IllegalArgumentException.class);
    }
    @Test void removeItem() {
        Cart cart = buildCart();
        cart.addItem(CartItem.create("sku1", "prod1", "Prod", 1, BigDecimal.TEN, "USD"));
        when(cartRepository.findByTenantIdAndCartId("t1", "cart1")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(buildResponse());
        assertThat(service.removeItem("cart1", "sku1")).isNotNull();
    }
    @Test void updateItemQuantity_positive() {
        Cart cart = buildCart();
        cart.addItem(CartItem.create("sku1", "prod1", "Prod", 1, BigDecimal.TEN, "USD"));
        when(cartRepository.findByTenantIdAndCartId("t1", "cart1")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(buildResponse());
        assertThat(service.updateItemQuantity("cart1", "sku1", new UpdateItemQuantityRequest(5))).isNotNull();
    }
    @Test void updateItemQuantity_zero_removes() {
        Cart cart = buildCart();
        cart.addItem(CartItem.create("sku1", "prod1", "Prod", 1, BigDecimal.TEN, "USD"));
        when(cartRepository.findByTenantIdAndCartId("t1", "cart1")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(buildResponse());
        service.updateItemQuantity("cart1", "sku1", new UpdateItemQuantityRequest(0));
        verify(cartRepository).save(any(Cart.class));
    }
    @Test void clearCart() {
        Cart cart = buildCart();
        cart.addItem(CartItem.create("sku1", "prod1", "Prod", 1, BigDecimal.TEN, "USD"));
        when(cartRepository.findByTenantIdAndCartId("t1", "cart1")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(buildResponse());
        service.clearCart("cart1");
        verify(cartRepository).save(any(Cart.class));
    }
    @Test void lockCart() {
        Cart cart = buildCart();
        when(cartRepository.findByTenantIdAndCartId("t1", "cart1")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(buildResponse());
        service.lockCart("cart1");
        verify(cartRepository).save(argThat(c -> c.getStatus() == Cart.CartStatus.LOCKED));
    }
    @Test void convertCart() {
        Cart cart = buildCart();
        cart.addItem(CartItem.create("sku1", "prod1", "Prod", 1, BigDecimal.TEN, "USD"));
        when(cartRepository.findByTenantIdAndCartId("t1", "cart1")).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));
        when(cartMapper.toCartResponse(any(Cart.class))).thenReturn(buildResponse());
        service.convertCart("cart1");
        verify(cartRepository).save(argThat(c -> c.getStatus() == Cart.CartStatus.CONVERTED));
    }
    @Test void getCustomerCarts() {
        when(cartRepository.findByTenantIdAndCustomerId("t1", "cust1")).thenReturn(List.of(buildCart()));
        when(cartMapper.toCartResponseList(anyList())).thenReturn(List.of(mock(CartResponse.class)));
        assertThat(service.getCustomerCarts("cust1")).hasSize(1);
    }
    @Test void deleteExpiredCarts() {
        service.deleteExpiredCarts();
        verify(cartRepository).deleteByTenantIdAndExpiresAtBefore(eq("t1"), any());
    }
}