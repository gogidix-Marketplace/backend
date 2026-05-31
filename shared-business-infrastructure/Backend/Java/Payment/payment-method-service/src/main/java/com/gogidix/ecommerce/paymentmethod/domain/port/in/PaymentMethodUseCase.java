package com.gogidix.ecommerce.paymentmethod.domain.port.in;

import com.gogidix.ecommerce.paymentmethod.application.dto.*;
import java.util.List;

public interface PaymentMethodUseCase {
    PaymentMethodResponse create(CreatePaymentMethodRequest request);
    PaymentMethodResponse update(String id, UpdatePaymentMethodRequest request);
    void delete(String id);
    PaymentMethodResponse getById(String id);
    List<PaymentMethodResponse> getAll();
}
