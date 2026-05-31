package com.gogidix.sales.communication.domain.repository;

import com.gogidix.sales.communication.domain.model.CommunicationChannel;

import java.util.List;
import java.util.Optional;

/**
 * Communication Channel Repository Interface (Port)
 * Defines the contract for communication channel persistence operations
 */
public interface CommunicationChannelRepository {

    CommunicationChannel save(CommunicationChannel channel);

    List<CommunicationChannel> saveAll(List<CommunicationChannel> channels);

    Optional<CommunicationChannel> findById(String id);

    Optional<CommunicationChannel> findByChannelIdAndTenantId(String channelId, String tenantId);

    Optional<CommunicationChannel> findByTenantIdAndTypeAndIsDefault(
            String tenantId, CommunicationChannel.ChannelType type, Boolean isDefault);

    List<CommunicationChannel> findByTenantId(String tenantId);

    List<CommunicationChannel> findByTenantIdAndStatus(String tenantId, CommunicationChannel.ChannelStatus status);

    List<CommunicationChannel> findByTenantIdAndType(String tenantId, CommunicationChannel.ChannelType type);

    List<CommunicationChannel> findByTenantIdAndIsDefault(String tenantId, Boolean isDefault);

    boolean existsByChannelIdAndTenantId(String channelId, String tenantId);

    void deleteById(String id);

    void deleteByChannelIdAndTenantId(String channelId, String tenantId);

    void deleteAllByTenantId(String tenantId);

    long countByTenantId(String tenantId);

    long countByTenantIdAndStatus(String tenantId, CommunicationChannel.ChannelStatus status);
}
