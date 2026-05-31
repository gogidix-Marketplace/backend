package com.gogidix.ecommerce.promotion.domain.port.in;

import com.gogidix.ecommerce.promotion.application.dto.*;
import java.util.List;

public interface PromotionUseCase {
    PromotionResponse create(CreatePromotionRequest request);
    PromotionResponse update(String id, UpdatePromotionRequest request);
    void delete(String id);
    PromotionResponse getById(String id);
    List<PromotionResponse> getAll();
}
