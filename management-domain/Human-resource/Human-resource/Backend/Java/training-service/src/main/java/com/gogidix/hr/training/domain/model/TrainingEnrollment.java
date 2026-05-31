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
 * TrainingEnrollment Domain Entity
 * Represents an employee's enrollment in a training program
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "training_enrollments")
public class TrainingEnrollment extends BaseEntity {

    @Indexed(unique = true)
    private String enrollmentCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String programId;

    private String programName;

    @Indexed
    private String courseId;

    private String courseName;

    @Indexed
    private String employeeId;

    private String employeeName;
    private String employeeEmail;

    @Indexed
    private EnrollmentStatus status;

    @Indexed
    private LocalDate enrollmentDate;

    @Indexed
    private LocalDate startDate;

    @Indexed
    private LocalDate endDate;

    @Indexed
    private String enrolledBy;

    @Indexed
    private String approvedBy;

    private LocalDate approvalDate;

    @Indexed
    private Boolean isMandatory;

    private String enrollmentReason;
    private String managerComments;

    @Builder.Default
    private Map<String, Object> progress = new HashMap<>();

    private Integer completionPercentage;

    @Builder.Default
    private List<String> completedModuleIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> assessmentScores = new HashMap<>();

    private String finalScore;
    private Boolean passed;

    @Indexed
    private String certificateId;

    @Builder.Default
    private List<String> prerequisiteWaiverIds = new ArrayList<>();

    @Indexed
    private Double cost;

    private String costCenter;
    private String billingCode;

    @Indexed
    private Boolean isPaid;

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    public enum EnrollmentStatus {
        PENDING_APPROVAL,
        APPROVED,
        REJECTED,
        ENROLLED,
        IN_PROGRESS,
        ON_HOLD,
        COMPLETED,
        DROPPED,
        FAILED,
        CANCELLED
    }

    /**
     * Creates a new training enrollment
     */
    public static TrainingEnrollment create(String tenantId, String programId, String programName,
                                             String courseId, String courseName,
                                             String employeeId, String employeeName, String employeeEmail,
                                             String enrolledBy, Boolean isMandatory) {
        String enrollmentCode = generateEnrollmentCode(programId, employeeId);

        TrainingEnrollment enrollment = new TrainingEnrollment();
        enrollment.setTenantId(tenantId);
        enrollment.setProgramId(programId);
        enrollment.setProgramName(programName);
        enrollment.setCourseId(courseId);
        enrollment.setCourseName(courseName);
        enrollment.setEmployeeId(employeeId);
        enrollment.setEmployeeName(employeeName);
        enrollment.setEmployeeEmail(employeeEmail);
        enrollment.setEnrollmentCode(enrollmentCode);
        enrollment.setEnrolledBy(enrolledBy);
        enrollment.setIsMandatory(isMandatory != null ? isMandatory : false);
        enrollment.setStatus(EnrollmentStatus.PENDING_APPROVAL);
        enrollment.setEnrollmentDate(LocalDate.now());
        enrollment.setIsPaid(false);
        enrollment.setProgress(new HashMap<>());
        enrollment.setCompletedModuleIds(new ArrayList<>());
        enrollment.setAssessmentScores(new HashMap<>());
        enrollment.setPrerequisiteWaiverIds(new ArrayList<>());
        enrollment.setMetadata(new HashMap<>());

        return enrollment;
    }

    /**
     * Approves enrollment
     */
    public void approve(String approvedBy) {
        if (this.status != EnrollmentStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Can only approve pending enrollments");
        }
        this.status = EnrollmentStatus.APPROVED;
        this.approvedBy = approvedBy;
        this.approvalDate = LocalDate.now();
    }

    /**
     * Rejects enrollment
     */
    public void reject(String reason) {
        if (this.status != EnrollmentStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("Can only reject pending enrollments");
        }
        this.status = EnrollmentStatus.REJECTED;
        this.managerComments = reason;
    }

    /**
     * Confirms enrollment
     */
    public void confirm() {
        if (this.status != EnrollmentStatus.APPROVED) {
            throw new IllegalStateException("Can only confirm approved enrollments");
        }
        this.status = EnrollmentStatus.ENROLLED;
    }

    /**
     * Starts training
     */
    public void start() {
        if (this.status != EnrollmentStatus.ENROLLED) {
            throw new IllegalStateException("Can only start confirmed enrollments");
        }
        this.status = EnrollmentStatus.IN_PROGRESS;
        if (this.startDate == null) {
            this.startDate = LocalDate.now();
        }
    }

    /**
     * Updates progress
     */
    public void updateProgress(Integer completionPercentage) {
        if (this.status != EnrollmentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only update progress for in-progress enrollments");
        }
        this.completionPercentage = completionPercentage;
        if (completionPercentage >= 100) {
            this.status = EnrollmentStatus.COMPLETED;
        }
    }

    /**
     * Completes module
     */
    public void completeModule(String moduleId) {
        if (this.completedModuleIds == null) {
            this.completedModuleIds = new ArrayList<>();
        }
        if (!this.completedModuleIds.contains(moduleId)) {
            this.completedModuleIds.add(moduleId);
        }
    }

    /**
     * Sets assessment score
     */
    public void setAssessmentScore(String assessmentName, Object score) {
        if (this.assessmentScores == null) {
            this.assessmentScores = new HashMap<>();
        }
        this.assessmentScores.put(assessmentName, score);
    }

    /**
     * Puts on hold
     */
    public void putOnHold(String reason) {
        if (this.status != EnrollmentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only put in-progress enrollments on hold");
        }
        this.status = EnrollmentStatus.ON_HOLD;
        this.managerComments = reason;
    }

    /**
     * Resumes
     */
    public void resume() {
        if (this.status != EnrollmentStatus.ON_HOLD) {
            throw new IllegalStateException("Can only resume enrollments that are on hold");
        }
        this.status = EnrollmentStatus.IN_PROGRESS;
    }

    /**
     * Drops enrollment
     */
    public void drop(String reason) {
        if (this.status == EnrollmentStatus.COMPLETED || this.status == EnrollmentStatus.DROPPED) {
            throw new IllegalStateException("Cannot drop completed or already dropped enrollments");
        }
        this.status = EnrollmentStatus.DROPPED;
        this.managerComments = reason;
    }

    /**
     * Cancels enrollment
     */
    public void cancel(String reason) {
        if (this.status == EnrollmentStatus.COMPLETED || this.status == EnrollmentStatus.CANCELLED) {
            throw new IllegalStateException("Cannot cancel completed or already cancelled enrollments");
        }
        this.status = EnrollmentStatus.CANCELLED;
        this.managerComments = reason;
    }

    /**
     * Marks as failed
     */
    public void fail(String finalScore) {
        if (this.status != EnrollmentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only fail in-progress enrollments");
        }
        this.status = EnrollmentStatus.FAILED;
        this.finalScore = finalScore;
        this.passed = false;
    }

    /**
     * Marks as completed
     */
    public void markCompleted(String finalScore, Boolean passed, String certificateId) {
        if (this.status != EnrollmentStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only complete in-progress enrollments");
        }
        this.status = EnrollmentStatus.COMPLETED;
        this.finalScore = finalScore;
        this.passed = passed;
        this.certificateId = certificateId;
        this.endDate = LocalDate.now();
    }

    /**
     * Adds prerequisite waiver
     */
    public void addPrerequisiteWaiver(String waiverId) {
        if (this.prerequisiteWaiverIds == null) {
            this.prerequisiteWaiverIds = new ArrayList<>();
        }
        if (!this.prerequisiteWaiverIds.contains(waiverId)) {
            this.prerequisiteWaiverIds.add(waiverId);
        }
    }

    /**
     * Marks as paid
     */
    public void markAsPaid() {
        this.isPaid = true;
    }

    /**
     * Checks if is active
     */
    public boolean isActive() {
        return this.status == EnrollmentStatus.ENROLLED || this.status == EnrollmentStatus.IN_PROGRESS;
    }

    /**
     * Checks if is completed
     */
    public boolean isCompleted() {
        return this.status == EnrollmentStatus.COMPLETED;
    }

    /**
     * Generates enrollment code
     */
    private static String generateEnrollmentCode(String programId, String employeeId) {
        return "ENR-" + programId.substring(0, 8) + "-" + employeeId.substring(0, 8);
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
