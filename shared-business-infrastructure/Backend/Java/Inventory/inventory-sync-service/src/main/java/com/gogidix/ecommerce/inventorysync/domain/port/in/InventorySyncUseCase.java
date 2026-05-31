package com.gogidix.ecommerce.inventorysync.domain.port.in;

import com.gogidix.ecommerce.inventorysync.application.dto.*;
import java.util.List;

public interface InventorySyncUseCase {
    InventorySyncResponse create(CreateInventorySyncRequest request);
    InventorySyncResponse update(String id, UpdateInventorySyncRequest request);
    void delete(String id);
    InventorySyncResponse getById(String id);
    List<InventorySyncResponse> getAll();
}
