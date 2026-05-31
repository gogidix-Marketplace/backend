package com.gogidix.ecommerce.reward.domain.port.in;

import com.gogidix.ecommerce.reward.application.dto.*;
import java.util.List;

public interface RewardUseCase {
    RewardResponse create(CreateRewardRequest request);
    RewardResponse update(String id, UpdateRewardRequest request);
    void delete(String id);
    RewardResponse getById(String id);
    List<RewardResponse> getAll();
}
