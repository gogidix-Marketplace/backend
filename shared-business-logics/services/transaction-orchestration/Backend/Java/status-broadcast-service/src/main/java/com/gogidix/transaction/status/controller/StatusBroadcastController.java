package com.gogidix.transaction.status.controller;

import com.gogidix.transaction.status.domain.entity.Subscriber;
import com.gogidix.transaction.status.domain.entity.Subscriber.SubscriptionStatus;
import com.gogidix.transaction.status.domain.repository.SubscriberRepository;
import com.gogidix.transaction.status.service.StatusBroadcastService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/status-broadcast")
@RequiredArgsConstructor
public class StatusBroadcastController {

    private final SubscriberRepository subscriberRepository;
    private final StatusBroadcastService broadcastService;

    @GetMapping("/subscribers")
    public ResponseEntity<List<Subscriber>> getAllSubscribers() {
        List<Subscriber> subscribers = subscriberRepository.findByStatus(SubscriptionStatus.ACTIVE);
        return ResponseEntity.ok(subscribers);
    }

    @GetMapping("/subscribers/{id}")
    public ResponseEntity<Subscriber> getSubscriber(@PathVariable UUID id) {
        return subscriberRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/subscribers/user/{userId}")
    public ResponseEntity<List<Subscriber>> getSubscribersByUser(@PathVariable String userId) {
        List<Subscriber> subscribers = subscriberRepository.findByUserIdAndStatus(
            userId, SubscriptionStatus.ACTIVE);
        return ResponseEntity.ok(subscribers);
    }

    @DeleteMapping("/subscribers/{sessionId}")
    public ResponseEntity<Void> disconnectSubscriber(@PathVariable String sessionId) {
        subscriberRepository.deleteBySessionId(sessionId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Void> broadcastStatus(@Valid @RequestBody StatusBroadcastRequest request) {
        broadcastService.broadcastStatusUpdate(
            request.getTransactionId(),
            request.getStatus(),
            request.getData()
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/broadcast/all")
    public ResponseEntity<Void> broadcastToAll(@Valid @RequestBody BroadcastAllRequest request) {
        broadcastService.broadcastToAll(request.getMessageType(), request.getData());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<BroadcastStats> getStats() {
        long activeSubscribers = broadcastService.getActiveSubscriberCount();
        long totalSubscribers = subscriberRepository.count();

        BroadcastStats stats = new BroadcastStats();
        stats.setActiveSubscribers(activeSubscribers);
        stats.setTotalSubscribers(totalSubscribers);

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Status Broadcast Service is running");
    }

    @Getter
    @Setter
    public static class StatusBroadcastRequest {
        @NotBlank(message = "Transaction ID is required")
        private String transactionId;

        @NotBlank(message = "Status is required")
        private String status;

        private Map<String, Object> data;
    }

    @Getter
    @Setter
    public static class BroadcastAllRequest {
        @NotBlank(message = "Message type is required")
        private String messageType;

        private Map<String, Object> data;
    }

    @Getter
    @Setter
    public static class BroadcastStats {
        private Long activeSubscribers;
        private Long totalSubscribers;
    }
}
