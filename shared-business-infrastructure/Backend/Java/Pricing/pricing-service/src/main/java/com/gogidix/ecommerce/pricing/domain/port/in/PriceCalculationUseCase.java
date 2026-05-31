package com.gogidix.ecommerce.pricing.domain.port.in;

import java.math.BigDecimal;

public interface PriceCalculationUseCase {

    BigDecimal calculatePrice(String tenantId, String productId, BigDecimal basePrice, int quantity);

    BigDecimal calculateDiscount(String tenantId, String productId, BigDecimal basePrice);
}
