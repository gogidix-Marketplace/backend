package com.gogidix.corporate.website.infrastructure.web.rest;

import com.gogidix.corporate.website.application.dto.PressReleaseDto;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.LocalizedContent;
import com.gogidix.corporate.website.domain.model.PressRelease;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.service.PressReleaseDomainService;
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
@RequestMapping("/api/v1/press")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Press Releases", description = "Press release management and serving API")
public class PressReleaseController {

    private final PressReleaseDomainService pressReleaseDomainService;

    @GetMapping
    @Operation(summary = "Get all published press releases", description = "Retrieve all published press releases")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved press releases")
    public ResponseEntity<List<PressReleaseDto>> getPublishedReleases(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<PressRelease> releases = pressReleaseDomainService.getPublishedReleasesByRegion(region);
        return ResponseEntity.ok(releases.stream()
                .map(release -> mapToDto(release, language))
                .toList());
    }

    @GetMapping("/recent")
    @Operation(summary = "Get recent press releases", description = "Retrieve recent press releases")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved recent press releases")
    public ResponseEntity<List<PressReleaseDto>> getRecentReleases(
            @Parameter(description = "Maximum number of releases", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "10") int limit,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<PressRelease> releases = pressReleaseDomainService.getRecentReleases(limit);
        return ResponseEntity.ok(releases.stream()
                .map(release -> mapToDto(release, language))
                .toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get press release by ID", description = "Retrieve a specific press release by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved press release")
    @ApiResponse(responseCode = "404", description = "Press release not found")
    public ResponseEntity<PressReleaseDto> getPressReleaseById(
            @Parameter(description = "Press release ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        return pressReleaseDomainService.getPressReleaseById(id)
                .map(release -> ResponseEntity.ok(mapToDto(release, language)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/slug/{slug}")
    @Operation(summary = "Get press release by slug", description = "Retrieve a specific press release by its slug")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved press release")
    @ApiResponse(responseCode = "404", description = "Press release not found")
    public ResponseEntity<PressReleaseDto> getPressReleaseBySlug(
            @Parameter(description = "Press release slug", required = true, in = ParameterIn.PATH)
            @PathVariable String slug,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        return pressReleaseDomainService.getPressReleaseBySlug(slug)
                .map(release -> ResponseEntity.ok(mapToDto(release, language)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search press releases", description = "Search press releases by keyword")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    public ResponseEntity<List<PressReleaseDto>> searchReleases(
            @Parameter(description = "Search keyword", required = true, in = ParameterIn.QUERY)
            @RequestParam String keyword,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<PressRelease> releases = pressReleaseDomainService.searchPressReleases(keyword, language);
        return ResponseEntity.ok(releases.stream()
                .map(release -> mapToDto(release, language))
                .toList());
    }

    @GetMapping("/media")
    @Operation(summary = "Get media contacts", description = "Retrieve media contact information")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved media contacts")
    public ResponseEntity<List<PressReleaseDto>> getMediaReleases(
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<PressRelease> releases = pressReleaseDomainService.getNonEmbargoedReleases();
        return ResponseEntity.ok(releases.stream()
                .limit(10)
                .map(release -> mapToDto(release, language))
                .toList());
    }

    private PressReleaseDto mapToDto(PressRelease release, Language language) {
        LocalizedContent content = release.getLocalizedContent().stream()
                .filter(lc -> lc.getLanguage() == language)
                .findFirst()
                .orElse(release.getLocalizedContent().isEmpty() ? null : release.getLocalizedContent().get(0));

        return PressReleaseDto.builder()
                .id(release.getId())
                .slug(release.getSlug())
                .localizedContent(content)
                .language(language)
                .releaseDate(release.getReleaseDate())
                .contactName(release.getContactName())
                .contactEmail(release.getContactEmail())
                .contactPhone(release.getContactPhone())
                .mediaContacts(release.getMediaContacts())
                .availableRegions(release.getAvailableRegions())
                .embargoed(release.isEmbargoed())
                .immediateRelease(release.isImmediateRelease())
                .tags(release.getTags())
                .status(release.getStatus())
                .publishDate(release.getPublishDate())
                .createdAt(release.getCreatedAt())
                .organization(release.getOrganization())
                .tickerSymbol(release.getTickerSymbol())
                .build();
    }
}
