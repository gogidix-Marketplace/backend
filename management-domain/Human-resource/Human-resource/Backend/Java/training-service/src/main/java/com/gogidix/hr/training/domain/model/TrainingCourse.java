package com.gogidix.hr.training.domain.model;

import com.gogidix.hr.training.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * TrainingCourse Domain Entity
 * Represents an individual training course
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "training_courses")
public class TrainingCourse extends BaseEntity {

    @Indexed(unique = true)
    private String courseCode;

    @Indexed
    private String tenantId;

    private String courseName;
    private String description;
    private String objectives;

    @Indexed
    private String category;

    @Indexed
    private Integer duration;

    private String durationUnit;

    @Indexed
    private String difficultyLevel;

    @Indexed
    private String programId;

    private String programName;

    @Indexed
    private Boolean isActive;

    @Indexed
    private String instructorId;

    private String instructorName;

    @Builder.Default
    private List<String> moduleIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> syllabus = new HashMap<>();

    @Builder.Default
    private List<String> prerequisiteIds = new ArrayList<>();

    @Builder.Default
    private List<String> learningOutcomes = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> resources = new HashMap<>();

    @Builder.Default
    private List<String> materialIds = new ArrayList<>();

    @Indexed
    private String deliveryMode;

    @Builder.Default
    private Map<String, Object> schedule = new HashMap<>();

    @Indexed
    private Double cost;

    private String currency;

    @Indexed
    private Integer maxCapacity;

    private Integer currentEnrollment;

    @Indexed
    private LocalDate startDate;

    @Indexed
    private LocalDate endDate;

    @Builder.Default
    private Map<String, Object> assessment = new HashMap<>();

    private String passingScore;
    private String certificate;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Creates a new training course
     */
    public static TrainingCourse create(String tenantId, String courseName, String description,
                                         String category, Integer duration, String durationUnit,
                                         String difficultyLevel, String createdBy) {
        String courseCode = generateCourseCode(tenantId, category);

        TrainingCourse course = new TrainingCourse();
        course.setTenantId(tenantId);
        course.setCourseName(courseName);
        course.setDescription(description);
        course.setCategory(category);
        course.setCourseCode(courseCode);
        course.setDuration(duration);
        course.setDurationUnit(durationUnit);
        course.setDifficultyLevel(difficultyLevel);
        course.setCreatedBy(createdBy);
        course.setIsActive(true);
        course.setDeliveryMode("ONLINE");
        course.setCurrentEnrollment(0);
        course.setModuleIds(new ArrayList<>());
        course.setSyllabus(new HashMap<>());
        course.setPrerequisiteIds(new ArrayList<>());
        course.setLearningOutcomes(new ArrayList<>());
        course.setResources(new HashMap<>());
        course.setMaterialIds(new ArrayList<>());
        course.setSchedule(new HashMap<>());
        course.setAssessment(new HashMap<>());
        course.setTags(new ArrayList<>());
        course.setMetadata(new HashMap<>());

        return course;
    }

    /**
     * Activates course
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Deactivates course
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Adds module
     */
    public void addModule(String moduleId) {
        if (this.moduleIds == null) {
            this.moduleIds = new ArrayList<>();
        }
        if (!this.moduleIds.contains(moduleId)) {
            this.moduleIds.add(moduleId);
        }
    }

    /**
     * Adds prerequisite
     */
    public void addPrerequisite(String prerequisiteId) {
        if (this.prerequisiteIds == null) {
            this.prerequisiteIds = new ArrayList<>();
        }
        if (!this.prerequisiteIds.contains(prerequisiteId)) {
            this.prerequisiteIds.add(prerequisiteId);
        }
    }

    /**
     * Adds learning outcome
     */
    public void addLearningOutcome(String outcome) {
        if (this.learningOutcomes == null) {
            this.learningOutcomes = new ArrayList<>();
        }
        if (!this.learningOutcomes.contains(outcome)) {
            this.learningOutcomes.add(outcome);
        }
    }

    /**
     * Adds resource
     */
    public void addResource(String type, String url) {
        if (this.resources == null) {
            this.resources = new HashMap<>();
        }
        this.resources.put(type, url);
    }

    /**
     * Adds material
     */
    public void addMaterial(String materialId) {
        if (this.materialIds == null) {
            this.materialIds = new ArrayList<>();
        }
        if (!this.materialIds.contains(materialId)) {
            this.materialIds.add(materialId);
        }
    }

    /**
     * Increments enrollment
     */
    public void incrementEnrollment() {
        this.currentEnrollment = (this.currentEnrollment != null ? this.currentEnrollment : 0) + 1;
    }

    /**
     * Decrements enrollment
     */
    public void decrementEnrollment() {
        this.currentEnrollment = Math.max(0, (this.currentEnrollment != null ? this.currentEnrollment : 0) - 1);
    }

    /**
     * Checks if is full
     */
    public boolean isFull() {
        return this.maxCapacity != null && this.currentEnrollment != null &&
               this.currentEnrollment >= this.maxCapacity;
    }

    /**
     * Checks if is active
     */
    public boolean isActiveCourse() {
        return this.isActive;
    }

    /**
     * Adds tag
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Generates course code
     */
    private static String generateCourseCode(String tenantId, String category) {
        String normalized = category.toUpperCase().replaceAll("\\s+", "_");
        String uniqueId = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return "CRS-" + normalized + "-" + uniqueId;
    }

    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }
}
