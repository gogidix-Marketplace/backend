package com.gogidix.corporate.website.infrastructure.web.rest;

import com.gogidix.corporate.website.domain.model.Region;
import com.gogidix.corporate.website.domain.service.SitemapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sitemap")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Sitemap", description = "Sitemap generation API")
public class SitemapController {

    private final SitemapService sitemapService;

    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Generate XML sitemap", description = "Generate an XML sitemap for search engines")
    @ApiResponse(responseCode = "200", description = "Successfully generated sitemap",
            content = @Content(schema = @Schema(type = "string", format = "xml")))
    public ResponseEntity<String> getSitemapXml(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region
    ) {
        String xml = sitemapService.generateSitemapXml(region);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_XML);

        return ResponseEntity.ok()
                .headers(headers)
                .body(xml);
    }

    @GetMapping("/json")
    @Operation(summary = "Generate JSON sitemap", description = "Generate a JSON sitemap with all URLs")
    @ApiResponse(responseCode = "200", description = "Successfully generated sitemap")
    public ResponseEntity<List<com.gogidix.corporate.website.application.dto.SitemapEntry>> getSitemapJson(
            @Parameter(description = "Region filter", required = true, in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region
    ) {
        List<com.gogidix.corporate.website.application.dto.SitemapEntry> sitemap = sitemapService.generateSitemap(region);
        return ResponseEntity.ok(sitemap);
    }

    @GetMapping("/sitemap.xml")
    @Operation(summary = "Get sitemap.xml file", description = "Standard sitemap endpoint for search engines")
    @ApiResponse(responseCode = "200", description = "Successfully generated sitemap")
    public ResponseEntity<String> getSitemapFile(
            @Parameter(description = "Region filter", in = ParameterIn.QUERY)
            @RequestParam(defaultValue = "NG") Region region
    ) {
        String xml = sitemapService.generateSitemapXml(region);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_XML)
                .header("Content-Disposition", "inline; filename=sitemap.xml")
                .body(xml);
    }
}
