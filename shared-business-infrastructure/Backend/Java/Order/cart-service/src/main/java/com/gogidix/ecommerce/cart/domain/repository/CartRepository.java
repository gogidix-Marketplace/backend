package com.gogidix.ecommerce.cart.domain.repository;

import com.gogidix.ecommerce.cart.domain.model.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends MongoRepository<Cart, String> {

    Optional<Cart> findByTenantIdAndCartId(String tenantId, String cartId);

    Optional<Cart> findByTenantIdAndCustomerIdAndStatus(
            String tenantId, String customerId, Cart.CartStatus status);
}
