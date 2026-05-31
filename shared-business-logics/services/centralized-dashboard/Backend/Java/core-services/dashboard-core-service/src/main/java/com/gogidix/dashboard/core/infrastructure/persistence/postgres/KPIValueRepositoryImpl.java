package com.gogidix.dashboard.core.infrastructure.persistence.postgres;

import com.gogidix.dashboard.core.domain.model.KPIValue;
import com.gogidix.dashboard.core.domain.port.out.KPIValueRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * JPA implementation of KPIValueRepository.
 */
@Slf4j
@Repository
public class KPIValueRepositoryImpl implements KPIValueRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public KPIValueRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public KPIValue save(KPIValue value) {
        if (value.getId() == null) {
            entityManager.persist(value);
            return value;
        } else {
            return entityManager.merge(value);
        }
    }

    @Override
    public Optional<KPIValue> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(KPIValue.class, id));
    }

    @Override
    public List<KPIValue> findByKpiId(UUID kpiId) {
        return entityManager.createQuery(
                        "SELECT v FROM KPIValue v WHERE v.kpi.id = :kpiId ORDER BY v.recordedAt DESC", KPIValue.class)
                .setParameter("kpiId", kpiId)
                .getResultList();
    }

    @Override
    public List<KPIValue> findByKpiIdAndRecordedAtBetween(UUID kpiId, LocalDateTime start, LocalDateTime end) {
        return entityManager.createQuery(
                        "SELECT v FROM KPIValue v WHERE v.kpi.id = :kpiId AND v.recordedAt BETWEEN :start AND :end ORDER BY v.recordedAt DESC", KPIValue.class)
                .setParameter("kpiId", kpiId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }

    @Override
    public Optional<KPIValue> findFirstByKpiIdOrderByRecordedAtDesc(UUID kpiId) {
        List<KPIValue> results = entityManager.createQuery(
                        "SELECT v FROM KPIValue v WHERE v.kpi.id = :kpiId ORDER BY v.recordedAt DESC", KPIValue.class)
                .setParameter("kpiId", kpiId)
                .setMaxResults(1)
                .getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public List<KPIValue> findByKpiIdOrderByRecordedAtDesc(UUID kpiId, int limit) {
        return entityManager.createQuery(
                        "SELECT v FROM KPIValue v WHERE v.kpi.id = :kpiId ORDER BY v.recordedAt DESC", KPIValue.class)
                .setParameter("kpiId", kpiId)
                .setMaxResults(limit)
                .getResultList();
    }

    @Override
    public void deleteByKpiId(UUID kpiId) {
        entityManager.createQuery(
                        "DELETE FROM KPIValue v WHERE v.kpi.id = :kpiId")
                .setParameter("kpiId", kpiId)
                .executeUpdate();
    }

    @Override
    public void deleteByRecordedAtBefore(LocalDateTime date) {
        entityManager.createQuery(
                        "DELETE FROM KPIValue v WHERE v.recordedAt < :date")
                .setParameter("date", date)
                .executeUpdate();
    }
}
