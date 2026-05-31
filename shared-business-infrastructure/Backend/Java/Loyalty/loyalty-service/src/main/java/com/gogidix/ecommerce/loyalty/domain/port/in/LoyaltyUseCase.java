package com.gogidix.ecommerce.loyalty.domain.port.in;

import com.gogidix.ecommerce.loyalty.application.dto.*;
import java.util.List;

public interface LoyaltyUseCase {
    LoyaltyResponse create(CreateLoyaltyRequest request);
    LoyaltyResponse update(String id, UpdateLoyaltyRequest request);
    void delete(String id);
    LoyaltyResponse getById(String id);
    List<LoyaltyResponse> getAll();
}
