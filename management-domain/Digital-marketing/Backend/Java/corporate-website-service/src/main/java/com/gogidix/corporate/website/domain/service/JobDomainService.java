package com.gogidix.corporate.website.domain.service;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Job;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.repository.JobRepository;
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
public class JobDomainService {

    private final JobRepository jobRepository;

    @CacheEvict(value = "jobs", allEntries = true)
    public Job createJob(Job job) {
        validateJob(job);
        return jobRepository.save(job);
    }

    @CacheEvict(value = "jobs", allEntries = true)
    public Job updateJob(Job job) {
        validateJob(job);
        return jobRepository.save(job);
    }

    @CacheEvict(value = "jobs", allEntries = true)
    public void deleteJob(String id) {
        jobRepository.deleteById(id);
    }

    @Cacheable(value = "jobs", key = "#id")
    public Optional<Job> getJobById(String id) {
        return jobRepository.findById(id);
    }

    @Cacheable(value = "jobs", key = "'key:' + #jobKey")
    public Optional<Job> getJobByKey(String jobKey) {
        return jobRepository.findByJobKey(jobKey);
    }

    @Cacheable(value = "jobs", key = "'slug:' + #slug")
    public Optional<Job> getJobBySlug(String slug) {
        return jobRepository.findBySlug(slug);
    }

    @Cacheable(value = "jobs", key = "'open:' + #region")
    public List<Job> getOpenJobsByRegion(Region region) {
        return jobRepository.findOpenJobsByRegion(region);
    }

    @Cacheable(value = "jobs", key = "'featured:' + #region")
    public List<Job> getFeaturedJobs(Region region) {
        return jobRepository.findFeaturedJobs().stream()
                .filter(job -> job.getAvailableRegions().contains(region))
                .toList();
    }

    @Cacheable(value = "jobs", key = "'department:' + #department + ':' + #region")
    public List<Job> getJobsByDepartment(String department, Region region) {
        return jobRepository.findByDepartment(department).stream()
                .filter(job -> job.getAvailableRegions().contains(region))
                .toList();
    }

    @Cacheable(value = "jobs", key = "'location:' + #location + ':' + #region")
    public List<Job> getJobsByLocation(String location, Region region) {
        return jobRepository.findByLocation(location).stream()
                .filter(job -> job.getAvailableRegions().contains(region))
                .toList();
    }

    public List<Job> getJobsByEmploymentType(String employmentType, Region region) {
        return jobRepository.findByEmploymentType(employmentType).stream()
                .filter(job -> job.getAvailableRegions().contains(region))
                .toList();
    }

    public List<Job> getRemoteJobs(Region region) {
        return jobRepository.findByRemote(true).stream()
                .filter(job -> job.getAvailableRegions().contains(region))
                .toList();
    }

    public List<Job> searchJobs(String keyword, Language language) {
        return jobRepository.searchByKeyword(keyword, language);
    }

    public List<Job> getJobsClosingSoon(int limit) {
        return jobRepository.findJobsClosingBefore(LocalDateTime.now().plusDays(7));
    }

    public List<Job> getScheduledJobs() {
        return jobRepository.findJobsScheduledForPublish(LocalDateTime.now());
    }

    @CacheEvict(value = "jobs", allEntries = true)
    public Job publishJob(String id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
        job.setStatus(ContentStatus.PUBLISHED);
        job.setPublishDate(LocalDateTime.now());
        return jobRepository.save(job);
    }

    @CacheEvict(value = "jobs", allEntries = true)
    public Job closeJob(String id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Job not found: " + id));
        job.setCloseDate(LocalDateTime.now());
        return jobRepository.save(job);
    }

    private void validateJob(Job job) {
        if (job.getJobKey() == null || job.getJobKey().isBlank()) {
            throw new IllegalArgumentException("Job key cannot be null or blank");
        }
        if (job.getSlug() == null || job.getSlug().isBlank()) {
            throw new IllegalArgumentException("Job slug cannot be null or blank");
        }
        if (job.getLocalizedContent() == null || job.getLocalizedContent().isEmpty()) {
            throw new IllegalArgumentException("Job must have at least one localized content");
        }
    }

    public long getJobCount() {
        return jobRepository.countByStatus(ContentStatus.PUBLISHED);
    }
}
