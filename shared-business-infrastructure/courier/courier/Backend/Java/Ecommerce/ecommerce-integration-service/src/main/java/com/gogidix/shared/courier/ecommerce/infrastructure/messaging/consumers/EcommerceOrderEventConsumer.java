package com.gogidix.shared.courier.ecommerce.infrastructure.messaging.consumers;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.gogidix.shared.courier.ecommerce.application.service.EcommerceCourierAssignmentService;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.AssignmentPriority;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryType;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.AssignCourierRequest;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.HubDetailsDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.PackageDto;
import com.gogidix.shared.courier.ecommerce.interfaces.rest.dto.ZoneInfoDto;
import com.gogidix.shared.courier.ecommerce.application.command.AssignCourierCommand;
import com.gogidix.shared.courier.ecommerce.application.command.AssignHubLegCommand;
import com.gogidix.shared.courier.ecommerce.domain.valueobject.DeliveryLeg;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EcommerceOrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(EcommerceOrderEventConsumer.class);

    private final EcommerceCourierAssignmentService assignmentService;

    @KafkaListener(topics = "ecommerce.order.ready_for_pickup", groupId = "ecommerce-integration-service")
    public void handleReadyForPickup(Map<String, Object> event) {
        String orderId = (String) event.get("orderId");
        String subOrderId = (String) event.get("subOrderId");
        log.info("Received ready_for_pickup event for order {}", orderId);

        try {
            AssignCourierCommand command = AssignCourierCommand.builder()
                .orderId(orderId)
                .subOrderId(subOrderId)
                .vendorId((String) event.get("vendorId"))
                .deliveryType(DeliveryType.valueOf((String) event.getOrDefault("deliveryType", "TYPE_A")))
                .priority(AssignmentPriority.NORMAL)
                .build();

            assignmentService.assignCourier(command);
            log.info("Auto-assigned courier for ready_for_pickup order {}", orderId);
        } catch (Exception e) {
            log.error("Failed to auto-assign courier for order {}: {}", orderId, e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "ecommerce.order.hub_ready_for_dispatch", groupId = "ecommerce-integration-service")
    public void handleHubReadyForDispatch(Map<String, Object> event) {
        String orderId = (String) event.get("orderId");
        String subOrderId = (String) event.get("subOrderId");
        log.info("Received hub_ready_for_dispatch event for order {}", orderId);

        try {
            AssignHubLegCommand command = AssignHubLegCommand.builder()
                .orderId(orderId)
                .subOrderId(subOrderId)
                .leg(DeliveryLeg.LASTMILE)
                .build();

            assignmentService.assignHubLeg(command);
            log.info("Auto-assigned last-mile courier for hub_ready_for_dispatch order {}", orderId);
        } catch (Exception e) {
            log.error("Failed to assign last-mile courier for order {}: {}", orderId, e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "ecommerce.order.cancelled", groupId = "ecommerce-integration-service")
    public void handleOrderCancelled(Map<String, Object> event) {
        String orderId = (String) event.get("orderId");
        String subOrderId = (String) event.get("subOrderId");
        String reason = (String) event.getOrDefault("reason", "Order cancelled");
        log.info("Received order cancelled event for order {}", orderId);

        try {
            assignmentService.cancelAssignment(orderId, subOrderId, reason);
            log.info("Cancelled courier assignment for order {}", orderId);
        } catch (Exception e) {
            log.error("Failed to cancel assignment for order {}: {}", orderId, e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "ecommerce.order.delivery_rescheduled", groupId = "ecommerce-integration-service")
    public void handleDeliveryRescheduled(Map<String, Object> event) {
        String orderId = (String) event.get("orderId");
        String subOrderId = (String) event.get("subOrderId");
        String newTimeStr = (String) event.get("newScheduledTime");
        log.info("Received delivery_rescheduled event for order {}", orderId);

        try {
            Instant newScheduledTime = newTimeStr != null ? Instant.parse(newTimeStr) : Instant.now().plusSeconds(86400);
            assignmentService.rescheduleAssignment(orderId, subOrderId, newScheduledTime);
            log.info("Rescheduled courier assignment for order {}", orderId);
        } catch (Exception e) {
            log.error("Failed to reschedule assignment for order {}: {}", orderId, e.getMessage(), e);
        }
    }
}
