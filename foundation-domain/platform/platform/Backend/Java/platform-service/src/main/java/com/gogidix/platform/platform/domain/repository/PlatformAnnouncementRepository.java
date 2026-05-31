package com.gogidix.platform.platform.domain.repository;

import com.gogidix.platform.platform.domain.model.PlatformAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for PlatformAnnouncement entity operations.
 */
@Repository
public interface PlatformAnnouncementRepository extends JpaRepository<PlatformAnnouncement, String> {

    /**
     * Find active announcements for a tenant
     */
    List<PlatformAnnouncement> findByTenantIdAndIsActiveTrue(String tenantId);

    /**
     * Find active announcements effective at a given time
     */
    List<PlatformAnnouncement> findByTenantIdAndIsActiveTrueAndEffectiveFromBeforeAndEffectiveUntilAfter(
        String tenantId, LocalDateTime effectiveFrom, LocalDateTime effectiveUntil);

    /**
     * Find announcements by type
     */
    List<PlatformAnnouncement> findByTenantIdAndAnnouncementType(String tenantId, PlatformAnnouncement.AnnouncementType type);

    /**
     * Find announcements by priority
     */
    List<PlatformAnnouncement> findByTenantIdAndPriority(String tenantId, PlatformAnnouncement.Priority priority);

    /**
     * Find expired announcements
     */
    List<PlatformAnnouncement> findByEffectiveUntilBefore(LocalDateTime dateTime);
}
