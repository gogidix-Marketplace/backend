package com.gogidix.platform.realtime.interfaces.rest;

import com.gogidix.platform.realtime.application.service.ChannelManagementService;
import com.gogidix.platform.realtime.domain.model.ChannelType;
import com.gogidix.platform.realtime.domain.model.RealTimeChannel;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for channel management.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/channels")
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelManagementService channelService;

    @PostMapping
    public ResponseEntity<RealTimeChannel> createChannel(@Valid @RequestBody CreateChannelRequest request) {
        return ResponseEntity.ok(channelService.createChannel(
                request.getTenantId(), request.getName(), request.getDescription(), request.getType()));
    }

    @GetMapping
    public ResponseEntity<List<RealTimeChannel>> listChannels(@RequestParam String tenantId) {
        return ResponseEntity.ok(channelService.listChannels(tenantId));
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<RealTimeChannel> getChannel(@PathVariable String channelId) {
        return ResponseEntity.ok(channelService.getChannel(channelId));
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable String channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{channelId}/subscribers")
    public ResponseEntity<Void> addSubscriber(@PathVariable String channelId,
                                           @RequestBody SubscribeRequest request) {
        channelService.addSubscriber(channelId, request.getUserId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{channelId}/subscribers/{userId}")
    public ResponseEntity<Void> removeSubscriber(@PathVariable String channelId,
                                              @PathVariable String userId) {
        channelService.removeSubscriber(channelId, userId);
        return ResponseEntity.noContent().build();
    }

    @lombok.Data
    public static class CreateChannelRequest {
        private String tenantId;
        private String name;
        private String description;
        private ChannelType type;
    }

    @lombok.Data
    public static class SubscribeRequest {
        private String userId;
    }
}
