package com.gogidix.aiservices.researchintelligenceservice.infrastructure.persistence.mongo;

import com.gogidix.aiservices.researchintelligenceservice.domain.aggregate.ResearchProjectAggregate;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Publication;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchData;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchFinding;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.ResearchProject;
import com.gogidix.aiservices.researchintelligenceservice.domain.model.Researcher;
import com.gogidix.aiservices.researchintelligenceservice.domain.repository.ResearchProjectRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class MongoResearchProjectRepository implements ResearchProjectRepository {

    private final MongoTemplate mongoTemplate;

    public MongoResearchProjectRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public ResearchProject save(ResearchProject project) {
        ResearchProjectDocument document = toDocument(project);
        mongoTemplate.save(document);
        return toEntity(document);
    }

    @Override
    public ResearchProjectAggregate saveAggregate(ResearchProjectAggregate aggregate) {
        ResearchProject project = save(aggregate.getProject());
        return new ResearchProjectAggregate(project, aggregate.getResearchers(),
                aggregate.getFindings(), aggregate.getPublications(), aggregate.getDatasets());
    }

    @Override
    public Optional<ResearchProject> findById(String id) {
        ResearchProjectDocument document = mongoTemplate.findById(id, ResearchProjectDocument.class);
        return Optional.ofNullable(document).map(this::toEntity);
    }

    @Override
    public Optional<ResearchProject> findByProjectId(String projectId) {
        Query query = new Query(Criteria.where("projectId").is(projectId));
        ResearchProjectDocument document = mongoTemplate.findOne(query, ResearchProjectDocument.class);
        return Optional.ofNullable(document).map(this::toEntity);
    }

    @Override
    public List<ResearchProject> findByTenantId(String tenantId) {
        Query query = new Query(Criteria.where("tenantId").is(tenantId));
        return mongoTemplate.find(query, ResearchProjectDocument.class).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<ResearchProject> findByStatus(ResearchProject.ProjectStatus status) {
        Query query = new Query(Criteria.where("status").is(status.name()));
        return mongoTemplate.find(query, ResearchProjectDocument.class).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<ResearchProject> findByDomain(ResearchProject.ResearchDomain domain) {
        Query query = new Query(Criteria.where("domain").is(domain.name()));
        return mongoTemplate.find(query, ResearchProjectDocument.class).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<ResearchProject> findByCreatedBy(String createdBy) {
        Query query = new Query(Criteria.where("createdBy").is(createdBy));
        return mongoTemplate.find(query, ResearchProjectDocument.class).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<ResearchProject> findActiveProjects() {
        Query query = new Query(Criteria.where("status")
                .in(ResearchProject.ProjectStatus.INITIATED.name(),
                    ResearchProject.ProjectStatus.IN_PROGRESS.name()));
        return mongoTemplate.find(query, ResearchProjectDocument.class).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<ResearchProject> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        Query query = new Query(Criteria.where("createdAt").gte(startDate).lte(endDate));
        return mongoTemplate.find(query, ResearchProjectDocument.class).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<ResearchProject> findByTitleContaining(String title) {
        Query query = new Query(Criteria.where("title").regex(title, "i"));
        return mongoTemplate.find(query, ResearchProjectDocument.class).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public List<ResearchProject> findAll() {
        return mongoTemplate.findAll(ResearchProjectDocument.class).stream()
                .map(this::toEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        mongoTemplate.remove(id);
    }

    @Override
    public boolean existsByProjectId(String projectId) {
        Query query = new Query(Criteria.where("projectId").is(projectId));
        return mongoTemplate.exists(query, ResearchProjectDocument.class);
    }

    @Override
    public int countByTenantId(String tenantId) {
        Query query = new Query(Criteria.where("tenantId").is(tenantId));
        return Math.toIntExact(mongoTemplate.count(query, ResearchProjectDocument.class));
    }

    @Override
    public int countByStatus(ResearchProject.ProjectStatus status) {
        Query query = new Query(Criteria.where("status").is(status.name()));
        return Math.toIntExact(mongoTemplate.count(query, ResearchProjectDocument.class));
    }

    private ResearchProjectDocument toDocument(ResearchProject entity) {
        List<String> researcherIds = entity.getResearchers().stream()
                .map(Researcher::getResearcherId)
                .toList();

        List<String> findingIds = entity.getFindings().stream()
                .map(ResearchFinding::getFindingId)
                .toList();

        List<String> publicationIds = entity.getPublications().stream()
                .map(Publication::getPublicationId)
                .toList();

        return new ResearchProjectDocument(
                entity.getProjectId(),
                entity.getTenantId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus().name(),
                entity.getDomain().name(),
                entity.getPriority().name(),
                researcherIds,
                findingIds,
                publicationIds,
                entity.getCreatedAt().toString(),
                entity.getUpdatedAt().toString(),
                entity.getCompletedAt() != null ? entity.getCompletedAt().toString() : null,
                entity.getCreatedBy(),
                entity.getProgressPercentage()
        );
    }

    private ResearchProject toEntity(ResearchProjectDocument document) {
        ResearchProject project = new ResearchProject(
                document.projectId(),
                document.tenantId(),
                document.title(),
                ResearchProject.ResearchDomain.valueOf(document.domain())
        );
        project.setDescription(document.description());
        project.setStatus(ResearchProject.ProjectStatus.valueOf(document.status()));
        project.setPriority(ResearchProject.Priority.valueOf(document.priority()));
        project.setCreatedBy(document.createdBy());
        project.setProgressPercentage(document.progressPercentage());

        return project;
    }

    record ResearchProjectDocument(
            String projectId,
            String tenantId,
            String title,
            String description,
            String status,
            String domain,
            String priority,
            List<String> researcherIds,
            List<String> findingIds,
            List<String> publicationIds,
            String createdAt,
            String updatedAt,
            String completedAt,
            String createdBy,
            double progressPercentage
    ) {}
}
