package com.gogidix.foundation.devtools.controller;

import com.gogidix.foundation.devtools.dto.DocumentationGenerationDto;
import com.gogidix.foundation.devtools.dto.DocumentationProjectDto;
import com.gogidix.foundation.devtools.dto.DocumentationRequest;
import com.gogidix.foundation.devtools.dto.DocumentationResult;
import com.gogidix.foundation.devtools.service.DocumentationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for documentation generation functionality.
 */
@RestController
@RequestMapping("/documentation")
@RequiredArgsConstructor
@Tag(name = "Documentation", description = "Documentation generation endpoints")
@CrossOrigin(origins = "${cors.allowed-origins}")
public class DocumentationController {

    private final DocumentationService documentationService;

    @PostMapping("/projects")
    @Operation(summary = "Create a new documentation project")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<DocumentationProjectDto> createProject(@Valid @RequestBody DocumentationProjectDto dto) {
        DocumentationProjectDto created = documentationService.createProject(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/projects/{uuid}")
    @Operation(summary = "Update an existing documentation project")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<DocumentationProjectDto> updateProject(
            @Parameter(description = "Project UUID") @PathVariable UUID uuid,
            @Valid @RequestBody DocumentationProjectDto dto) {
        DocumentationProjectDto updated = documentationService.updateProject(uuid, dto);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/projects/{uuid}")
    @Operation(summary = "Get a documentation project by UUID")
    public ResponseEntity<DocumentationProjectDto> getProject(
            @Parameter(description = "Project UUID") @PathVariable UUID uuid) {
        DocumentationProjectDto project = documentationService.getProject(uuid);
        return ResponseEntity.ok(project);
    }

    @GetMapping("/projects")
    @Operation(summary = "Get all documentation projects")
    public ResponseEntity<Page<DocumentationProjectDto>> getProjects(
            @Parameter(description = "Project ID") @RequestParam String projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<DocumentationProjectDto> projects = documentationService.getProjectsByProjectId(projectId, pageable);
        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/projects/{uuid}")
    @Operation(summary = "Delete a documentation project")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProject(
            @Parameter(description = "Project UUID") @PathVariable UUID uuid) {
        documentationService.deleteProject(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/projects/{id}/generate")
    @Operation(summary = "Generate documentation for a project")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public CompletableFuture<ResponseEntity<DocumentationResult>> generateDocumentation(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @RequestBody DocumentationRequest request) {

        return documentationService.generateDocumentation(id, request)
                .thenApply(result -> ResponseEntity.accepted().body(result));
    }

    @GetMapping("/projects/{id}/generations")
    @Operation(summary = "Get generation history for a project")
    public ResponseEntity<List<DocumentationGenerationDto>> getGenerationHistory(
            @Parameter(description = "Project ID") @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<DocumentationGenerationDto> history = documentationService.getGenerationHistory(id, pageable);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/generations/{uuid}")
    @Operation(summary = "Get a specific generation by UUID")
    public ResponseEntity<DocumentationGenerationDto> getGeneration(
            @Parameter(description = "Generation UUID") @PathVariable UUID uuid) {

        DocumentationGenerationDto generation = documentationService.getGeneration(uuid);
        return ResponseEntity.ok(generation);
    }

    @PostMapping("/preview")
    @Operation(summary = "Preview documentation as HTML")
    public ResponseEntity<String> previewDocumentation(
            @RequestBody String markdownContent) {

        String html = documentationService.previewDocumentation(markdownContent);
        return ResponseEntity.ok(html);
    }

    @GetMapping("/generate/api")
    @Operation(summary = "Generate API documentation")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<String> generateApiDocumentation(
            @RequestParam String projectId,
            @RequestParam String sourcePath) {

        String markdown = documentationService.generateApiDocumentation(projectId, sourcePath);
        return ResponseEntity.ok(markdown);
    }

    @GetMapping("/generate/database")
    @Operation(summary = "Generate database documentation")
    @PreAuthorize("hasRole('DEVELOPER') or hasRole('ADMIN')")
    public ResponseEntity<String> generateDatabaseDocumentation(
            @RequestParam(defaultValue = "public") String schemaName) {

        String markdown = documentationService.generateDatabaseDocumentation(schemaName);
        return ResponseEntity.ok(markdown);
    }
}
