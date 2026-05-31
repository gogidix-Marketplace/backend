package com.gogidix.platform.realtime.application.service;

import com.gogidix.platform.realtime.domain.model.ChannelType;
import com.gogidix.platform.realtime.domain.model.RealTimeChannel;
import com.gogidix.platform.realtime.domain.port.in.ChannelManagementUseCase;
import com.gogidix.platform.realtime.domain.port.out.ChannelRepository;
import com.gogidix.platform.realtime.domain.port.out.RealTimePublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Application service implementing channel management use cases.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelManagementService implements ChannelManagementUseCase {

    private final ChannelRepository channelRepository;
    private final RealTimePublisher realTimePublisher;

    @Override
    public RealTimeChannel createChannel(String tenantId, String name, String description, ChannelType type) {
        log.debug("Creating channel: {} for tenant: {}", name, tenantId);
        RealTimeChannel channel = RealTimeChannel.create(tenantId, name, description, type);
        channel.setId(UUID.randomUUID().toString());
        RealTimeChannel saved = channelRepository.save(channel);
        realTimePublisher.publishEvent("channel.created", saved);
        return saved;
    }

    @Override
    public RealTimeChannel getChannel(String channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("Channel not found: " + channelId));
    }

    @Override
    public List<RealTimeChannel> listChannels(String tenantId) {
        return channelRepository.findByTenantId(tenantId);
    }

    @Override
    public void deleteChannel(String channelId) {
        log.info("Deleting channel: {}", channelId);
        channelRepository.deleteById(channelId);
        realTimePublisher.publishEvent("channel.deleted", channelId);
    }

    @Override
    public void addSubscriber(String channelId, String subscriberId) {
        RealTimeChannel channel = getChannel(channelId);
        channel.addSubscriber(subscriberId);
        channelRepository.save(channel);
        log.debug("Added subscriber: {} to channel: {}", subscriberId, channelId);
    }

    @Override
    public void removeSubscriber(String channelId, String subscriberId) {
        RealTimeChannel channel = getChannel(channelId);
        channel.removeSubscriber(subscriberId);
        channelRepository.save(channel);
        log.debug("Removed subscriber: {} from channel: {}", subscriberId, channelId);
    }
}
