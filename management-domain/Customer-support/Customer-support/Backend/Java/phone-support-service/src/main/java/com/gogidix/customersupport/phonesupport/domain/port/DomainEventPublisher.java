package com.gogidix.customersupport.phonesupport.domain.port;

import com.gogidix.customersupport.phonesupport.domain.event.*;

public interface DomainEventPublisher {
    void publishCallQueueCreated(CallQueueCreatedEvent event);
    void publishCallQueueUpdated(CallQueueUpdatedEvent event);
    void publishCallQueueDeleted(CallQueueDeletedEvent event);
    void publishPhoneCallCreated(PhoneCallCreatedEvent event);
    void publishPhoneCallUpdated(PhoneCallUpdatedEvent event);
    void publishPhoneCallDeleted(PhoneCallDeletedEvent event);
}
