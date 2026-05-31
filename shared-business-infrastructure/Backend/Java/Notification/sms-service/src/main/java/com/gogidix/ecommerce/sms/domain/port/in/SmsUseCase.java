package com.gogidix.ecommerce.sms.domain.port.in;

import com.gogidix.ecommerce.sms.application.dto.*;
import java.util.List;

public interface SmsUseCase {
    SmsResponse create(CreateSmsRequest request);
    SmsResponse update(String id, UpdateSmsRequest request);
    void delete(String id);
    SmsResponse getById(String id);
    List<SmsResponse> getAll();
}
