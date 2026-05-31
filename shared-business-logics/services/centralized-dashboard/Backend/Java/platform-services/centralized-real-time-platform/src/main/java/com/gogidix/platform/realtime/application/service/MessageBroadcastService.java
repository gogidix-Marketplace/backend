package com.gogidix.platform.realtime.application.service;

import com.gogidix.platform.realtime.domain.model.RealTimeMessage;
import com.gogidix.platform.realtime.domain.port.in.MessageBroadcastUseCase;
import com.gogidix.platform.realtime.domain.port.out.RealTimePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service implementing message broadcast use cases.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageBroadcastService implements MessageBroadcastUseCase {

    private final RealTimePublisher realTimePublisher;

    @Override
    public void broadcast(String channelId, RealTimeMessage message) {
        log.debug("Broadcasting message to channel: {}", channelId);
        realTimePublisher.publish(channelId, message);
    }

    @Override
    public void sendDirectMessage(String userId, RealTimeMessage message) {
        log.debug("Sending direct message to user: {}", userId);
        realTimePublisher.publish("user:" + userId, message);
    }

    @Override
    public void broadcastToChannels(List<String> channelIds, RealTimeMessage message) {
        log.debug("Broadcasting message to {} channels", channelIds.size());
        channelIds.forEach(channelId -> broadcast(channelId, message));
    }
}
