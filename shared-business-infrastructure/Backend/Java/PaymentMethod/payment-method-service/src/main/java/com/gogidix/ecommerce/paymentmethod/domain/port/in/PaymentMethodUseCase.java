package com.gogidix.ecommerce.paymentmethod.domain.port.in;

import com.gogidix.ecommerce.paymentmethod.application.dto.CreatePaymentMethodRequest;
import com.gogidix.ecommerce.paymentmethod.application.dto.UpdatePaymentMethodRequest;
import com.gogidix.ecommerce.paymentmethod.application.dto.PaymentMethodResponse;

import java.util.List;

public interface PaymentMethodUseCase {

    PaymentMethodResponse create(CreatePaymentMethodRequest request);

    PaymentMethodResponse getById(String id);

    List<PaymentMethodResponse> getList(int page, int size);

    PaymentMethodResponse update(String id, UpdatePaymentMethodRequest request);

    void delete(String id);
}