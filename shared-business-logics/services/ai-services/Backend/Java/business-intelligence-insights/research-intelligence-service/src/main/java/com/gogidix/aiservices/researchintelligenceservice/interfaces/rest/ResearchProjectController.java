package com.gogidix.aiservices.researchintelligenceservice.interfaces.rest;

import com.gogidix.aiservices.researchintelligenceservice.application.service.ResearchIntelligenceService;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchFinding;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Researcher;
import com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.request.CreateProjectRequest;
import com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.request.UpdateProjectRequest;
import com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.response.ProjectResponse;
import com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.response.ResearcherResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/research-projects")
public class ResearchProjectController {

    private final ResearchIntelligenceService researchService;

    public ResearchProjectController(ResearchIntelligenceService researchService) {
        this.researchService = researchService;
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody CreateProjectRequest request) {
        ResearchProject project = researchService.createProject(
                request.tenantId(),
                request.title(),
                request.description(),
                request.domain(),
                request.createdBy()
        );
        if (request.priority() != null) {
            project.setPriority(request.priority());
            researchService.updateProject(project.getProjectId(), null, null, null, request.priority());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectResponse.from(project));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getProject(@PathVariable String id) {
        Optional<ResearchProject> project = researchService.getProjectById(id);
        return project.map(value -> ResponseEntity.ok(ProjectResponse.from(value)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tenant/{tenantId}")
    public ResponseEntity<List<ProjectResponse>> getProjectsByTenant(@PathVariable String tenantId) {
        List<ProjectResponse> projects = researchService.getProjectsByTenant(tenantId).stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ProjectResponse>> getProjectsByStatus(@PathVariable ResearchProject.ProjectStatus status) {
        List<ProjectResponse> projects = researchService.getProjectsByStatus(status).stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/domain/{domain}")
    public ResponseEntity<List<ProjectResponse>> getProjectsByDomain(@PathVariable ResearchProject.ResearchDomain domain) {
        List<ProjectResponse> projects = researchService.getProjectsByDomain(domain).stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProjectResponse>> getActiveProjects() {
        List<ProjectResponse> projects = researchService.getActiveProjects().stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProjectResponse>> searchProjects(@RequestParam String title) {
        List<ProjectResponse> projects = researchService.searchProjectsByTitle(title).stream()
                .map(ProjectResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectResponse> updateProject(@PathVariable String projectId,
                                                         @Valid @RequestBody UpdateProjectRequest request) {
        ResearchProject updated = researchService.updateProject(
                projectId,
                request.title(),
                request.description(),
                request.domain(),
                request.priority()
        );
        if (updated != null) {
            return ResponseEntity.ok(ProjectResponse.from(updated));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{projectId}/start")
    public ResponseEntity<ProjectResponse> startProject(@PathVariable String projectId) {
        ResearchProject project = researchService.startProject(projectId);
        if (project != null) {
            return ResponseEntity.ok(ProjectResponse.from(project));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{projectId}/complete")
    public ResponseEntity<ProjectResponse> completeProject(@PathVariable String projectId) {
        ResearchProject project = researchService.completeProject(projectId);
        if (project != null) {
            return ResponseEntity.ok(ProjectResponse.from(project));
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{projectId}/cancel")
    public ResponseEntity<ProjectResponse> cancelProject(@PathVariable String projectId) {
        ResearchProject project = researchService.cancelProject(projectId);
        if (project != null) {
            return ResponseEntity.ok(ProjectResponse.from(project));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable String id) {
        researchService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{projectId}/aggregate")
    public ResponseEntity<?> getProjectAggregate(@PathVariable String projectId) {
        var aggregate = researchService.getProjectAggregate(projectId);
        if (aggregate != null) {
            return ResponseEntity.ok(aggregate);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/tenant/{tenantId}/count")
    public ResponseEntity<Integer> getProjectCount(@PathVariable String tenantId) {
        int count = researchService.getProjectCountByTenant(tenantId);
        return ResponseEntity.ok(count);
    }
}
