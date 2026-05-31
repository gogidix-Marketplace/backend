package com.gogidix.ecommerce.search.domain.port.out;

import com.gogidix.ecommerce.search.domain.event.SearchDomainEvent;

public interface SearchEventPublisher {
    void publish(SearchDomainEvent event);
}
