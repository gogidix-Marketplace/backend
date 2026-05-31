package com.gogidix.ecommerce.paymentgateway.domain.port.in;

import com.gogidix.ecommerce.paymentgateway.application.dto.*;
import java.util.List;

public interface PaymentGatewayUseCase {
    PaymentGatewayResponse create(CreatePaymentGatewayRequest request);
    PaymentGatewayResponse update(String id, UpdatePaymentGatewayRequest request);
    void delete(String id);
    PaymentGatewayResponse getById(String id);
    List<PaymentGatewayResponse> getAll();
}
