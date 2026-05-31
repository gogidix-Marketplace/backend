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
 * TrainingCompletion Domain Entity
 * Records training completion details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "training_completions")
public class TrainingCompletion extends BaseEntity {

    @Indexed(unique = true)
    private String completionCode;

    @Indexed
    private String tenantId;

    @Indexed
    private String enrollmentId;

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
    private CompletionStatus status;

    @Indexed
    private LocalDate completionDate;

    @Indexed
    private LocalDate dueDate;

    private Integer timeSpentHours;
    private Integer timeSpentMinutes;

    @Builder.Default
    private Map<String, Object> assessmentResults = new HashMap<>();

    private String finalScore;
    private Integer maxScore;
    private Double percentageScore;

    @Indexed
    private Boolean passed;

    private String grade;
    private String performanceLevel;

    @Indexed
    private String certificateId;

    private String certificateUrl;
    private LocalDate certificateExpiryDate;

    @Indexed
    private String verifiedBy;

    private LocalDate verificationDate;

    @Builder.Default
    private List<String> completedModuleIds = new ArrayList<>();

    @Builder.Default
    private Map<String, Object> moduleScores = new HashMap<>();

    @Builder.Default
    private List<String> skillIdsAcquired = new ArrayList<>();

    @Builder.Default
    private List<String> competencyIdsImproved = new ArrayList<>();

    private String feedback;
    private String instructorComments;

    @Indexed
    private String nextRecommendedCourseId;

    @Builder.Default
    private Map<String, Object> metadata = new HashMap<>();

    public enum CompletionStatus {
        IN_PROGRESS,
        SUBMITTED_FOR_REVIEW,
        COMPLETED,
        VERIFIED,
        FAILED,
        EXPIRED,
        REVOKED
    }

    /**
     * Creates a new training completion record
     */
    public static TrainingCompletion create(String tenantId, String enrollmentId,
                                             String programId, String programName,
                                             String courseId, String courseName,
                                             String employeeId, String employeeName,
                                             LocalDate dueDate) {
        String completionCode = generateCompletionCode(courseId, employeeId);

        TrainingCompletion completion = new TrainingCompletion();
        completion.setTenantId(tenantId);
        completion.setEnrollmentId(enrollmentId);
        completion.setProgramId(programId);
        completion.setProgramName(programName);
        completion.setCourseId(courseId);
        completion.setCourseName(courseName);
        completion.setEmployeeId(employeeId);
        completion.setEmployeeName(employeeName);
        completion.setCompletionCode(completionCode);
        completion.setStatus(CompletionStatus.IN_PROGRESS);
        completion.setDueDate(dueDate);
        completion.setPassed(false);
        completion.setCompletedModuleIds(new ArrayList<>());
        completion.setModuleScores(new HashMap<>());
        completion.setAssessmentResults(new HashMap<>());
        completion.setSkillIdsAcquired(new ArrayList<>());
        completion.setCompetencyIdsImproved(new ArrayList<>());
        completion.setMetadata(new HashMap<>());

        return completion;
    }

    /**
     * Submits for review
     */
    public void submitForReview() {
        if (this.status != CompletionStatus.IN_PROGRESS) {
            throw new IllegalStateException("Can only submit in-progress completions for review");
        }
        this.status = CompletionStatus.SUBMITTED_FOR_REVIEW;
    }

    /**
     * Marks as completed
     */
    public void markCompleted(String finalScore, Integer maxScore, Boolean passed) {
        if (this.status != CompletionStatus.IN_PROGRESS &&
            this.status != CompletionStatus.SUBMITTED_FOR_REVIEW) {
            throw new IllegalStateException("Can only mark in-progress or submitted completions as complete");
        }
        this.status = CompletionStatus.COMPLETED;
        this.finalScore = finalScore;
        this.maxScore = maxScore;
        this.passed = passed;
        this.completionDate = LocalDate.now();

        if (maxScore != null && finalScore != null) {
            try {
                double score = Double.parseDouble(finalScore);
                double max = maxScore.doubleValue();
                this.percentageScore = (score / max) * 100;
            } catch (NumberFormatException e) {
                // Handle case where finalScore is not numeric
            }
        }

        // Determine grade
        determineGrade();
    }

    /**
     * Verifies completion
     */
    public void verify(String verifiedBy) {
        if (this.status != CompletionStatus.COMPLETED) {
            throw new IllegalStateException("Can only verify completed records");
        }
        this.status = CompletionStatus.VERIFIED;
        this.verifiedBy = verifiedBy;
        this.verificationDate = LocalDate.now();
    }

    /**
     * Marks as failed
     */
    public void markFailed(String reason) {
        this.status = CompletionStatus.FAILED;
        this.passed = false;
        this.feedback = reason;
        this.completionDate = LocalDate.now();
    }

    /**
     * Revokes completion
     */
    public void revoke(String reason) {
        if (this.status != CompletionStatus.VERIFIED) {
            throw new IllegalStateException("Can only revoke verified completions");
        }
        this.status = CompletionStatus.REVOKED;
        this.feedback = reason;
    }

    /**
     * Marks as expired
     */
    public void markAsExpired() {
        if (this.status == CompletionStatus.VERIFIED &&
            this.certificateExpiryDate != null &&
            LocalDate.now().isAfter(this.certificateExpiryDate)) {
            this.status = CompletionStatus.EXPIRED;
        }
    }

    /**
     * Adds completed module
     */
    public void addCompletedModule(String moduleId, String score) {
        if (this.completedModuleIds == null) {
            this.completedModuleIds = new ArrayList<>();
        }
        if (!this.completedModuleIds.contains(moduleId)) {
            this.completedModuleIds.add(moduleId);
        }
        if (this.moduleScores == null) {
            this.moduleScores = new HashMap<>();
        }
        this.moduleScores.put(moduleId, score);
    }

    /**
     * Adds assessment result
     */
    public void addAssessmentResult(String assessmentName, Object result) {
        if (this.assessmentResults == null) {
            this.assessmentResults = new HashMap<>();
        }
        this.assessmentResults.put(assessmentName, result);
    }

    /**
     * Adds acquired skill
     */
    public void addAcquiredSkill(String skillId) {
        if (this.skillIdsAcquired == null) {
            this.skillIdsAcquired = new ArrayList<>();
        }
        if (!this.skillIdsAcquired.contains(skillId)) {
            this.skillIdsAcquired.add(skillId);
        }
    }

    /**
     * Adds improved competency
     */
    public void addImprovedCompetency(String competencyId) {
        if (this.competencyIdsImproved == null) {
            this.competencyIdsImproved = new ArrayList<>();
        }
        if (!this.competencyIdsImproved.contains(competencyId)) {
            this.competencyIdsImproved.add(competencyId);
        }
    }

    /**
     * Sets certificate
     */
    public void setCertificate(String certificateId, String certificateUrl, LocalDate expiryDate) {
        this.certificateId = certificateId;
        this.certificateUrl = certificateUrl;
        this.certificateExpiryDate = expiryDate;
    }

    /**
     * Sets time spent
     */
    public void setTimeSpent(Integer hours, Integer minutes) {
        this.timeSpentHours = hours;
        this.timeSpentMinutes = minutes;
    }

    /**
     * Determines grade based on percentage
     */
    private void determineGrade() {
        if (this.percentageScore == null) {
            return;
        }

        double score = this.percentageScore;
        if (score >= 90) {
            this.grade = "A";
            this.performanceLevel = "Excellent";
        } else if (score >= 80) {
            this.grade = "B";
            this.performanceLevel = "Good";
        } else if (score >= 70) {
            this.grade = "C";
            this.performanceLevel = "Satisfactory";
        } else if (score >= 60) {
            this.grade = "D";
            this.performanceLevel = "Needs Improvement";
        } else {
            this.grade = "F";
            this.performanceLevel = "Fail";
        }
    }

    /**
     * Checks if is overdue
     */
    public boolean isOverdue() {
        return this.dueDate != null &&
               LocalDate.now().isAfter(this.dueDate) &&
               this.status != CompletionStatus.COMPLETED &&
               this.status != CompletionStatus.VERIFIED;
    }

    /**
     * Checks if certificate is valid
     */
    public boolean isCertificateValid() {
        if (this.status != CompletionStatus.VERIFIED) {
            return false;
        }
        if (this.certificateExpiryDate == null) {
            return true;
        }
        return LocalDate.now().isBefore(this.certificateExpiryDate);
    }

    /**
     * Generates completion code
     */
    private static String generateCompletionCode(String courseId, String employeeId) {
        return "CMP-" + courseId.substring(0, 8) + "-" + employeeId.substring(0, 8);
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
