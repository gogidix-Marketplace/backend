package com.gogidix.platform.platform.infrastructure.persistence.postgres;

import com.gogidix.platform.platform.domain.model.PlatformAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for PlatformAnnouncement entity.
 */
@Repository
public interface PlatformAnnouncementJpaRepository extends JpaRepository<PlatformAnnouncement, String> {

    List<PlatformAnnouncement> findByTenantIdOrderByCreatedAtDesc(String tenantId);

    @Query("SELECT pa FROM PlatformAnnouncement pa WHERE pa.tenantId = :tenantId " +
           "AND pa.isActive = true " +
           "AND pa.publishAt <= :currentTime " +
           "AND (pa.expiresAt IS NULL OR pa.expiresAt > :currentTime)")
    List<PlatformAnnouncement> findActiveAnnouncements(
        @Param("tenantId") String tenantId,
        @Param("currentTime") LocalDateTime currentTime
    );

    List<PlatformAnnouncement> findByIsActiveOrderByCreatedAtDesc(boolean isActive);

    @Query("SELECT pa FROM PlatformAnnouncement pa WHERE pa.isGlobal = true " +
           "AND pa.isActive = true " +
           "AND pa.publishAt <= :currentTime " +
           "AND (pa.expiresAt IS NULL OR pa.expiresAt > :currentTime)")
    List<PlatformAnnouncement> findGlobalAnnouncements(@Param("currentTime") LocalDateTime currentTime);
}
