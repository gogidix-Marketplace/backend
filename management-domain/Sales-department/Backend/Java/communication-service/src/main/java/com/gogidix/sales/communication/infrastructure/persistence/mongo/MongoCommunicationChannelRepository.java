package com.gogidix.sales.communication.infrastructure.persistence.mongo;

import com.gogidix.sales.communication.domain.model.CommunicationChannel;
import com.gogidix.sales.communication.domain.repository.CommunicationChannelRepository;
import com.gogidix.sales.communication.shared.requestcontext.RequestContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB Repository Implementation - CommunicationChannel
 * Implements communication channel persistence with mandatory tenant filtering
 */
@Repository
@RequiredArgsConstructor
@Slf4j
public class MongoCommunicationChannelRepository implements CommunicationChannelRepository {

    private final MongoTemplate mongoTemplate;

    @Override
    public CommunicationChannel save(CommunicationChannel channel) {
        log.debug("Saving channel: {} for tenant: {}", channel.getChannelId(), channel.getTenantId());
        return mongoTemplate.save(channel);
    }

    @Override
    public List<CommunicationChannel> saveAll(List<CommunicationChannel> channels) {
        return channels.stream().map(mongoTemplate::save).toList();
    }

    @Override
    public Optional<CommunicationChannel> findById(String id) {
        String tenantId = RequestContextHolder.getTenantId();
        Query query = Query.query(
                Criteria.where("id").is(id)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CommunicationChannel.class));
    }

    @Override
    public Optional<CommunicationChannel> findByChannelIdAndTenantId(String channelId, String tenantId) {
        Query query = Query.query(
                Criteria.where("channelId").is(channelId)
                        .and("tenantId").is(tenantId)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CommunicationChannel.class));
    }

    @Override
    public Optional<CommunicationChannel> findByTenantIdAndTypeAndIsDefault(
            String tenantId, CommunicationChannel.ChannelType type, Boolean isDefault) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
                        .and("isDefault").is(isDefault)
        );
        return Optional.ofNullable(mongoTemplate.findOne(query, CommunicationChannel.class));
    }

    @Override
    public List<CommunicationChannel> findByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, CommunicationChannel.class);
    }

    @Override
    public List<CommunicationChannel> findByTenantIdAndStatus(String tenantId, CommunicationChannel.ChannelStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.find(query, CommunicationChannel.class);
    }

    @Override
    public List<CommunicationChannel> findByTenantIdAndType(String tenantId, CommunicationChannel.ChannelType type) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("type").is(type)
        );
        return mongoTemplate.find(query, CommunicationChannel.class);
    }

    @Override
    public List<CommunicationChannel> findByTenantIdAndIsDefault(String tenantId, Boolean isDefault) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("isDefault").is(isDefault)
        );
        return mongoTemplate.find(query, CommunicationChannel.class);
    }

    @Override
    public boolean existsByChannelIdAndTenantId(String channelId, String tenantId) {
        Query query = Query.query(
                Criteria.where("channelId").is(channelId)
                        .and("tenantId").is(tenantId)
        );
        return mongoTemplate.exists(query, CommunicationChannel.class);
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), CommunicationChannel.class);
    }

    @Override
    public void deleteByChannelIdAndTenantId(String channelId, String tenantId) {
        Query query = Query.query(
                Criteria.where("channelId").is(channelId)
                        .and("tenantId").is(tenantId)
        );
        mongoTemplate.remove(query, CommunicationChannel.class);
    }

    @Override
    public void deleteAllByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        mongoTemplate.remove(query, CommunicationChannel.class);
    }

    @Override
    public long countByTenantId(String tenantId) {
        Query query = Query.query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.count(query, CommunicationChannel.class);
    }

    @Override
    public long countByTenantIdAndStatus(String tenantId, CommunicationChannel.ChannelStatus status) {
        Query query = Query.query(
                Criteria.where("tenantId").is(tenantId)
                        .and("status").is(status)
        );
        return mongoTemplate.count(query, CommunicationChannel.class);
    }
}
