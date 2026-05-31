package com.gogidix.shared.courier.ecommerce.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking;
import com.gogidix.shared.courier.ecommerce.domain.entity.EcommerceDeliveryTracking.HubStage;
import com.gogidix.shared.courier.ecommerce.domain.repository.EcommerceTrackingRepository;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.HubStageStatus;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.StageCompletionStatus;

@Service
@Transactional(readOnly = true)
public class EcommerceTrackingService {

    private static final Logger log = LoggerFactory.getLogger(EcommerceTrackingService.class);

    private final EcommerceTrackingRepository trackingRepository;

    public EcommerceTrackingService(EcommerceTrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    public EcommerceDeliveryTracking getTrackingByOrderId(String orderId) {
        log.debug("Fetching tracking for order {}", orderId);
        return trackingRepository.findByOrderId(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Tracking not found for order " + orderId));
    }

    @Transactional
    public void updateStage(String orderId, HubStageStatus stage, StageCompletionStatus completionStatus) {
        EcommerceDeliveryTracking tracking = getTrackingByOrderId(orderId);
        HubStage hubStage = new HubStage(stage, completionStatus);
        tracking.addStage(hubStage);
        tracking.updateCurrentStage(stage);
        trackingRepository.save(tracking);
        log.info("Updated tracking stage to {} for order {}", stage, orderId);
    }

    @Transactional
    public void markHubArrival(String orderId, String hubName) {
        EcommerceDeliveryTracking tracking = getTrackingByOrderId(orderId);
        HubStage hubStage = new HubStage(HubStageStatus.AT_ORIGIN_HUB, StageCompletionStatus.COMPLETED);
        hubStage.setHubName(hubName);
        tracking.addStage(hubStage);
        tracking.updateCurrentStage(HubStageStatus.AT_ORIGIN_HUB);
        trackingRepository.save(tracking);
        log.info("Marked hub arrival at {} for order {}", hubName, orderId);
    }

    @Transactional
    public void markDelivered(String orderId) {
        EcommerceDeliveryTracking tracking = getTrackingByOrderId(orderId);
        HubStage hubStage = new HubStage(HubStageStatus.DELIVERED, StageCompletionStatus.COMPLETED);
        tracking.addStage(hubStage);
        tracking.updateCurrentStage(HubStageStatus.DELIVERED);
        trackingRepository.save(tracking);
        log.info("Marked delivered for order {}", orderId);
    }
}
