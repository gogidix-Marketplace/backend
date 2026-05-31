package com.gogidix.corporate.website.domain.repository;

import com.gogidix.corporate.website.domain.model.CaseStudy;
import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CaseStudyRepository {
    CaseStudy save(CaseStudy caseStudy);
    Optional<CaseStudy> findById(String id);
    Optional<CaseStudy> findBySlug(String slug);
    List<CaseStudy> findByStatus(ContentStatus status);
    List<CaseStudy> findPublishedStudies();
    List<CaseStudy> findPublishedStudiesByRegion(Region region);
    List<CaseStudy> findFeaturedStudies();
    List<CaseStudy> findByIndustry(String industry);
    List<CaseStudy> findByClientName(String clientName);
    List<CaseStudy> findByServicesContaining(String service);
    List<CaseStudy> findByTechnologiesContaining(String technology);
    List<CaseStudy> findByTagsContaining(String tag);
    List<CaseStudy> searchByKeyword(String keyword, Language language);
    List<CaseStudy> findRelatedStudies(String caseStudyId, int limit);
    void deleteById(String id);
    boolean existsBySlug(String slug);
    List<CaseStudy> findStudiesScheduledForPublish(LocalDateTime now);
    void incrementViewCount(String caseStudyId);
    long countByStatus(ContentStatus status);
    long countByIndustry(String industry);
}
