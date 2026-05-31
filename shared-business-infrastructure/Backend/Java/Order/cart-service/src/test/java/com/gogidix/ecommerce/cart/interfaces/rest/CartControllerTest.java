package com.gogidix.ecommerce.cart.interfaces.rest;

import com.gogidix.ecommerce.cart.application.dto.*;
import com.gogidix.ecommerce.cart.application.mapper.CartMapper;
import com.gogidix.ecommerce.cart.domain.model.Cart;
import com.gogidix.ecommerce.cart.domain.service.CartService;
import com.gogidix.ecommerce.cart.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.cart.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {
    @Mock private CartService cartService;
    @Mock private CartMapper cartMapper;
    @InjectMocks private CartController controller;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private Cart buildCart() {
        Cart c = new Cart(); c.setId("id1"); c.setCartId("c1"); return c;
    }
    private CartDto buildDto() { return mock(CartDto.class); }
    private CartResponse buildResp() { return mock(CartResponse.class); }

    @Test void getCart_found() {
        when(cartService.getCart("c1")).thenReturn(buildCart());
        when(cartMapper.toDto(any())).thenReturn(buildDto());
        when(cartMapper.toResponse(any())).thenReturn(buildResp());
        assertThat(controller.getCart("c1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getCart_notFound() {
        when(cartService.getCart("x")).thenReturn(null);
        assertThat(controller.getCart("x").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void getActiveCart() {
        when(cartService.getActiveCart("cust1")).thenReturn(buildCart());
        when(cartMapper.toDto(any())).thenReturn(buildDto());
        when(cartMapper.toResponse(any())).thenReturn(buildResp());
        assertThat(controller.getActiveCart("cust1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void createCart() {
        when(cartService.createCart("cust1")).thenReturn(buildCart());
        when(cartMapper.toDto(any())).thenReturn(buildDto());
        when(cartMapper.toResponse(any())).thenReturn(buildResp());
        assertThat(controller.createCart("cust1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void addItem_found() {
        when(cartMapper.toCartItem(any(AddCartItemRequest.class))).thenReturn(new Cart.CartItem());
        when(cartService.addItem(eq("c1"), any())).thenReturn(buildCart());
        when(cartMapper.toDto(any())).thenReturn(buildDto());
        when(cartMapper.toResponse(any())).thenReturn(buildResp());
        AddCartItemRequest req = new AddCartItemRequest("p1", "SKU1", "Widget", "img.png", BigDecimal.TEN, 2, "v1");
        assertThat(controller.addItem("c1", req).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void updateItem_found() {
        when(cartService.updateItem("c1", "i1", 5)).thenReturn(buildCart());
        when(cartMapper.toDto(any())).thenReturn(buildDto());
        when(cartMapper.toResponse(any())).thenReturn(buildResp());
        assertThat(controller.updateItem("c1","i1",new UpdateCartItemRequest("p1", 5)).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void removeItem_found() {
        when(cartService.removeItem("c1","i1")).thenReturn(buildCart());
        when(cartMapper.toDto(any())).thenReturn(buildDto());
        when(cartMapper.toResponse(any())).thenReturn(buildResp());
        assertThat(controller.removeItem("c1","i1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}