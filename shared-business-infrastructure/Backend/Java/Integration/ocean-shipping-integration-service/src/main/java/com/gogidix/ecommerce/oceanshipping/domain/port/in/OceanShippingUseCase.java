package com.gogidix.ecommerce.oceanshipping.domain.port.in;

import com.gogidix.ecommerce.oceanshipping.application.dto.*;
import java.util.List;

public interface OceanShippingUseCase {
    OceanShippingResponse create(CreateOceanShippingRequest request);
    OceanShippingResponse update(String id, UpdateOceanShippingRequest request);
    void delete(String id);
    OceanShippingResponse getById(String id);
    List<OceanShippingResponse> getAll();
}
