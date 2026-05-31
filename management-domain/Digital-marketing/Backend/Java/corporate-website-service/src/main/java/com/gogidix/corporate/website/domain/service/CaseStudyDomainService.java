package com.gogidix.corporate.website.domain.service;

import com.gogidix.corporate.website.domain.model.CaseStudy;
import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.repository.CaseStudyRepository;
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
public class CaseStudyDomainService {

    private final CaseStudyRepository caseStudyRepository;

    @CacheEvict(value = "caseStudies", allEntries = true)
    public CaseStudy createCaseStudy(CaseStudy caseStudy) {
        validateCaseStudy(caseStudy);
        return caseStudyRepository.save(caseStudy);
    }

    @CacheEvict(value = "caseStudies", allEntries = true)
    public CaseStudy updateCaseStudy(CaseStudy caseStudy) {
        validateCaseStudy(caseStudy);
        return caseStudyRepository.save(caseStudy);
    }

    @CacheEvict(value = "caseStudies", allEntries = true)
    public void deleteCaseStudy(String id) {
        caseStudyRepository.deleteById(id);
    }

    @Cacheable(value = "caseStudies", key = "#id")
    public Optional<CaseStudy> getCaseStudyById(String id) {
        return caseStudyRepository.findById(id);
    }

    @Cacheable(value = "caseStudies", key = "'slug:' + #slug")
    public Optional<CaseStudy> getCaseStudyBySlug(String slug) {
        return caseStudyRepository.findBySlug(slug);
    }

    @Cacheable(value = "caseStudies", key = "'published:' + #region")
    public List<CaseStudy> getPublishedStudiesByRegion(Region region) {
        return caseStudyRepository.findPublishedStudiesByRegion(region);
    }

    @Cacheable(value = "caseStudies", key = "'featured:' + #region")
    public List<CaseStudy> getFeaturedStudies(Region region) {
        return caseStudyRepository.findFeaturedStudies().stream()
                .filter(study -> study.getAvailableRegions().contains(region))
                .toList();
    }

    @Cacheable(value = "caseStudies", key = "'industry:' + #industry + ':' + #region")
    public List<CaseStudy> getStudiesByIndustry(String industry, Region region) {
        return caseStudyRepository.findByIndustry(industry).stream()
                .filter(study -> study.getAvailableRegions().contains(region))
                .toList();
    }

    public List<CaseStudy> getStudiesByClient(String clientName, Region region) {
        return caseStudyRepository.findByClientName(clientName).stream()
                .filter(study -> study.getAvailableRegions().contains(region))
                .toList();
    }

    public List<CaseStudy> getStudiesByService(String service, Region region) {
        return caseStudyRepository.findByServicesContaining(service).stream()
                .filter(study -> study.getAvailableRegions().contains(region))
                .toList();
    }

    public List<CaseStudy> getStudiesByTechnology(String technology, Region region) {
        return caseStudyRepository.findByTechnologiesContaining(technology).stream()
                .filter(study -> study.getAvailableRegions().contains(region))
                .toList();
    }

    public List<CaseStudy> searchCaseStudies(String keyword, Language language) {
        return caseStudyRepository.searchByKeyword(keyword, language);
    }

    public List<CaseStudy> getRelatedStudies(String caseStudyId, int limit) {
        return caseStudyRepository.findRelatedStudies(caseStudyId, limit);
    }

    public List<CaseStudy> getScheduledStudies() {
        return caseStudyRepository.findStudiesScheduledForPublish(LocalDateTime.now());
    }

    @CacheEvict(value = "caseStudies", allEntries = true)
    public CaseStudy publishCaseStudy(String id) {
        CaseStudy study = caseStudyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Case study not found: " + id));
        study.setStatus(ContentStatus.PUBLISHED);
        study.setPublishDate(LocalDateTime.now());
        return caseStudyRepository.save(study);
    }

    @CacheEvict(value = "caseStudies", allEntries = true)
    public void incrementViewCount(String caseStudyId) {
        caseStudyRepository.incrementViewCount(caseStudyId);
    }

    private void validateCaseStudy(CaseStudy caseStudy) {
        if (caseStudy.getSlug() == null || caseStudy.getSlug().isBlank()) {
            throw new IllegalArgumentException("Case study slug cannot be null or blank");
        }
        if (caseStudy.getLocalizedContent() == null || caseStudy.getLocalizedContent().isEmpty()) {
            throw new IllegalArgumentException("Case study must have at least one localized content");
        }
        if (caseStudy.getClientName() == null || caseStudy.getClientName().isBlank()) {
            throw new IllegalArgumentException("Case study must have a client name");
        }
    }

    public long getCaseStudyCount() {
        return caseStudyRepository.countByStatus(ContentStatus.PUBLISHED);
    }
}
