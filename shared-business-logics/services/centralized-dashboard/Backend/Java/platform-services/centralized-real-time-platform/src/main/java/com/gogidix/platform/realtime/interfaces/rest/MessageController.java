package com.gogidix.platform.realtime.interfaces.rest;

import com.gogidix.platform.realtime.application.service.MessageBroadcastService;
import com.gogidix.platform.realtime.domain.model.RealTimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for message broadcast.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageBroadcastService messageService;

    @PostMapping("/broadcast")
    public ResponseEntity<Void> broadcast(@Valid @RequestBody BroadcastRequest request) {
        RealTimeMessage message = RealTimeMessage.create(
                request.getTenantId(), request.getChannelId(), request.getUserId(),
                request.getType(), request.getPayload(), request.getMetadata());
        messageService.broadcast(request.getChannelId(), message);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/direct")
    public ResponseEntity<Void> sendDirect(@Valid @RequestBody DirectMessageRequest request) {
        RealTimeMessage message = RealTimeMessage.create(
                request.getTenantId(), null, request.getUserId(),
                request.getType(), request.getPayload(), request.getMetadata());
        messageService.sendDirectMessage(request.getTargetUserId(), message);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/broadcast-multiple")
    public ResponseEntity<Void> broadcastMultiple(@Valid @RequestBody BroadcastMultipleRequest request) {
        RealTimeMessage message = RealTimeMessage.create(
                request.getTenantId(), null, request.getUserId(),
                request.getType(), request.getPayload(), request.getMetadata());
        messageService.broadcastToChannels(request.getChannelIds(), message);
        return ResponseEntity.ok().build();
    }

    @lombok.Data
    public static class BroadcastRequest {
        private String tenantId;
        private String channelId;
        private String userId;
        private String type;
        private String payload;
        private java.util.Map<String, String> metadata;
    }

    @lombok.Data
    public static class DirectMessageRequest {
        private String tenantId;
        private String userId;
        private String targetUserId;
        private String type;
        private String payload;
        private java.util.Map<String, String> metadata;
    }

    @lombok.Data
    public static class BroadcastMultipleRequest {
        private String tenantId;
        private String userId;
        private List<String> channelIds;
        private String type;
        private String payload;
        private java.util.Map<String, String> metadata;
    }
}
