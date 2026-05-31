package com.gogidix.shared.infrastructure.services.storage.filestorage.interfaces.rest;

import com.gogidix.shared.infrastructure.services.storage.filestorage.application.service.FileStorageService;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in.FileMetadata;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in.FileDownload;
import com.gogidix.shared.infrastructure.services.storage.filestorage.domain.port.in.FileStoragePort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * REST controller for file storage.
 */
@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Tag(name = "File Storage", description = "File upload and download APIs")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    @Operation(summary = "Upload file", description = "Upload a new file")
    public ResponseEntity<FileMetadata> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId) {
        FileMetadata metadata = fileStorageService.uploadFile(file, userId);
        return ResponseEntity.ok(metadata);
    }

    @GetMapping("/{fileId}")
    @Operation(summary = "Download file", description = "Download a file by ID")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) {
        FileDownload download = fileStorageService.downloadFile(fileId);

        if (download == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(download.metadata().contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + download.metadata().fileName() + "\"")
                .body(new InputStreamResource(download.inputStream()));
    }

    @GetMapping("/{fileId}/metadata")
    @Operation(summary = "Get file metadata", description = "Get file metadata by ID")
    public ResponseEntity<FileMetadata> getFileMetadata(@PathVariable String fileId) {
        FileMetadata metadata = fileStorageService.getFileMetadata(fileId);
        return metadata != null ? ResponseEntity.ok(metadata)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{fileId}")
    @Operation(summary = "Delete file", description = "Delete a file by ID")
    public ResponseEntity<Void> deleteFile(@PathVariable String fileId) {
        fileStorageService.deleteFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "List user files", description = "List all files for a user")
    public ResponseEntity<List<FileMetadata>> listUserFiles(@PathVariable String userId) {
        List<FileMetadata> files = fileStorageService.listUserFiles(userId);
        return ResponseEntity.ok(files);
    }
}
