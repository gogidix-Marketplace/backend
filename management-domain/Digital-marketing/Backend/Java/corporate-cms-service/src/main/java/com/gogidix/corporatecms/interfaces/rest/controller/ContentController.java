package com.gogidix.corporatecms.interfaces.rest.controller;

import com.gogidix.corporatecms.application.dto.ApiResponse;
import com.gogidix.corporatecms.application.dto.ContentDTO;
import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.domain.enums.ContentStatus;
import com.gogidix.corporatecms.domain.enums.ContentType;
import com.gogidix.corporatecms.domain.service.ContentService;
import com.gogidix.corporatecms.interfaces.rest.validator.ValidEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for content management.
 */
@Tag(name = "Content", description = "Content management APIs")
@RestController
@RequestMapping("/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;

    @Operation(summary = "Create new content", description = "Create a new content item")
    @PreAuthorize("hasAuthority('CONTENT_WRITE')")
    @PostMapping
    public ResponseEntity<ApiResponse<ContentDTO>> createContent(@Valid @RequestBody ContentDTO dto) {
        String userId = getCurrentUserId();
        ContentDTO created = contentService.createContent(dto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(created));
    }

    @Operation(summary = "Update content", description = "Update an existing content item")
    @PreAuthorize("hasAuthority('CONTENT_WRITE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ContentDTO>> updateContent(
            @Parameter(description = "Content ID") @PathVariable String id,
            @Valid @RequestBody ContentDTO dto) {
        String userId = getCurrentUserId();
        ContentDTO updated = contentService.updateContent(id, dto, userId);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @Operation(summary = "Get content by ID", description = "Retrieve a content item by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContentDTO>> getContentById(
            @Parameter(description = "Content ID") @PathVariable String id) {
        ContentDTO content = contentService.getContentById(id);
        return ResponseEntity.ok(ApiResponse.success(content));
    }

    @Operation(summary = "Get content by slug", description = "Retrieve a content item by its slug")
    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ContentDTO>> getContentBySlug(
            @Parameter(description = "Content slug") @PathVariable String slug) {
        ContentDTO content = contentService.getContentBySlug(slug);
        return ResponseEntity.ok(ApiResponse.success(content));
    }

    @Operation(summary = "Get content by type", description = "Retrieve content items filtered by type")
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<PageResponse<ContentDTO>>> getContentByType(
            @Parameter(description = "Content type") @PathVariable ContentType type,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<ContentDTO> content = contentService.getContentByType(type, page, size);
        return ResponseEntity.ok(ApiResponse.success(content));
    }

    @Operation(summary = "Get content by status", description = "Retrieve content items filtered by status")
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('CONTENT_READ')")
    public ResponseEntity<ApiResponse<PageResponse<ContentDTO>>> getContentByStatus(
            @Parameter(description = "Content status") @PathVariable ContentStatus status,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<ContentDTO> content = contentService.getContentByStatus(status, page, size);
        return ResponseEntity.ok(ApiResponse.success(content));
    }

    @Operation(summary = "Search content", description = "Search content by keyword")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ContentDTO>>> searchContent(
            @Parameter(description = "Search keyword") @RequestParam String keyword,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<ContentDTO> content = contentService.searchContent(keyword, page, size);
        return ResponseEntity.ok(ApiResponse.success(content));
    }

    @Operation(summary = "Get published content", description = "Retrieve all published content")
    @GetMapping("/published")
    public ResponseEntity<ApiResponse<PageResponse<ContentDTO>>> getPublishedContent(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {
        PageResponse<ContentDTO> content = contentService.getPublishedContent(page, size);
        return ResponseEntity.ok(ApiResponse.success(content));
    }

    @Operation(summary = "Update content status", description = "Change the status of a content item")
    @PreAuthorize("hasAuthority('CONTENT_WRITE')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ContentDTO>> updateStatus(
            @Parameter(description = "Content ID") @PathVariable String id,
            @Parameter(description = "New status") @RequestParam ContentStatus status) {
        String userId = getCurrentUserId();
        ContentDTO content = contentService.updateStatus(id, status, userId);
        return ResponseEntity.ok(ApiResponse.success("Status updated successfully", content));
    }

    @Operation(summary = "Publish content", description = "Publish a content item")
    @PreAuthorize("hasAuthority('CONTENT_PUBLISH')")
    @PostMapping("/{id}/publish")
    public ResponseEntity<ApiResponse<ContentDTO>> publishContent(
            @Parameter(description = "Content ID") @PathVariable String id) {
        String userId = getCurrentUserId();
        ContentDTO content = contentService.publishContent(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Content published successfully", content));
    }

    @Operation(summary = "Unpublish content", description = "Unpublish a content item")
    @PreAuthorize("hasAuthority('CONTENT_PUBLISH')")
    @PostMapping("/{id}/unpublish")
    public ResponseEntity<ApiResponse<ContentDTO>> unpublishContent(
            @Parameter(description = "Content ID") @PathVariable String id) {
        String userId = getCurrentUserId();
        ContentDTO content = contentService.unpublishContent(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Content unpublished successfully", content));
    }

    @Operation(summary = "Delete content", description = "Soft delete a content item")
    @PreAuthorize("hasAuthority('CONTENT_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContent(
            @Parameter(description = "Content ID") @PathVariable String id) {
        String userId = getCurrentUserId();
        contentService.deleteContent(id, userId);
        return ResponseEntity.ok(ApiResponse.success("Content deleted successfully", null));
    }

    @Operation(summary = "Restore content", description = "Restore a deleted content item")
    @PreAuthorize("hasAuthority('CONTENT_WRITE')")
    @PostMapping("/{id}/restore")
    public ResponseEntity<ApiResponse<ContentDTO>> restoreContent(
            @Parameter(description = "Content ID") @PathVariable String id) {
        ContentDTO content = contentService.restoreContent(id);
        return ResponseEntity.ok(ApiResponse.success("Content restored successfully", content));
    }

    @Operation(summary = "Get content by author", description = "Retrieve content items by author")
    @GetMapping("/author/{authorId}")
    @PreAuthorize("hasAuthority('CONTENT_READ')")
    public ResponseEntity<ApiResponse<List<ContentDTO>>> getContentByAuthor(
            @Parameter(description = "Author ID") @PathVariable String authorId) {
        List<ContentDTO> content = contentService.getContentByAuthor(authorId);
        return ResponseEntity.ok(ApiResponse.success(content));
    }

    @Operation(summary = "Get content by tags", description = "Retrieve content items by tags")
    @GetMapping("/tags")
    public ResponseEntity<ApiResponse<List<ContentDTO>>> getContentByTags(
            @Parameter(description = "Tags") @RequestParam List<String> tags) {
        List<ContentDTO> content = contentService.getContentByTags(tags);
        return ResponseEntity.ok(ApiResponse.success(content));
    }

    private String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof com.gogidix.corporatecms.application.security.UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return null;
    }
}
