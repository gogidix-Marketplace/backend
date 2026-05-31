package com.gogidix.management.executive.analytics.domain.repository;

import com.gogidix.management.executive.analytics.domain.model.KPI;
import com.gogidix.management.shared.infrastructure.persistence.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.CountQuery;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface KPIRepository extends BaseRepository<KPI> {

    List<KPI> findByCategory(String category);

    Page<KPI> findByCategory(String category, Pageable pageable);

    List<KPI> findByExecutiveLevel(String executiveLevel);

    List<KPI> findByExecutiveLevelAndVisibleTrue(String executiveLevel);

    List<KPI> findByPeriod(String period);

    List<KPI> findByCategoryAndPeriod(String category, String period);

    List<KPI> findByStatus(String status);

    @Query("{ 'status': { $in: ['AT_RISK', 'BEHIND'] } }")
    List<KPI> findNeedingAttention();

    List<KPI> findByName(String name);

    @Query("{ 'name': ?0, 'visible': true }")
    Optional<KPI> findLatestByName(String name);

    @Query("{ 'dataSources': { $in: [?0] } }")
    List<KPI> findByDataSource(String dataSource);

    List<KPI> findByIsCalculatedTrue();

    List<KPI> findByIsCalculatedFalse();

    @CountQuery("{ 'category': ?0 }")
    long countByCategory(String category);

    @Query("{ 'isCalculated': true, 'lastCalculatedAt': { $lt: ?0 } }")
    List<KPI> findNeedingRecalculation(Instant threshold);

    List<KPI> findByExecutiveLevelAndCategory(String executiveLevel, String category);

    List<KPI> findByExecutiveLevelAndStatus(String executiveLevel, String status);

    @Query("{ 'visible': true, 'executiveLevel': { $in: [?0, 'ALL'] } }")
    List<KPI> findVisibleForDashboard(String executiveLevel);

    @Query("{ 'percentChange': { $gt: ?0 } }")
    List<KPI> findWithSignificantChange(BigDecimal threshold);

    @Query("{ 'lastCalculatedAt': { $gte: ?0, $lte: ?1 } }")
    List<KPI> findByPeriodRange(Instant startDate, Instant endDate);

    @Query(value = "{ 'category': { $exists: true } }", fields = "{ 'category' : 1 }")
    List<String> findDistinctCategories();

    @Query(value = "{ 'executiveLevel': { $exists: true } }", fields = "{ 'executiveLevel' : 1 }")
    List<String> findDistinctExecutiveLevels();

    @Query(value = "{ 'period': { $exists: true } }", fields = "{ 'period' : 1 }")
    List<String> findDistinctPeriods();

    @Query("{ 'category': { $in: ?0 } }")
    List<KPI> findByCategoryIn(List<String> categories);

    @Query("{ $or: [ " +
            "{ 'name': { $regex: ?0, $options: 'i' } }, " +
            "{ 'category': { $regex: ?0, $options: 'i' } } " +
            "] }")
    List<KPI> search(String searchTerm);

    Optional<KPI> findByTenantIdAndCategoryAndPeriod(String tenantId, String category, String period);
}
