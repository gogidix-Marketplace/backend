package com.gogidix.corporate.website.infrastructure.external.content;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Optional;

@FeignClient(name = "content-management-service", url = "${application.integration.content-management.base-url}")
@CircuitBreaker(name = "contentManagementService")
@Retry(name = "contentManagementService")
public interface ContentManagementClient {

    @GetMapping("/api/v1/content/{id}")
    Optional<ContentDto> getContentById(
            @PathVariable String id,
            @RequestHeader("X-API-Key") String apiKey
    );

    @GetMapping("/api/v1/content/slug/{slug}")
    Optional<ContentDto> getContentBySlug(
            @PathVariable String slug,
            @RequestHeader("X-API-Key") String apiKey
    );

    default Optional<ContentDto> getContentByIdFallback(String id, Throwable throwable) {
        return Optional.empty();
    }

    default Optional<ContentDto> getContentBySlugFallback(String slug, Throwable throwable) {
        return Optional.empty();
    }
}
