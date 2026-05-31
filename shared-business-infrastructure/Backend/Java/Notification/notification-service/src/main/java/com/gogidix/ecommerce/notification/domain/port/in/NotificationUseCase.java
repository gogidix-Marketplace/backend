package com.gogidix.ecommerce.notification.domain.port.in;

import com.gogidix.ecommerce.notification.application.dto.*;
import java.util.List;

public interface NotificationUseCase {
    NotificationResponse create(CreateNotificationRequest request);
    NotificationResponse update(String id, UpdateNotificationRequest request);
    void delete(String id);
    NotificationResponse getById(String id);
    List<NotificationResponse> getAll();
}
