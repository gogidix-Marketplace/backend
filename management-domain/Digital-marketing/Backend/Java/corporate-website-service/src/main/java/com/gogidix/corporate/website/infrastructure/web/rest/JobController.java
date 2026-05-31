package com.gogidix.corporate.website.infrastructure.web.rest;

import com.gogidix.corporate.website.application.dto.JobDto;
import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Job;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.LocalizedContent;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.service.JobDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/careers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Careers", description = "Job opening management and serving API")
public class JobController {

    private final JobDomainService jobDomainService;

    @GetMapping
    @Operation(summary = "Get all open positions", description = "Retrieve all open job positions for a specific region")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved job positions")
    public ResponseEntity<List<JobDto>> getOpenJobs(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Job> jobs = jobDomainService.getOpenJobsByRegion(region);
        return ResponseEntity.ok(jobs.stream()
                .map(job -> mapToDto(job, language))
                .toList());
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured positions", description = "Retrieve featured job positions")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved featured positions")
    public ResponseEntity<List<JobDto>> getFeaturedJobs(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Job> jobs = jobDomainService.getFeaturedJobs(region);
        return ResponseEntity.ok(jobs.stream()
                .map(job -> mapToDto(job, language))
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID", description = "Retrieve a specific job position by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved job position")
    @ApiResponse(responseCode = "404", description = "Job position not found")
    public ResponseEntity<JobDto> getJobById(
            @Parameter(description = "Job ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        return jobDomainService.getJobById(id)
                .map(job -> ResponseEntity.ok(mapToDto(job, language)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get job by slug", description = "Retrieve a specific job position by its slug")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved job position")
    @ApiResponse(responseCode = "404", description = "Job position not found")
    public ResponseEntity<JobDto> getJobBySlug(
            @Parameter(description = "Job slug", required = true, in = ParameterIn.PATH)
            @PathVariable String slug,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        return jobDomainService.getJobBySlug(slug)
                .map(job -> ResponseEntity.ok(mapToDto(job, language)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/department/{department}")
    @Operation(summary = "Get jobs by department", description = "Retrieve job positions in a specific department")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved job positions")
    public ResponseEntity<List<JobDto>> getJobsByDepartment(
            @Parameter(description = "Department name", required = true, in = ParameterIn.PATH)
            @PathVariable String department,
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Job> jobs = jobDomainService.getJobsByDepartment(department, region);
        return ResponseEntity.ok(jobs.stream()
                .map(job -> mapToDto(job, language))
                .toList());
    }

    @GetMapping("/location/{location}")
    @Operation(summary = "Get jobs by location", description = "Retrieve job positions at a specific location")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved job positions")
    public ResponseEntity<List<JobDto>> getJobsByLocation(
            @Parameter(description = "Location name", required = true, in = ParameterIn.PATH)
            @PathVariable String location,
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Job> jobs = jobDomainService.getJobsByLocation(location, region);
        return ResponseEntity.ok(jobs.stream()
                .map(job -> mapToDto(job, language))
                .toList());
    }

    @GetMapping("/remote")
    @Operation(summary = "Get remote jobs", description = "Retrieve remote job positions")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved remote job positions")
    public ResponseEntity<List<JobDto>> getRemoteJobs(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Job> jobs = jobDomainService.getRemoteJobs(region);
        return ResponseEntity.ok(jobs.stream()
                .map(job -> mapToDto(job, language))
                .toList());
    }

    @GetMapping("/closing-soon")
    @Operation(summary = "Get jobs closing soon", description = "Retrieve job positions closing within the next 7 days")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved job positions")
    public ResponseEntity<List<JobDto>> getJobsClosingSoon(
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Job> jobs = jobDomainService.getJobsClosingSoon(10);
        return ResponseEntity.ok(jobs.stream()
                .map(job -> mapToDto(job, language))
                .toList());
    }

    @GetMapping("/search")
    @Operation(summary = "Search jobs", description = "Search job positions by keyword")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    public ResponseEntity<List<JobDto>> searchJobs(
            @Parameter(description = "Search keyword", required = true, in = ParameterIn.QUERY)
            @RequestParam String keyword,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Job> jobs = jobDomainService.searchJobs(keyword, language);
        return ResponseEntity.ok(jobs.stream()
                .map(job -> mapToDto(job, language))
                .toList());
    }

    private JobDto mapToDto(Job job, Language language) {
        LocalizedContent content = job.getLocalizedContent().stream()
                .filter(lc -> lc.getLanguage() == language)
                .findFirst()
                .orElse(job.getLocalizedContent().isEmpty() ? null : job.getLocalizedContent().get(0));

        return JobDto.builder()
                .id(job.getId())
                .jobKey(job.getJobKey())
                .slug(job.getSlug())
                .localizedContent(content)
                .language(language)
                .department(job.getDepartment())
                .employmentType(job.getEmploymentType())
                .experienceLevel(job.getExperienceLevel())
                .location(job.getLocation())
                .remote(job.isRemote())
                .availableRegions(job.getAvailableRegions())
                .responsibilities(job.getResponsibilities())
                .requirements(job.getRequirements())
                .benefits(job.getBenefits())
                .salaryMin(job.getSalaryMin())
                .salaryMax(job.getSalaryMax())
                .salaryCurrency(job.getSalaryCurrency())
                .applicationUrl(job.getApplicationUrl())
                .applicationEmail(job.getApplicationEmail())
                .deadline(job.getDeadline())
                .status(job.getStatus())
                .publishDate(job.getPublishDate())
                .closeDate(job.getCloseDate())
                .createdAt(job.getCreatedAt())
                .tags(job.getTags())
                .featured(job.isFeatured())
                .sortOrder(job.getSortOrder())
                .build();
    }
}
