package com.gogidix.ecommerce.cart.domain.service;

import com.gogidix.ecommerce.cart.domain.model.Cart;
import com.gogidix.ecommerce.cart.domain.model.Cart.CartItem;
import com.gogidix.ecommerce.cart.domain.repository.CartRepository;
import com.gogidix.ecommerce.cart.shared.requestcontext.RequestContext;
import com.gogidix.ecommerce.cart.shared.requestcontext.RequestContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock private CartRepository repository;
    @InjectMocks private CartService service;
    @BeforeEach void setUp() { RequestContextHolder.set(RequestContext.builder().tenantId("t1").userId("u1").build()); }
    @AfterEach void tearDown() { RequestContextHolder.clear(); }

    private Cart buildCart() {
        Cart c = new Cart(); c.setId("id1"); c.setTenantId("t1"); c.setCartId("c1");
        c.setStatus(Cart.CartStatus.ACTIVE); c.setSubtotal(BigDecimal.ZERO);
        c.setTaxAmount(BigDecimal.ZERO); c.setDiscountAmount(BigDecimal.ZERO);
        c.setTotalAmount(BigDecimal.ZERO);
        return c;
    }

    @Test void getCart_found() {
        when(repository.findByTenantIdAndCartId("t1","c1")).thenReturn(Optional.of(buildCart()));
        assertThat(service.getCart("c1")).isNotNull();
    }
    @Test void getCart_notFound() {
        when(repository.findByTenantIdAndCartId("t1","x")).thenReturn(Optional.empty());
        assertThat(service.getCart("x")).isNull();
    }
    @Test void getActiveCart_existing() {
        when(repository.findByTenantIdAndCustomerIdAndStatus("t1","cust1",Cart.CartStatus.ACTIVE))
            .thenReturn(Optional.of(buildCart()));
        assertThat(service.getActiveCart("cust1")).isNotNull();
    }
    @Test void getActiveCart_createsNew() {
        when(repository.findByTenantIdAndCustomerIdAndStatus("t1","cust1",Cart.CartStatus.ACTIVE))
            .thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Cart result = service.getActiveCart("cust1");
        assertThat(result).isNotNull();
        assertThat(result.getCartId()).isNotNull();
        verify(repository).save(any());
    }
    @Test void createCart() {
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Cart result = service.createCart("cust1");
        assertThat(result.getCartId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Cart.CartStatus.ACTIVE);
        assertThat(result.getTenantId()).isEqualTo("t1");
    }
    @Test void addItem() {
        Cart cart = buildCart();
        when(repository.findByTenantIdAndCartId("t1","c1")).thenReturn(Optional.of(cart));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        CartItem item = new CartItem();
        item.setProductId("p1"); item.setSku("SKU1"); item.setName("Widget");
        item.setUnitPrice(BigDecimal.TEN); item.setQuantity(2);
        Cart result = service.addItem("c1", item);
        assertThat(result).isNotNull();
        assertThat(result.getItems()).hasSize(1);
    }
    @Test void addItem_cartNotFound() {
        when(repository.findByTenantIdAndCartId("t1","x")).thenReturn(Optional.empty());
        assertThat(service.addItem("x", new CartItem())).isNull();
    }
    @Test void updateItem() {
        Cart cart = buildCart();
        CartItem ci = new CartItem(); ci.setItemId("i1"); ci.setUnitPrice(BigDecimal.TEN); ci.setQuantity(1);
        ci.setLineTotal(BigDecimal.TEN);
        cart.getItems().add(ci);
        when(repository.findByTenantIdAndCartId("t1","c1")).thenReturn(Optional.of(cart));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Cart result = service.updateItem("c1", "i1", 5);
        assertThat(result).isNotNull();
        assertThat(result.getItems().get(0).getQuantity()).isEqualTo(5);
    }
    @Test void removeItem() {
        Cart cart = buildCart();
        CartItem ci = new CartItem(); ci.setItemId("i1"); ci.setLineTotal(BigDecimal.TEN);
        cart.getItems().add(ci);
        when(repository.findByTenantIdAndCartId("t1","c1")).thenReturn(Optional.of(cart));
        when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        Cart result = service.removeItem("c1", "i1");
        assertThat(result.getItems()).isEmpty();
    }
}