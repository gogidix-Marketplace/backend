package com.gogidix.ecommerce.pushnotification.domain.port.in;

import com.gogidix.ecommerce.pushnotification.application.dto.CreatePushNotificationRequest;
import com.gogidix.ecommerce.pushnotification.application.dto.UpdatePushNotificationRequest;
import com.gogidix.ecommerce.pushnotification.application.dto.PushNotificationResponse;

import java.util.List;

public interface PushNotificationUseCase {

    PushNotificationResponse create(CreatePushNotificationRequest request);

    PushNotificationResponse getById(String id);

    List<PushNotificationResponse> getList(int page, int size);

    PushNotificationResponse update(String id, UpdatePushNotificationRequest request);

    void delete(String id);
}