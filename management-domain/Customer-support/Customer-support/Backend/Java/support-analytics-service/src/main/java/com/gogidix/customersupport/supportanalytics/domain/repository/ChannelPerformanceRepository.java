package com.gogidix.customersupport.supportanalytics.domain.repository;

import com.gogidix.customersupport.supportanalytics.domain.model.ChannelPerformance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChannelPerformanceRepository extends MongoRepository<ChannelPerformance, String> {

    List<ChannelPerformance> findByTenantId(String tenantId);

    List<ChannelPerformance> findByTenantIdAndChannelType(String tenantId, ChannelPerformance.ChannelType channelType);

    List<ChannelPerformance> findByTenantIdAndMetricDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<ChannelPerformance> findByTenantIdAndChannelTypeAndMetricDateBetween(String tenantId,
                                                                               ChannelPerformance.ChannelType channelType,
                                                                               LocalDate startDate,
                                                                               LocalDate endDate);

    Optional<ChannelPerformance> findFirstByTenantIdAndChannelTypeOrderByMetricDateDesc(String tenantId,
                                                                                         ChannelPerformance.ChannelType channelType);

    List<ChannelPerformance> findByTenantIdOrderByMetricDateDesc(String tenantId);

    void deleteByTenantIdAndId(String tenantId, String id);
}
