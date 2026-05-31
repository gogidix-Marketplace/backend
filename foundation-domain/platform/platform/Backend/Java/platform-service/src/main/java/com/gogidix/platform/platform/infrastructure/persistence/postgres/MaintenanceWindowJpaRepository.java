package com.gogidix.platform.platform.infrastructure.persistence.postgres;

import com.gogidix.platform.platform.domain.model.MaintenanceWindow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for MaintenanceWindow entity.
 */
@Repository
public interface MaintenanceWindowJpaRepository extends JpaRepository<MaintenanceWindow, String> {

    List<MaintenanceWindow> findByTenantId(String tenantId);

    @Query("SELECT mw FROM MaintenanceWindow mw WHERE mw.tenantId = :tenantId " +
           "AND mw.isActive = true " +
           "AND mw.startTime <= :currentTime " +
           "AND mw.endTime >= :currentTime")
    List<MaintenanceWindow> findActiveMaintenanceWindows(
        @Param("tenantId") String tenantId,
        @Param("currentTime") LocalDateTime currentTime
    );

    List<MaintenanceWindow> findByIsActive(boolean isActive);

    @Query("SELECT mw FROM MaintenanceWindow mw WHERE mw.startTime > :currentTime")
    List<MaintenanceWindow> findUpcomingMaintenanceWindows(@Param("currentTime") LocalDateTime currentTime);
}
