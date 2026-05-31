package com.gogidix.ecommerce.customer.domain.port.in;

import com.gogidix.ecommerce.customer.application.dto.*;
import java.util.List;

public interface CustomerUseCase {
    CustomerResponse create(CreateCustomerRequest request);
    CustomerResponse update(String id, UpdateCustomerRequest request);
    void delete(String id);
    CustomerResponse getById(String id);
    List<CustomerResponse> getAll();
}
