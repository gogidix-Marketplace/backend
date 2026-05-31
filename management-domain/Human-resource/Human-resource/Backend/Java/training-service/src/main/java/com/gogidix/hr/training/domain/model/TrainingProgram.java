package com.gogidix.hr.training.domain.model;

import com.gogidix.hr.training.domain.enums.ProgramStatus;
import com.gogidix.hr.training.domain.enums.ProgramType;
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
 * TrainingProgram Domain Entity
 * Represents a comprehensive training program
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "training_programs")
public class TrainingProgram extends BaseEntity {

    @Indexed(unique = true)
    private String programCode;

    @Indexed
    private String tenantId;

    private String programName;
    private String description;
    private String objectives;

    @Indexed
    private ProgramType programType;

    @Indexed
    private ProgramStatus status;

    @Indexed
    private String category;

    @Indexed
    private Integer duration;

    private String durationUnit; // HOURS, DAYS, WEEKS

    @Indexed
    private String difficultyLevel;

    @Indexed
    private String targetAudience;

    @Builder.Default
    private List<String> prerequisiteIds = new ArrayList<>();

    @Builder.Default
    private List<String> courseIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> curriculum = new HashMap<>();

    @Indexed
    private String instructorId;

    private String instructorName;
    private String instructorBio;

    @Indexed
    private Integer maxCapacity;

    private Integer currentEnrollment;
    private Integer minEnrollment;

    @Indexed
    private LocalDate startDate;

    @Indexed
    private LocalDate endDate;

    @Indexed
    private LocalDate enrollmentStartDate;

    @Indexed
    private LocalDate enrollmentEndDate;

    private String schedule; // FLEXIBLE, FIXED, SELF_PACED
    private String timing; // WEEKDAYS, WEEKENDS, EVENING

    @Indexed
    private Double cost;

    private String currency;
    private String costIncludes;

    @Indexed
    private String deliveryMode; // ONLINE, IN_PERSON, HYBRID, VIRTUAL

    @Builder.Default
    private Map<String, String> deliveryDetails = new HashMap<>();

    private String platform;
    private String location;
    private String venue;

    @Builder.Default
    private List<String> materials = new ArrayList<>();

    @Builder.Default
    private List<String> resourceIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> assessmentCriteria = new HashMap<>();

    @Builder.Default
    private Map<String, Object> completionRequirements = new HashMap<>();

    @Indexed
    private String certificate;

    private Boolean providesCertificate;
    private String certificateTemplate;

    @Indexed
    private Boolean isActive;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    /**
     * Creates a new training program
     */
    public static TrainingProgram create(String tenantId, String programName, String description,
                                          ProgramType programType, ProgramStatus status,
                                          Integer duration, String durationUnit,
                                          String difficultyLevel, String createdBy) {
        String programCode = generateProgramCode(tenantId, programType);

        TrainingProgram program = new TrainingProgram();
        program.setTenantId(tenantId);
        program.setProgramName(programName);
        program.setDescription(description);
        program.setProgramType(programType);
        program.setProgramCode(programCode);
        program.setStatus(status);
        program.setDuration(duration);
        program.setDurationUnit(durationUnit);
        program.setDifficultyLevel(difficultyLevel);
        program.setCreatedBy(createdBy);
        program.setIsActive(true);
        program.setProvidesCertificate(false);
        program.setDeliveryMode("ONLINE");
        program.setSchedule("FLEXIBLE");
        program.setCurrentEnrollment(0);
        program.setPrerequisiteIds(new ArrayList<>());
        program.setCourseIds(new ArrayList<>());
        program.setCurriculum(new HashMap<>());
        program.setDeliveryDetails(new HashMap<>());
        program.setMaterials(new ArrayList<>());
        program.setResourceIds(new ArrayList<>());
        program.setAssessmentCriteria(new HashMap<>());
        program.setCompletionRequirements(new HashMap<>());
        program.setTags(new ArrayList<>());
        program.setMetadata(new HashMap<>());

        return program;
    }

    /**
     * Activates program
     */
    public void activate() {
        this.isActive = true;
    }

    /**
     * Deactivates program
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Opens enrollment
     */
    public void openEnrollment() {
        if (!this.isActive) {
            throw new IllegalStateException("Cannot open enrollment for inactive program");
        }
        this.status = ProgramStatus.ENROLLMENT_OPEN;
    }

    /**
     * Closes enrollment
     */
    public void closeEnrollment() {
        if (this.status != ProgramStatus.ENROLLMENT_OPEN) {
            throw new IllegalStateException("Enrollment is not open");
        }
        this.status = ProgramStatus.ENROLLMENT_CLOSED;
    }

    /**
     * Starts program
     */
    public void start() {
        if (this.status != ProgramStatus.ENROLLMENT_CLOSED) {
            throw new IllegalStateException("Cannot start program that is not ready");
        }
        this.status = ProgramStatus.IN_PROGRESS;
    }

    /**
     * Completes program
     */
    public void complete() {
        if (this.status != ProgramStatus.IN_PROGRESS) {
            throw new IllegalStateException("Cannot complete program that is not in progress");
        }
        this.status = ProgramStatus.COMPLETED;
    }

    /**
     * Cancels program
     */
    public void cancel(String reason) {
        if (this.status == ProgramStatus.COMPLETED || this.status == ProgramStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel completed or already cancelled program");
        }
        this.status = ProgramStatus.CANCELLED;
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
     * Adds course
     */
    public void addCourse(String courseId) {
        if (this.courseIds == null) {
            this.courseIds = new ArrayList<>();
        }
        if (!this.courseIds.contains(courseId)) {
            this.courseIds.add(courseId);
        }
    }

    /**
     * Adds material
     */
    public void addMaterial(String material) {
        if (this.materials == null) {
            this.materials = new ArrayList<>();
        }
        if (!this.materials.contains(material)) {
            this.materials.add(material);
        }
    }

    /**
     * Adds resource
     */
    public void addResource(String resourceId) {
        if (this.resourceIds == null) {
            this.resourceIds = new ArrayList<>();
        }
        if (!this.resourceIds.contains(resourceId)) {
            this.resourceIds.add(resourceId);
        }
    }

    /**
     * Sets delivery detail
     */
    public void setDeliveryDetail(String key, String value) {
        if (this.deliveryDetails == null) {
            this.deliveryDetails = new HashMap<>();
        }
        this.deliveryDetails.put(key, value);
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
     * Checks if enrollment is open
     */
    public boolean isEnrollmentOpen() {
        if (this.status != ProgramStatus.ENROLLMENT_OPEN) {
            return false;
        }
        if (this.enrollmentStartDate != null && LocalDate.now().isBefore(this.enrollmentStartDate)) {
            return false;
        }
        if (this.enrollmentEndDate != null && LocalDate.now().isAfter(this.enrollmentEndDate)) {
            return false;
        }
        return true;
    }

    /**
     * Checks if is active
     */
    public boolean isActiveProgram() {
        return this.isActive && (this.status == ProgramStatus.ENROLLMENT_OPEN || this.status == ProgramStatus.IN_PROGRESS);
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
     * Generates program code
     */
    private static String generateProgramCode(String tenantId, ProgramType type) {
        String prefix = type.name().substring(0, 3).toUpperCase();
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "PRG-" + prefix + "-" + uniqueId;
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
