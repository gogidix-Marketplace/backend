package com.gogidix.ecommerce.airfreight.domain.port.in;

import com.gogidix.ecommerce.airfreight.application.dto.*;
import java.util.List;

public interface AirFreightUseCase {
    AirFreightResponse create(CreateAirFreightRequest request);
    AirFreightResponse update(String id, UpdateAirFreightRequest request);
    void delete(String id);
    AirFreightResponse getById(String id);
    List<AirFreightResponse> getAll();
}
