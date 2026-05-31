package com.gogidix.ecommerce.email.domain.port.in;

import com.gogidix.ecommerce.email.application.dto.*;
import java.util.List;

public interface EmailUseCase {
    EmailResponse create(CreateEmailRequest request);
    EmailResponse update(String id, UpdateEmailRequest request);
    void delete(String id);
    EmailResponse getById(String id);
    List<EmailResponse> getAll();
}
