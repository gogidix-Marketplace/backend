package com.gogidix.corporate.website.domain.repository;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Job;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JobRepository {
    Job save(Job job);
    Optional<Job> findById(String id);
    Optional<Job> findByJobKey(String jobKey);
    Optional<Job> findBySlug(String slug);
    List<Job> findByStatus(ContentStatus status);
    List<Job> findOpenJobs();
    List<Job> findOpenJobsByRegion(Region region);
    List<Job> findFeaturedJobs();
    List<Job> findByDepartment(String department);
    List<Job> findByLocation(String location);
    List<Job> findByEmploymentType(String employmentType);
    List<Job> findByExperienceLevel(String experienceLevel);
    List<Job> findByRemote(boolean remote);
    List<Job> findByTagsContaining(String tag);
    List<Job> searchByKeyword(String keyword, Language language);
    List<Job> findJobsClosingBefore(LocalDateTime deadline);
    List<Job> findRecentJobs(int limit);
    void deleteById(String id);
    boolean existsByJobKey(String jobKey);
    boolean existsBySlug(String slug);
    List<Job> findJobsScheduledForPublish(LocalDateTime now);
    long countByStatus(ContentStatus status);
    long countByDepartment(String department);
}
