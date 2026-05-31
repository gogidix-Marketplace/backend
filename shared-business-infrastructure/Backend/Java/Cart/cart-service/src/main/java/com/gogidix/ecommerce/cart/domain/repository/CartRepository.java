package com.gogidix.ecommerce.cart.domain.repository;

import com.gogidix.ecommerce.cart.domain.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

    Optional<Cart> findByTenantIdAndCartId(String tenantId, String cartId);

    List<Cart> findByTenantIdAndCustomerId(String tenantId, String customerId);

    Optional<Cart> findByTenantIdAndCustomerIdAndStatus(String tenantId, String customerId, Cart.CartStatus status);

    List<Cart> findByTenantIdAndStatus(String tenantId, Cart.CartStatus status);

    void deleteByTenantIdAndExpiresAtBefore(String tenantId, java.time.Instant expiryDate);
}
