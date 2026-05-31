package com.gogidix.ecommerce.checkout.domain.service;

import com.gogidix.ecommerce.checkout.domain.model.Checkout;
import com.gogidix.ecommerce.checkout.domain.repository.CheckoutRepository;
import com.gogidix.ecommerce.checkout.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.checkout.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {
    @Mock private CheckoutRepository repository;
    @InjectMocks private CheckoutService service;
    private RequestContext requestContext;
    @BeforeEach void setUp() {
        requestContext = RequestContext.builder()
                .tenantId("tenant-123").customerId("customer-456")
                .userId("user-789").correlationId("corr-abc").build();
        RequestContextHolder.set(requestContext);
    }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }
    @Test void testServiceCreation() { assertThat(service).isNotNull(); }
    @Test void initiateCheckout_shouldCreateCheckout() {
        Checkout saved = new Checkout(); saved.setId("id1");
        when(repository.save(any(Checkout.class))).thenReturn(saved);
        Checkout result = service.initiateCheckout("customer-456", "cart-1");
        assertThat(result).isNotNull(); assertThat(result.getId()).isEqualTo("id1");
        verify(repository).save(argThat(c ->
            c.getTenantId().equals("tenant-123") &&
            c.getCustomerId().equals("customer-456") &&
            c.getCartId().equals("cart-1") &&
            c.getStatus() == Checkout.CheckoutStatus.INITIATED &&
            c.getCheckoutId() != null
        ));
    }
    @Test void getCheckout_shouldReturnCheckout() {
        Checkout checkout = new Checkout(); checkout.setId("id1");
        when(repository.findByTenantIdAndCheckoutId("tenant-123", "co-1"))
            .thenReturn(Optional.of(checkout));
        Checkout result = service.getCheckout("co-1");
        assertThat(result).isNotNull(); assertThat(result.getId()).isEqualTo("id1");
    }
    @Test void getCheckout_notFound_returnsNull() {
        when(repository.findByTenantIdAndCheckoutId("tenant-123", "missing"))
            .thenReturn(Optional.empty());
        assertThat(service.getCheckout("missing")).isNull();
    }
    @Test void updateShipping_shouldUpdate() {
        Checkout checkout = new Checkout(); checkout.setId("id1");
        when(repository.findByTenantIdAndCheckoutId("tenant-123", "co-1"))
            .thenReturn(Optional.of(checkout));
        when(repository.save(any(Checkout.class))).thenAnswer(inv -> inv.getArgument(0));
        Checkout.ShippingAddress addr = new Checkout.ShippingAddress(); addr.setCity("NYC");
        Checkout result = service.updateShipping("co-1", addr);
        assertThat(result.getShippingAddress().getCity()).isEqualTo("NYC");
        assertThat(result.getStatus()).isEqualTo(Checkout.CheckoutStatus.SHIPPING_INFO);
    }
    @Test void updateShipping_notFound_returnsNull() {
        when(repository.findByTenantIdAndCheckoutId("tenant-123", "missing"))
            .thenReturn(Optional.empty());
        assertThat(service.updateShipping("missing", new Checkout.ShippingAddress())).isNull();
    }
    @Test void updatePayment_shouldUpdate() {
        Checkout checkout = new Checkout(); checkout.setId("id1");
        when(repository.findByTenantIdAndCheckoutId("tenant-123", "co-1"))
            .thenReturn(Optional.of(checkout));
        when(repository.save(any(Checkout.class))).thenAnswer(inv -> inv.getArgument(0));
        Checkout result = service.updatePayment("co-1", "paypal");
        assertThat(result.getPaymentMethod()).isEqualTo("paypal");
        assertThat(result.getStatus()).isEqualTo(Checkout.CheckoutStatus.PAYMENT_PENDING);
    }
    @Test void completeCheckout_shouldComplete() {
        Checkout checkout = new Checkout(); checkout.setId("id1");
        when(repository.findByTenantIdAndCheckoutId("tenant-123", "co-1"))
            .thenReturn(Optional.of(checkout));
        when(repository.save(any(Checkout.class))).thenAnswer(inv -> inv.getArgument(0));
        Checkout result = service.completeCheckout("co-1", "order-1");
        assertThat(result.getStatus()).isEqualTo(Checkout.CheckoutStatus.COMPLETED);
        assertThat(result.getOrderId()).isEqualTo("order-1");
        assertThat(result.getCompletedAt()).isNotNull();
    }
    @Test void failCheckout_shouldFail() {
        Checkout checkout = new Checkout(); checkout.setId("id1");
        when(repository.findByTenantIdAndCheckoutId("tenant-123", "co-1"))
            .thenReturn(Optional.of(checkout));
        when(repository.save(any(Checkout.class))).thenAnswer(inv -> inv.getArgument(0));
        Checkout result = service.failCheckout("co-1", "card declined");
        assertThat(result.getStatus()).isEqualTo(Checkout.CheckoutStatus.FAILED);
        assertThat(result.getUpdatedAt()).isNotNull();
    }
    @Test void completeCheckout_notFound_returnsNull() {
        when(repository.findByTenantIdAndCheckoutId("tenant-123", "missing"))
            .thenReturn(Optional.empty());
        assertThat(service.completeCheckout("missing", "order-1")).isNull();
    }
}