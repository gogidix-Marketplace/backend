package com.gogidix.ecommerce.cart.interfaces.rest;

import com.gogidix.ecommerce.cart.application.dto.*;
import com.gogidix.ecommerce.cart.application.service.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {
    @Mock private CartService cartService;
    @InjectMocks private CartController controller;

    private CartResponse buildResp() { return mock(CartResponse.class); }

    @Test void getCart() {
        when(cartService.getCart("c1")).thenReturn(buildResp());
        assertThat(controller.getCart("c1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getActiveCart() {
        when(cartService.getActiveCart("cust1")).thenReturn(buildResp());
        assertThat(controller.getActiveCart("cust1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getCustomerCarts() {
        when(cartService.getCustomerCarts("cust1")).thenReturn(List.of(buildResp()));
        assertThat(controller.getCustomerCarts("cust1").getBody()).hasSize(1);
    }
    @Test void testCreateCart() {
        when(cartService.createCart(any())).thenReturn(buildResp());
        assertThat(controller.createCart(new CreateCartRequest("c","USD","web")).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void addItem() {
        when(cartService.addItem(eq("c1"), any())).thenReturn(buildResp());
        AddItemRequest req = new AddItemRequest("sku1","prod1","Product",1,BigDecimal.TEN,"USD",null);
        assertThat(controller.addItem("c1", req).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void removeItem() {
        when(cartService.removeItem("c1", "sku1")).thenReturn(buildResp());
        assertThat(controller.removeItem("c1", "sku1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void updateQuantity() {
        when(cartService.updateItemQuantity(eq("c1"), eq("sku1"), any())).thenReturn(buildResp());
        assertThat(controller.updateQuantity("c1","sku1",new UpdateItemQuantityRequest(3)).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void clearCart() {
        when(cartService.clearCart("c1")).thenReturn(buildResp());
        assertThat(controller.clearCart("c1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void lockCart() {
        when(cartService.lockCart("c1")).thenReturn(buildResp());
        assertThat(controller.lockCart("c1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void convertCart() {
        when(cartService.convertCart("c1")).thenReturn(buildResp());
        assertThat(controller.convertCart("c1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}