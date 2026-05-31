package com.gogidix.corporatecms.interfaces.rest.controller;

import com.gogidix.corporatecms.application.dto.ApiResponse;
import com.gogidix.corporatecms.application.dto.JobDTO;
import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.domain.enums.JobStatus;
import com.gogidix.corporatecms.domain.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for job/career management.
 */
@Tag(name = "Jobs", description = "Job and career APIs")
@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobService jobService;

    @Operation(summary = "Create job posting", description = "Create a new job posting")
    @PreAuthorize("hasAuthority('CAREER_MANAGE')")
    @PostMapping
    public ResponseEntity<ApiResponse<JobDTO>> createJob(@Valid @RequestBody JobDTO dto) {
        JobDTO job = jobService.createJob(dto);
        return ResponseEntity.status(201).body(ApiResponse.created(job));
    }

    @Operation(summary = "Update job posting", description = "Update an existing job posting")
    @PreAuthorize("hasAuthority('CAREER_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDTO>> updateJob(
            @Parameter(description = "Job ID") @PathVariable String id,
            @Valid @RequestBody JobDTO dto) {
        JobDTO job = jobService.updateJob(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Job updated successfully", job));
    }

    @Operation(summary = "Get job by ID", description = "Retrieve a job posting by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobDTO>> getJobById(
            @Parameter(description = "Job ID") @PathVariable String id) {
        JobDTO job = jobService.getJobById(id);
        return ResponseEntity.ok(ApiResponse.success(job));
    }

    @Operation(summary = "Get job by slug", description = "Retrieve a job posting by slug")
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<JobDTO>> getJobBySlug(
            @Parameter(description = "Job slug") @PathVariable String slug) {
        JobDTO job = jobService.getJobBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(job));
    }

    @Operation(summary = "Get jobs by department", description = "Retrieve job postings by department")
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<ApiResponse<PageResponse<JobDTO>>> getJobsByDepartment(
            @Parameter(description = "Department ID") @PathVariable String departmentId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<JobDTO> jobs = jobService.getJobsByDepartment(departmentId, page, size);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @Operation(summary = "Get jobs by status", description = "Retrieve job postings filtered by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('CAREER_MANAGE')")
    public ResponseEntity<ApiResponse<PageResponse<JobDTO>>> getJobsByStatus(
            @Parameter(description = "Job status") @PathVariable JobStatus status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<JobDTO> jobs = jobService.getJobsByStatus(status, page, size);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @Operation(summary = "Get open jobs", description = "Retrieve all open job postings")
    @GetMapping("/open")
    public ResponseEntity<ApiResponse<PageResponse<JobDTO>>> getOpenJobs(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<JobDTO> jobs = jobService.getOpenJobs(page, size);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @Operation(summary = "Get published jobs", description = "Retrieve all published job postings")
    @GetMapping("/published")
    public ResponseEntity<ApiResponse<PageResponse<JobDTO>>> getPublishedJobs(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<JobDTO> jobs = jobService.getPublishedJobs(page, size);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @Operation(summary = "Get featured jobs", description = "Retrieve all featured job postings")
    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<List<JobDTO>>> getFeaturedJobs() {
        List<JobDTO> jobs = jobService.getFeaturedJobs();
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @Operation(summary = "Search jobs", description = "Search job postings by keyword")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<JobDTO>>> searchJobs(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<JobDTO> jobs = jobService.searchJobs(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @Operation(summary = "Search jobs by location", description = "Search job postings by location")
    @GetMapping("/search/location")
    public ResponseEntity<ApiResponse<PageResponse<JobDTO>>> searchJobsByLocation(
            @Parameter(description = "Location") @RequestParam String location,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<JobDTO> jobs = jobService.searchJobsByLocation(location, page, size);
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @Operation(summary = "Publish job", description = "Publish a job posting")
    @PreAuthorize("hasAuthority('CAREER_MANAGE')")
    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<JobDTO>> publishJob(
            @Parameter(description = "Job ID") @PathVariable String id) {
        JobDTO job = jobService.publishJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job published successfully", job));
    }

    @Operation(summary = "Close job", description = "Close a job posting")
    @PreAuthorize("hasAuthority('CAREER_MANAGE')")
    @PostMapping("/{id}/close")
    public ResponseEntity<ApiResponse<JobDTO>> closeJob(
            @Parameter(description = "Job ID") @PathVariable String id) {
        JobDTO job = jobService.closeJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job closed successfully", job));
    }

    @Operation(summary = "Delete job", description = "Delete a job posting")
    @PreAuthorize("hasAuthority('CAREER_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteJob(
            @Parameter(description = "Job ID") @PathVariable String id) {
        jobService.deleteJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job deleted successfully", null));
    }

    @Operation(summary = "Record job application", description = "Record a new job application")
    @PostMapping("/{id}/apply")
    public ResponseEntity<ApiResponse<Void>> recordApplication(
            @Parameter(description = "Job ID") @PathVariable String id) {
        jobService.recordApplication(id);
        return ResponseEntity.ok(ApiResponse.success("Application recorded", null));
    }
}
