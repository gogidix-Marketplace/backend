package com.gogidix.corporate.website.domain.repository;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.PressRelease;
import com.gogidix.corporate.website.domain.model.Region;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PressReleaseRepository {
    PressRelease save(PressRelease pressRelease);
    Optional<PressRelease> findById(String id);
    Optional<PressRelease> findBySlug(String slug);
    List<PressRelease> findByStatus(ContentStatus status);
    List<PressRelease> findPublishedReleases();
    List<PressRelease> findPublishedReleasesByRegion(Region region);
    List<PressRelease> findRecentReleases(int limit);
    List<PressRelease> searchByKeyword(String keyword, Language language);
    List<PressRelease> findByReleaseDateBetween(String startDate, String endDate);
    List<PressRelease> findNonEmbargoedReleases();
    List<PressRelease> findEmbargoedReleases();
    void deleteById(String id);
    boolean existsBySlug(String slug);
    List<PressRelease> findReleasesScheduledForPublish(LocalDateTime now);
    long countByStatus(ContentStatus status);
}
