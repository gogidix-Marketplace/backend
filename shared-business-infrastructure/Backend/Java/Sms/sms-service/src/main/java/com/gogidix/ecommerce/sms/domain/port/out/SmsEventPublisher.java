package com.gogidix.ecommerce.sms.domain.port.out;

import com.gogidix.ecommerce.sms.domain.model.Sms;

public interface SmsEventPublisher {

    void publishCreated(Sms entity);

    void publishUpdated(Sms entity);

    void publishDeleted(Sms entity);
}