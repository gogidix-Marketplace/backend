package com.gogidix.ecommerce.paymentgateway.domain.port.in;

import com.gogidix.ecommerce.paymentgateway.application.dto.CreatePaymentGatewayRequest;
import com.gogidix.ecommerce.paymentgateway.application.dto.UpdatePaymentGatewayRequest;
import com.gogidix.ecommerce.paymentgateway.application.dto.PaymentGatewayResponse;

import java.util.List;

public interface PaymentGatewayUseCase {

    PaymentGatewayResponse create(CreatePaymentGatewayRequest request);

    PaymentGatewayResponse getById(String id);

    List<PaymentGatewayResponse> getList(int page, int size);

    PaymentGatewayResponse update(String id, UpdatePaymentGatewayRequest request);

    void delete(String id);
}