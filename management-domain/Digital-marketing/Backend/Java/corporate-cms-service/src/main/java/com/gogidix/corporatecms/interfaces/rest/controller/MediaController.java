package com.gogidix.corporatecms.interfaces.rest.controller;

import com.gogidix.corporatecms.application.dto.ApiResponse;
import com.gogidix.corporatecms.application.dto.MediaDTO;
import com.gogidix.corporatecms.application.dto.PageResponse;
import com.gogidix.corporatecms.domain.enums.MediaType;
import com.gogidix.corporatecms.domain.service.MediaService;
import com.gogidix.corporatecms.interfaces.rest.validator.ValidEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for media management.
 */
@Tag(name = "Media", description = "Media management APIs")
@RestController
@RequestMapping("/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @Operation(summary = "Upload media file", description = "Upload a single media file")
    @PreAuthorize("hasAuthority('MEDIA_MANAGE')")
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<MediaDTO>> uploadMedia(
            @Parameter(description = "Media file") @RequestParam("file") MultipartFile file,
            @Parameter(description = "Alt text") @RequestParam(required = false) String altText,
            @Parameter(description = "Caption") @RequestParam(required = false) String caption) {
        String userId = getCurrentUserId();
        MediaDTO media = mediaService.uploadMedia(file, userId, altText, caption);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(media));
    }

    @Operation(summary = "Get media by ID", description = "Retrieve media by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MediaDTO>> getMediaById(
            @Parameter(description = "Media ID") @PathVariable String id) {
        MediaDTO media = mediaService.getMediaById(id);
        return ResponseEntity.ok(ApiResponse.success(media));
    }

    @Operation(summary = "Get media by type", description = "Retrieve media filtered by type")
    @GetMapping("/type/{type}")
    public ResponseEntity<ApiResponse<List<MediaDTO>>> getMediaByType(
            @Parameter(description = "Media type") @PathVariable MediaType type) {
        List<MediaDTO> media = mediaService.getMediaByType(type);
        return ResponseEntity.ok(ApiResponse.success(media));
    }

    @Operation(summary = "Search media", description = "Search media by keyword")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MediaDTO>>> searchMedia(
            @Parameter(description = "Search keyword") @RequestParam String keyword) {
        List<MediaDTO> media = mediaService.searchMedia(keyword);
        return ResponseEntity.ok(ApiResponse.success(media));
    }

    @Operation(summary = "Update media metadata", description = "Update media metadata")
    @PreAuthorize("hasAuthority('MEDIA_MANAGE')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MediaDTO>> updateMedia(
            @Parameter(description = "Media ID") @PathVariable String id,
            @Valid @RequestBody MediaDTO dto) {
        MediaDTO media = mediaService.updateMedia(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Media updated successfully", media));
    }

    @Operation(summary = "Delete media", description = "Delete a media file")
    @PreAuthorize("hasAuthority('MEDIA_MANAGE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMedia(
            @Parameter(description = "Media ID") @PathVariable String id) {
        mediaService.deleteMedia(id);
        return ResponseEntity.ok(ApiResponse.success("Media deleted successfully", null));
    }

    @Operation(summary = "Download media", description = "Download a media file")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadMedia(
            @Parameter(description = "Media ID") @PathVariable String id) {
        byte[] fileContent = mediaService.downloadMedia(id);
        MediaDTO media = mediaService.getMediaById(id);

        ByteArrayResource resource = new ByteArrayResource(fileContent);

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.parseMediaType(media.getMimeType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + media.getOriginalFileName() + "\"")
                .body(resource);
    }

    @Operation(summary = "Get media by uploader", description = "Retrieve media uploaded by a specific user")
    @GetMapping("/uploader/{uploaderId}")
    @PreAuthorize("hasAuthority('MEDIA_MANAGE')")
    public ResponseEntity<ApiResponse<List<MediaDTO>>> getMediaByUploader(
            @Parameter(description = "Uploader ID") @PathVariable String uploaderId) {
        List<MediaDTO> media = mediaService.getMediaByUploader(uploaderId);
        return ResponseEntity.ok(ApiResponse.success(media));
    }

    private String getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof com.gogidix.corporatecms.application.security.UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return null;
    }

    private String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof com.gogidix.corporatecms.application.security.UserDetailsImpl userDetails) {
            return userDetails.getUsername();
        }
        return null;
    }
}
