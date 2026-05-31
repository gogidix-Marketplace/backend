package com.gogidix.corporate.website.infrastructure.web.rest;

import com.gogidix.corporate.website.application.dto.CaseStudyDto;
import com.gogidix.corporate.website.domain.model.CaseStudy;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.LocalizedContent;
import com.gogidix.corporate.website.domain.model.Metric;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.service.CaseStudyDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/case-studies")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Case Studies", description = "Case study management and serving API")
public class CaseStudyController {

    private final CaseStudyDomainService caseStudyDomainService;

    @GetMapping
    @Operation(summary = "Get all published case studies", description = "Retrieve all published case studies for a specific region")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved case studies")
    public ResponseEntity<List<CaseStudyDto>> getPublishedStudies(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<CaseStudy> studies = caseStudyDomainService.getPublishedStudiesByRegion(region);
        return ResponseEntity.ok(studies.stream()
                .map(study -> mapToDto(study, language))
                .toList());
    }

    @GetMapping("/featured")
    @Operation(summary = "Get featured case studies", description = "Retrieve featured case studies")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved featured case studies")
    public ResponseEntity<List<CaseStudyDto>> getFeaturedStudies(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<CaseStudy> studies = caseStudyDomainService.getFeaturedStudies(region);
        return ResponseEntity.ok(studies.stream()
                .map(study -> mapToDto(study, language))
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get case study by ID", description = "Retrieve a specific case study by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved case study")
    @ApiResponse(responseCode = "404", description = "Case study not found")
    public ResponseEntity<CaseStudyDto> getCaseStudyById(
            @Parameter(description = "Case study ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language,
            HttpServletRequest request
    ) {
        return caseStudyDomainService.getCaseStudyById(id)
                .map(study -> {
                    caseStudyDomainService.incrementViewCount(id);
                    return ResponseEntity.ok(mapToDto(study, language));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get case study by slug", description = "Retrieve a specific case study by its slug")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved case study")
    @ApiResponse(responseCode = "404", description = "Case study not found")
    public ResponseEntity<CaseStudyDto> getCaseStudyBySlug(
            @Parameter(description = "Case study slug", required = true, in = ParameterIn.PATH)
            @PathVariable String slug,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language,
            HttpServletRequest request
    ) {
        return caseStudyDomainService.getCaseStudyBySlug(slug)
                .map(study -> {
                    caseStudyDomainService.incrementViewCount(study.getId());
                    return ResponseEntity.ok(mapToDto(study, language));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/industry/{industry}")
    @Operation(summary = "Get case studies by industry", description = "Retrieve case studies in a specific industry")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved case studies")
    public ResponseEntity<List<CaseStudyDto>> getStudiesByIndustry(
            @Parameter(description = "Industry name", required = true, in = ParameterIn.PATH)
            @PathVariable String industry,
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<CaseStudy> studies = caseStudyDomainService.getStudiesByIndustry(industry, region);
        return ResponseEntity.ok(studies.stream()
                .map(study -> mapToDto(study, language))
                .toList());
    }

    @GetMapping("/client/{clientName}")
    @Operation(summary = "Get case studies by client", description = "Retrieve case studies for a specific client")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved case studies")
    public ResponseEntity<List<CaseStudyDto>> getStudiesByClient(
            @Parameter(description = "Client name", required = true, in = ParameterIn.PATH)
            @PathVariable String clientName,
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<CaseStudy> studies = caseStudyDomainService.getStudiesByClient(clientName, region);
        return ResponseEntity.ok(studies.stream()
                .map(study -> mapToDto(study, language))
                .toList());
    }

    @GetMapping("/service/{service}")
    @Operation(summary = "Get case studies by service", description = "Retrieve case studies related to a specific service")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved case studies")
    public ResponseEntity<List<CaseStudyDto>> getStudiesByService(
            @Parameter(description = "Service name", required = true, in = ParameterIn.PATH)
            @PathVariable String service,
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<CaseStudy> studies = caseStudyDomainService.getStudiesByService(service, region);
        return ResponseEntity.ok(studies.stream()
                .map(study -> mapToDto(study, language))
                .toList());
    }

    @GetMapping("/technology/{technology}")
    @Operation(summary = "Get case studies by technology", description = "Retrieve case studies using a specific technology")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved case studies")
    public ResponseEntity<List<CaseStudyDto>> getStudiesByTechnology(
            @Parameter(description = "Technology name", required = true, in = ParameterIn.PATH)
            @PathVariable String technology,
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<CaseStudy> studies = caseStudyDomainService.getStudiesByTechnology(technology, region);
        return ResponseEntity.ok(studies.stream()
                .map(study -> mapToDto(study, language))
                .toList());
    }

    @GetMapping("/search")
    @Operation(summary = "Search case studies", description = "Search case studies by keyword")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    public ResponseEntity<List<CaseStudyDto>> searchStudies(
            @Parameter(description = "Search keyword", required = true, in = ParameterIn.QUERY)
            @RequestParam String keyword,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<CaseStudy> studies = caseStudyDomainService.searchCaseStudies(keyword, language);
        return ResponseEntity.ok(studies.stream()
                .map(study -> mapToDto(study, language))
                .toList());
    }

    @GetMapping("/{id}/related")
    @Operation(summary = "Get related case studies", description = "Retrieve case studies related to a specific case study")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved related case studies")
    public ResponseEntity<List<CaseStudyDto>> getRelatedStudies(
            @Parameter(description = "Case study ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id,
            @Parameter(description = "Maximum number of studies", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "4") int limit,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<CaseStudy> studies = caseStudyDomainService.getRelatedStudies(id, limit);
        return ResponseEntity.ok(studies.stream()
                .map(study -> mapToDto(study, language))
                .toList());
    }

    private CaseStudyDto mapToDto(CaseStudy study, Language language) {
        LocalizedContent content = study.getLocalizedContent().stream()
                .filter(lc -> lc.getLanguage() == language)
                .findFirst()
                .orElse(study.getLocalizedContent().isEmpty() ? null : study.getLocalizedContent().get(0));

        List<CaseStudyDto.MetricDto> metricDtos = study.getMetrics().stream()
                .map(m -> CaseStudyDto.MetricDto.builder()
                        .label(m.getLabel())
                        .value(m.getValue())
                        .unit(m.getUnit())
                        .description(m.getDescription())
                        .build())
                .collect(Collectors.toList());

        return CaseStudyDto.builder()
                .id(study.getId())
                .slug(study.getSlug())
                .localizedContent(content)
                .language(language)
                .clientName(study.getClientName())
                .clientLogo(study.getClientLogo())
                .industry(study.getIndustry())
                .projectDuration(study.getProjectDuration())
                .availableRegions(study.getAvailableRegions())
                .challenge(study.getChallenge())
                .solution(study.getSolution())
                .results(study.getResults())
                .metrics(metricDtos)
                .technologies(study.getTechnologies())
                .services(study.getServices())
                .heroImage(study.getHeroImage())
                .heroImageAlt(study.getHeroImageAlt())
                .gallery(study.getGallery())
                .testimonial(study.getTestimonial())
                .testimonialAuthor(study.getTestimonialAuthor())
                .testimonialRole(study.getTestimonialRole())
                .testimonialImage(study.getTestimonialImage())
                .status(study.getStatus())
                .publishDate(study.getPublishDate())
                .createdAt(study.getCreatedAt())
                .tags(study.getTags())
                .featured(study.isFeatured())
                .sortOrder(study.getSortOrder())
                .viewCount(study.getViewCount())
                .build();
    }
}
