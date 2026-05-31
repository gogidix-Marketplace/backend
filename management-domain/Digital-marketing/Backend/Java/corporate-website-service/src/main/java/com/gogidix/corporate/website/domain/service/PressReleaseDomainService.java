package com.gogidix.corporate.website.domain.service;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.PressRelease;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.repository.PressReleaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PressReleaseDomainService {

    private final PressReleaseRepository pressReleaseRepository;

    @CacheEvict(value = "pressReleases", allEntries = true)
    public PressRelease createPressRelease(PressRelease pressRelease) {
        validatePressRelease(pressRelease);
        return pressReleaseRepository.save(pressRelease);
    }

    @CacheEvict(value = "pressReleases", allEntries = true)
    public PressRelease updatePressRelease(PressRelease pressRelease) {
        validatePressRelease(pressRelease);
        return pressReleaseRepository.save(pressRelease);
    }

    @CacheEvict(value = "pressReleases", allEntries = true)
    public void deletePressRelease(String id) {
        pressReleaseRepository.deleteById(id);
    }

    @Cacheable(value = "pressReleases", key = "#id")
    public Optional<PressRelease> getPressReleaseById(String id) {
        return pressReleaseRepository.findById(id);
    }

    @Cacheable(value = "pressReleases", key = "'slug:' + #slug")
    public Optional<PressRelease> getPressReleaseBySlug(String slug) {
        return pressReleaseRepository.findBySlug(slug);
    }

    @Cacheable(value = "pressReleases", key = "'published:' + #region")
    public List<PressRelease> getPublishedReleasesByRegion(Region region) {
        return pressReleaseRepository.findPublishedReleasesByRegion(region);
    }

    @Cacheable(value = "pressReleases", key = "'recent:' + #limit")
    public List<PressRelease> getRecentReleases(int limit) {
        return pressReleaseRepository.findRecentReleases(limit);
    }

    public List<PressRelease> searchPressReleases(String keyword, Language language) {
        return pressReleaseRepository.searchByKeyword(keyword, language);
    }

    public List<PressRelease> getReleasesByDateRange(String startDate, String endDate) {
        return pressReleaseRepository.findByReleaseDateBetween(startDate, endDate);
    }

    public List<PressRelease> getNonEmbargoedReleases() {
        return pressReleaseRepository.findNonEmbargoedReleases();
    }

    public List<PressRelease> getEmbargoedReleases() {
        return pressReleaseRepository.findEmbargoedReleases();
    }

    public List<PressRelease> getScheduledReleases() {
        return pressReleaseRepository.findReleasesScheduledForPublish(LocalDateTime.now());
    }

    @CacheEvict(value = "pressReleases", allEntries = true)
    public PressRelease publishPressRelease(String id) {
        PressRelease release = pressReleaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Press release not found: " + id));
        release.setStatus(ContentStatus.PUBLISHED);
        release.setPublishDate(LocalDateTime.now());
        return pressReleaseRepository.save(release);
    }

    private void validatePressRelease(PressRelease pressRelease) {
        if (pressRelease.getSlug() == null || pressRelease.getSlug().isBlank()) {
            throw new IllegalArgumentException("Press release slug cannot be null or blank");
        }
        if (pressRelease.getLocalizedContent() == null || pressRelease.getLocalizedContent().isEmpty()) {
            throw new IllegalArgumentException("Press release must have at least one localized content");
        }
    }

    public long getPressReleaseCount() {
        return pressReleaseRepository.countByStatus(ContentStatus.PUBLISHED);
    }
}
