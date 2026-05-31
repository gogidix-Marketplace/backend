package com.gogidix.ecommerce.checkout.interfaces.rest;

import com.gogidix.ecommerce.checkout.domain.model.Checkout;
import com.gogidix.ecommerce.checkout.domain.service.CheckoutService;
import com.gogidix.ecommerce.checkout.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.checkout.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutControllerTest {
    @Mock private CheckoutService service;
    @InjectMocks private CheckoutController controller;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private Checkout createCheckout() { Checkout c = new Checkout(); c.setId("id1"); return c; }

    @Test void initiateCheckout() {
        when(service.initiateCheckout("cust1", "cart1")).thenReturn(createCheckout());
        assertThat(controller.initiateCheckout("cust1","cart1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getCheckout_found() {
        when(service.getCheckout("co1")).thenReturn(createCheckout());
        assertThat(controller.getCheckout("co1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void getCheckout_notFound() {
        when(service.getCheckout("missing")).thenReturn(null);
        assertThat(controller.getCheckout("missing").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void updateShipping_found() {
        when(service.updateShipping(eq("co1"), any())).thenReturn(createCheckout());
        assertThat(controller.updateShipping("co1", new Checkout.ShippingAddress()).getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void updateShipping_notFound() {
        when(service.updateShipping(eq("missing"), any())).thenReturn(null);
        assertThat(controller.updateShipping("missing", new Checkout.ShippingAddress()).getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    @Test void updatePayment_found() {
        when(service.updatePayment("co1", "paypal")).thenReturn(createCheckout());
        assertThat(controller.updatePayment("co1","paypal").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void completeCheckout_found() {
        when(service.completeCheckout("co1","ord1")).thenReturn(createCheckout());
        assertThat(controller.completeCheckout("co1","ord1").getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    @Test void completeCheckout_notFound() {
        when(service.completeCheckout("missing","ord1")).thenReturn(null);
        assertThat(controller.completeCheckout("missing","ord1").getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}