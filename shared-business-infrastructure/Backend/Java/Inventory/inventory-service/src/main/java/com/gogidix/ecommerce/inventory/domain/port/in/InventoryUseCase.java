package com.gogidix.ecommerce.inventory.domain.port.in;

import com.gogidix.ecommerce.inventory.application.dto.*;
import java.util.List;

public interface InventoryUseCase {
    InventoryResponse create(CreateInventoryRequest request);
    InventoryResponse update(String id, UpdateInventoryRequest request);
    void delete(String id);
    InventoryResponse getById(String id);
    List<InventoryResponse> getAll();
}
