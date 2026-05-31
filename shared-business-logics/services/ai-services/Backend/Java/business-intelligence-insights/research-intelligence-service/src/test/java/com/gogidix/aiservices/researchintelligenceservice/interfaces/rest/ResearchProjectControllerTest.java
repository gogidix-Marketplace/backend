package com.gogidix.aiservices.researchintelligenceservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.researchintelligenceservice.application.service.ResearchIntelligenceService;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.request.CreateProjectRequest;
import com.gogidix.aiservices.researchintelligenceservice.interfaces.dto.response.ProjectResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResearchProjectController.class)
@DisplayName("ResearchProjectController Tests")
class ResearchProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResearchIntelligenceService researchService;

    @Test
    @DisplayName("Should create project successfully")
    void shouldCreateProject() throws Exception {
        CreateProjectRequest request = new CreateProjectRequest(
                "tenant-001",
                "AI Research Project",
                "Research Description",
                ResearchProject.ResearchDomain.MACHINE_LEARNING,
                ResearchProject.Priority.HIGH,
                "creator@example.com"
        );

        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research Project",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.setDescription("Research Description");
        project.setPriority(ResearchProject.Priority.HIGH);
        project.setCreatedBy("creator@example.com");

        when(researchService.createProject(any(), any(), any(), any(), any())).thenReturn(project);

        mockMvc.perform(post("/api/v1/research-projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.projectId").value("proj-001"))
                .andExpect(jsonPath("$.title").value("AI Research Project"));
    }

    @Test
    @DisplayName("Should get project by id")
    void shouldGetProjectById() throws Exception {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(researchService.getProjectById("proj-001")).thenReturn(Optional.of(project));

        mockMvc.perform(get("/api/v1/research-projects/proj-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.projectId").value("proj-001"))
                .andExpect(jsonPath("$.title").value("AI Research"));
    }

    @Test
    @DisplayName("Should return 404 when project not found")
    void shouldReturn404WhenProjectNotFound() throws Exception {
        when(researchService.getProjectById("non-existent")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/research-projects/non-existent"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get projects by tenant")
    void shouldGetProjectsByTenant() throws Exception {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(researchService.getProjectsByTenant("tenant-001")).thenReturn(java.util.List.of(project));

        mockMvc.perform(get("/api/v1/research-projects/tenant/tenant-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].tenantId").value("tenant-001"));
    }

    @Test
    @DisplayName("Should get active projects")
    void shouldGetActiveProjects() throws Exception {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
            );

        when(researchService.getActiveProjects()).thenReturn(java.util.List.of(project));

        mockMvc.perform(get("/api/v1/research-projects/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("INITIATED"));
    }

    @Test
    @DisplayName("Should search projects by title")
    void shouldSearchProjectsByTitle() throws Exception {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "Machine Learning Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );

        when(researchService.searchProjectsByTitle("Machine")).thenReturn(java.util.List.of(project));

        mockMvc.perform(get("/api/v1/research-projects/search")
                        .param("title", "Machine"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Machine Learning Research"));
    }

    @Test
    @DisplayName("Should start project")
    void shouldStartProject() throws Exception {
        ResearchProject project = new ResearchProject(
                "proj-001",
                "tenant-001",
                "AI Research",
                ResearchProject.ResearchDomain.MACHINE_LEARNING
        );
        project.start();

        when(researchService.startProject("proj-001")).thenReturn(project);

        mockMvc.perform(post("/api/v1/research-projects/proj-001/start"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @DisplayName("Should delete project")
    void shouldDeleteProject() throws Exception {
        mockMvc.perform(delete("/api/v1/research-projects/proj-001"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should get project count")
    void shouldGetProjectCount() throws Exception {
        when(researchService.getProjectCountByTenant("tenant-001")).thenReturn(5);

        mockMvc.perform(get("/api/v1/research-projects/tenant/tenant-001/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }
}
