package com.gogidix.ecommerce.sms.domain.port.in;

import com.gogidix.ecommerce.sms.application.dto.CreateSmsRequest;
import com.gogidix.ecommerce.sms.application.dto.UpdateSmsRequest;
import com.gogidix.ecommerce.sms.application.dto.SmsResponse;

import java.util.List;

public interface SmsUseCase {

    SmsResponse create(CreateSmsRequest request);

    SmsResponse getById(String id);

    List<SmsResponse> getList(int page, int size);

    SmsResponse update(String id, UpdateSmsRequest request);

    void delete(String id);
}