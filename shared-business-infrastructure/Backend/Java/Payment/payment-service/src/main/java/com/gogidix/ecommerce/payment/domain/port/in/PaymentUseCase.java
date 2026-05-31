package com.gogidix.ecommerce.payment.domain.port.in;

import com.gogidix.ecommerce.payment.application.dto.*;
import java.util.List;

public interface PaymentUseCase {
    PaymentResponse create(CreatePaymentRequest request);
    PaymentResponse update(String id, UpdatePaymentRequest request);
    void delete(String id);
    PaymentResponse getById(String id);
    List<PaymentResponse> getAll();
}
