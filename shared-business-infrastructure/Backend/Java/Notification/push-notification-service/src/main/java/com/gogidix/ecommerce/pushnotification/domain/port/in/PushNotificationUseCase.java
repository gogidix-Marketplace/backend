package com.gogidix.ecommerce.pushnotification.domain.port.in;

import com.gogidix.ecommerce.pushnotification.application.dto.*;
import java.util.List;

public interface PushNotificationUseCase {
    PushNotificationResponse create(CreatePushNotificationRequest request);
    PushNotificationResponse update(String id, UpdatePushNotificationRequest request);
    void delete(String id);
    PushNotificationResponse getById(String id);
    List<PushNotificationResponse> getAll();
}
