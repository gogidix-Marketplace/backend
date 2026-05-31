package com.gogidix.corporate.website.infrastructure.web.rest;

import com.gogidix.corporate.website.application.dto.PageDto;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Page;
import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.service.PageDomainService;
import com.gogidix.corporate.website.infrastructure.external.analytics.AnalyticsClient;
import com.gogidix.corporate.website.infrastructure.external.analytics.PageViewEvent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pages")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Pages", description = "Page content management and serving API")
public class PageController {

    private final PageDomainService pageDomainService;
    private final AnalyticsClient analyticsClient;

    @Value("${application.integration.analytics.enabled:true}")
    private boolean analyticsEnabled;

    @Value("${application.security.api-key}")
    private String apiKey;

    @GetMapping
    @Operation(summary = "Get all published pages", description = "Retrieve all published pages for a specific region and language")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved pages")
    public ResponseEntity<List<PageDto>> getPublishedPages(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Page> pages = pageDomainService.getPublishedPagesByRegion(region);
        List<PageDto> pageDtos = pages.stream()
                .map(page -> PageDto.fromEntity(page, language))
                .toList();
        return ResponseEntity.ok(pageDtos);
    }

    @GetMapping("/navigation")
    @Operation(summary = "Get navigation structure", description = "Retrieve pages that should appear in site navigation")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved navigation pages")
    public ResponseEntity<List<PageDto>> getNavigationPages(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Page> pages = pageDomainService.getNavigationPages(region);
        List<PageDto> pageDtos = pages.stream()
                .map(page -> PageDto.fromEntity(page, language))
                .toList();
        return ResponseEntity.ok(pageDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get page by ID", description = "Retrieve a specific page by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved page")
    @ApiResponse(responseCode = "404", description = "Page not found")
    public ResponseEntity<PageDto> getPageById(
            @Parameter(description = "Page ID", required = true, in = ParameterIn.PATH)
            @PathVariable String id,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language,
            HttpServletRequest request
    ) {
        return pageDomainService.getPageById(id)
                .map(page -> {
                    trackPageView(request, page.getPath(), page.getLocalizedContent().stream()
                            .filter(lc -> lc.getLanguage() == language)
                            .findFirst()
                            .map(com.gogidix.corporate.website.domain.model.LocalizedContent::getTitle)
                            .orElse(page.getPageKey()));
                    return ResponseEntity.ok(PageDto.fromEntity(page, language));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/key/{pageKey}")
    @Operation(summary = "Get page by key", description = "Retrieve a specific page by its key")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved page")
    @ApiResponse(responseCode = "404", description = "Page not found")
    public ResponseEntity<PageDto> getPageByKey(
            @Parameter(description = "Page key", required = true, in = ParameterIn.PATH)
            @PathVariable String pageKey,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language,
            HttpServletRequest request
    ) {
        return pageDomainService.getPageByKey(pageKey)
                .map(page -> {
                    trackPageView(request, page.getPath(), page.getLocalizedContent().stream()
                            .filter(lc -> lc.getLanguage() == language)
                            .findFirst()
                            .map(com.gogidix.corporate.website.domain.model.LocalizedContent::getTitle)
                            .orElse(page.getPageKey()));
                    return ResponseEntity.ok(PageDto.fromEntity(page, language));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/path/{path}")
    @Operation(summary = "Get page by path", description = "Retrieve a specific page by its URL path")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved page")
    @ApiResponse(responseCode = "404", description = "Page not found")
    public ResponseEntity<PageDto> getPageByPath(
            @Parameter(description = "Page path", required = true, in = ParameterIn.PATH)
            @PathVariable String path,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language,
            HttpServletRequest request
    ) {
        return pageDomainService.getPageByPath(path)
                .map(page -> {
                    trackPageView(request, path, page.getLocalizedContent().stream()
                            .filter(lc -> lc.getLanguage() == language)
                            .findFirst()
                            .map(com.gogidix.corporate.website.domain.model.LocalizedContent::getTitle)
                            .orElse(page.getPageKey()));
                    return ResponseEntity.ok(PageDto.fromEntity(page, language));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    @Operation(summary = "Search pages", description = "Search pages by keyword")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved search results")
    public ResponseEntity<List<PageDto>> searchPages(
            @Parameter(description = "Search keyword", required = true, in = ParameterIn.QUERY)
            @RequestParam String keyword,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Page> pages = pageDomainService.searchPages(keyword, language);
        List<PageDto> pageDtos = pages.stream()
                .map(page -> PageDto.fromEntity(page, language))
                .toList();
        return ResponseEntity.ok(pageDtos);
    }

    @GetMapping("/tags/{tag}")
    @Operation(summary = "Get pages by tag", description = "Retrieve pages tagged with a specific tag")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved pages")
    public ResponseEntity<List<PageDto>> getPagesByTag(
            @Parameter(description = "Tag name", required = true, in = ParameterIn.PATH)
            @PathVariable String tag,
            @Parameter(description = "Language filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "en") Language language
    ) {
        List<Page> pages = pageDomainService.getPagesByTag(tag);
        List<PageDto> pageDtos = pages.stream()
                .map(page -> PageDto.fromEntity(page, language))
                .toList();
        return ResponseEntity.ok(pageDtos);
    }

    private void trackPageView(HttpServletRequest request, String pageUrl, String pageTitle) {
        if (!analyticsEnabled) return;

        try {
            PageViewEvent event = PageViewEvent.builder()
                    .sessionId(getSessionId(request))
                    .pageUrl(pageUrl)
                    .pageTitle(pageTitle)
                    .referrer(request.getHeader("Referer"))
                    .userAgent(request.getHeader("User-Agent"))
                    .language(getLanguageFromRequest(request))
                    .region(getRegionFromRequest(request))
                    .countryCode(request.getHeader("X-Country-Code"))
                    .timestamp(java.time.LocalDateTime.now())
                    .build();

            analyticsClient.trackPageView(event, apiKey);
        } catch (Exception e) {
            log.warn("Failed to track page view: {}", e.getMessage());
        }
    }

    private String getSessionId(HttpServletRequest request) {
        String sessionId = request.getHeader("X-Session-ID");
        if (sessionId == null || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }
        return sessionId;
    }

    private Language getLanguageFromRequest(HttpServletRequest request) {
        String langHeader = request.getHeader("Accept-Language");
        if (langHeader != null && langHeader.startsWith("fr")) return Language.FR;
        if (langHeader != null && langHeader.startsWith("es")) return Language.ES;
        if (langHeader != null && langHeader.startsWith("pt")) return Language.PT;
        if (langHeader != null && langHeader.startsWith("ar")) return Language.AR;
        return Language.EN;
    }

    private Region getRegionFromRequest(HttpServletRequest request) {
        String regionHeader = request.getHeader("X-Region");
        if (regionHeader != null) {
            try {
                return Region.valueOf(regionHeader.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.debug("Invalid region header: {}", regionHeader);
            }
        }
        return Region.NG;
    }
}
