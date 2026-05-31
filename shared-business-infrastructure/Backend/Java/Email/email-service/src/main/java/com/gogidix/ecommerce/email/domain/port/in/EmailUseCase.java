package com.gogidix.ecommerce.email.domain.port.in;

import com.gogidix.ecommerce.email.application.dto.CreateEmailRequest;
import com.gogidix.ecommerce.email.application.dto.UpdateEmailRequest;
import com.gogidix.ecommerce.email.application.dto.EmailResponse;

import java.util.List;

public interface EmailUseCase {

    EmailResponse create(CreateEmailRequest request);

    EmailResponse getById(String id);

    List<EmailResponse> getList(int page, int size);

    EmailResponse update(String id, UpdateEmailRequest request);

    void delete(String id);
}