package com.gogidix.ecommerce.analytics.domain.port.in;

import com.gogidix.ecommerce.analytics.application.dto.*;
import java.util.List;

public interface AnalyticsUseCase {
    AnalyticsResponse create(CreateAnalyticsRequest request);
    AnalyticsResponse update(String id, UpdateAnalyticsRequest request);
    void delete(String id);
    AnalyticsResponse getById(String id);
    List<AnalyticsResponse> getAll();
}
