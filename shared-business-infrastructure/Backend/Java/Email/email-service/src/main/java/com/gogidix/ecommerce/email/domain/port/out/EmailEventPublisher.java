package com.gogidix.ecommerce.email.domain.port.out;

import com.gogidix.ecommerce.email.domain.model.Email;

public interface EmailEventPublisher {

    void publishCreated(Email entity);

    void publishUpdated(Email entity);

    void publishDeleted(Email entity);
}