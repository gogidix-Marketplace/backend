package com.gogidix.ecommerce.checkout.domain.repository;

import com.gogidix.ecommerce.checkout.domain.model.Checkout;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CheckoutRepository extends MongoRepository<Checkout, String> {

    Optional<Checkout> findByTenantIdAndCheckoutId(String tenantId, String checkoutId);

    Optional<Checkout> findByTenantIdAndCustomerIdAndStatus(
            String tenantId, String customerId, Checkout.CheckoutStatus status);
}
