package com.gogidix.customersupport.supportanalytics.domain.repository;

import com.gogidix.customersupport.supportanalytics.domain.model.TicketTrend;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketTrendRepository extends MongoRepository<TicketTrend, String> {

    List<TicketTrend> findByTenantId(String tenantId);

    List<TicketTrend> findByTenantIdOrderByTrendDateDesc(String tenantId);

    List<TicketTrend> findByTenantIdAndTrendDateBetween(String tenantId, LocalDate startDate, LocalDate endDate);

    List<TicketTrend> findByTenantIdAndPeriodType(String tenantId, TicketTrend.PeriodType periodType);

    List<TicketTrend> findByTenantIdAndPeriodTypeAndTrendDateBetween(String tenantId,
                                                                      TicketTrend.PeriodType periodType,
                                                                      LocalDate startDate,
                                                                      LocalDate endDate);

    TicketTrend findFirstByTenantIdOrderByTrendDateDesc(String tenantId);

    void deleteByTenantIdAndId(String tenantId, String id);
}
