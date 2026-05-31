package com.gogidix.ecommerce.discount.domain.port.in;

import com.gogidix.ecommerce.discount.application.dto.*;
import java.util.List;

public interface DiscountUseCase {
    DiscountResponse create(CreateDiscountRequest request);
    DiscountResponse update(String id, UpdateDiscountRequest request);
    void delete(String id);
    DiscountResponse getById(String id);
    List<DiscountResponse> getAll();
}
