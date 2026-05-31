package com.gogidix.platform.realtime.domain.port.out;

import com.gogidix.platform.realtime.domain.model.RealTimeChannel;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port (repository) for channel persistence.
 */
public interface ChannelRepository {

    /**
     * Save a channel.
     */
    RealTimeChannel save(RealTimeChannel channel);

    /**
     * Find channel by ID.
     */
    Optional<RealTimeChannel> findById(String id);

    /**
     * Find channels for a tenant.
     */
    List<RealTimeChannel> findByTenantId(String tenantId);

    /**
     * Delete a channel.
     */
    void deleteById(String id);

    /**
     * Update subscribers.
     */
    RealTimeChannel updateSubscribers(String id);
}
