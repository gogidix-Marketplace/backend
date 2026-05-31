package com.gogidix.corporatecms.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.dto.JobDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.dto.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.exception.DuplicateResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.mapper.JobMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.enums.JobStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.repository.JobRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Service for managing job postings.
 */
@Service
@RequiredArgsConstructor
public class JobService {
    private static final Logger log = LoggerFactory.getLogger(JobService.class);

    private final JobRepository jobRepository;
    private final JobMapper jobMapper;

    @Transactional
    public JobDTO createJob(JobDTO dto) {
        log.info("Creating job with slug: {}", dto.getSlug());

        if (jobRepository.findBySlugAndDeletedFalse(dto.getSlug()).isPresent()) {
            throw new DuplicateResourceException("Job", "slug", dto.getSlug());
        }

        Job job = jobMapper.toEntity(dto);
        job.setStatus(com.gogidix.corporatecms.domain.enums.JobStatus.DRAFT);

        Job savedJob = jobRepository.save(job);
        log.info("Job created with ID: {}", savedJob.getId());

        return jobMapper.toDto(savedJob);
    }

    @Transactional
    @CacheEvict(value = "jobs", key = "#id")
    public JobDTO updateJob(String id, JobDTO dto) {
        log.info("Updating job: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));

        jobMapper.updateEntityFromDto(dto, job);

        Job savedJob = jobRepository.save(job);
        log.info("Job updated: {}", id);

        return jobMapper.toDto(savedJob);
    }

    @Cacheable(value = "jobs", key = "#id")
    public JobDTO getJobById(String id) {
        log.info("Fetching job by ID: {}", id);
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
        return jobMapper.toDto(job);
    }

    public JobDTO getJobBySlug(String slug) {
        log.info("Fetching job by slug: {}", slug);
        Job job = jobRepository.findBySlugAndDeletedFalse(slug)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "slug", slug));

        if (job.isOpen()) {
            job.incrementViewCount();
            jobRepository.save(job);
        }

        return jobMapper.toDto(job);
    }

    public PageResponse<JobDTO> getJobsByDepartment(String departmentId, int page, int size) {
        log.info("Fetching jobs by department: {}", departmentId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage = jobRepository.findByDepartmentIdAndDeletedFalse(departmentId, pageable);
        return PageResponse.of(jobPage.map(jobMapper::toDto));
    }

    public PageResponse<JobDTO> getJobsByStatus(JobStatus status, int page, int size) {
        log.info("Fetching jobs by status: {}", status);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage = jobRepository.findByStatusAndDeletedFalse(status, pageable);
        return PageResponse.of(jobPage.map(jobMapper::toDto));
    }

    public PageResponse<JobDTO> getOpenJobs(int page, int size) {
        log.info("Fetching open jobs");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage = jobRepository.findOpenJobsWithDeadlineAfter(LocalDateTime.now(), pageable);
        return PageResponse.of(jobPage.map(jobMapper::toDto));
    }

    public PageResponse<JobDTO> getPublishedJobs(int page, int size) {
        log.info("Fetching published jobs");
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "publishedAt"));
        Page<Job> jobPage = jobRepository.findByPublishedTrueAndDeletedFalse(pageable);
        return PageResponse.of(jobPage.map(jobMapper::toDto));
    }

    public List<JobDTO> getFeaturedJobs() {
        List<Job> jobs = jobRepository.findFeaturedJobs();
        return jobMapper.toDtoList(jobs);
    }

    public PageResponse<JobDTO> searchJobs(String keyword, int page, int size) {
        log.info("Searching jobs with keyword: {}", keyword);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage = jobRepository.searchByKeyword(keyword, pageable);
        return PageResponse.of(jobPage.map(jobMapper::toDto));
    }

    public PageResponse<JobDTO> searchJobsByLocation(String location, int page, int size) {
        log.info("Searching jobs by location: {}", location);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Job> jobPage = jobRepository.searchByLocation(location, pageable);
        return PageResponse.of(jobPage.map(jobMapper::toDto));
    }

    @Transactional
    @CacheEvict(value = "jobs", key = "#id")
    public JobDTO publishJob(String id) {
        log.info("Publishing job: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));

        job.setStatus(com.gogidix.corporatecms.domain.enums.JobStatus.OPEN);
        job.setPublishedAt(LocalDateTime.now());

        Job savedJob = jobRepository.save(job);
        log.info("Job published: {}", id);

        return jobMapper.toDto(savedJob);
    }

    @Transactional
    @CacheEvict(value = "jobs", key = "#id")
    public JobDTO closeJob(String id) {
        log.info("Closing job: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));

        job.setStatus(com.gogidix.corporatecms.domain.enums.JobStatus.CLOSED);
        job.setClosedAt(LocalDateTime.now());

        Job savedJob = jobRepository.save(job);
        log.info("Job closed: {}", id);

        return jobMapper.toDto(savedJob);
    }

    @Transactional
    @CacheEvict(value = "jobs", key = "#id")
    public void deleteJob(String id) {
        log.info("Deleting job: {}", id);

        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));

        job.setDeleted(true);
        job.setDeletedAt(LocalDateTime.now());

        jobRepository.save(job);
        log.info("Job deleted: {}", id);
    }

    @Transactional
    public void recordApplication(String id) {
        Job job = jobRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
        job.incrementApplicationCount();
        jobRepository.save(job);
    }
}
