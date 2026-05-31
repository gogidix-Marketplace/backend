package com.gogidix.ecommerce.warehouse.domain.port.in;

import com.gogidix.ecommerce.warehouse.application.dto.*;
import java.util.List;

public interface WarehouseUseCase {
    WarehouseResponse create(CreateWarehouseRequest request);
    WarehouseResponse update(String id, UpdateWarehouseRequest request);
    void delete(String id);
    WarehouseResponse getById(String id);
    List<WarehouseResponse> getAll();
}
