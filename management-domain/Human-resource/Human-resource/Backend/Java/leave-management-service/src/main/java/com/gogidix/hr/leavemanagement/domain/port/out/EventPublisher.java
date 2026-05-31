package com.gogidix.hr.leavemanagement.domain.port.out;

import com.gogidix.hr.leavemanagement.domain.event.*;

public interface EventPublisher {
    void publish(Object event);
    void publish(String topic, Object event);
}
