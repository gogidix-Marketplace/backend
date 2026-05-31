package com.gogidix.ecommerce.storecredit.domain.port.in;

import com.gogidix.ecommerce.storecredit.application.dto.*;
import java.util.List;

public interface StoreCreditUseCase {
    StoreCreditResponse create(CreateStoreCreditRequest request);
    StoreCreditResponse update(String id, UpdateStoreCreditRequest request);
    void delete(String id);
    StoreCreditResponse getById(String id);
    List<StoreCreditResponse> getAll();
}
