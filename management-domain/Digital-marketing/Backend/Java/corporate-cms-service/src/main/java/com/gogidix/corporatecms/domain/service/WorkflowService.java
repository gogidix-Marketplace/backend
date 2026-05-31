package com.gogidix.corporatecms.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.dto.WorkflowDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.dto.PageResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.application.mapper.WorkflowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.enums.ContentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.enums.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.enums.WorkflowStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.model.Content;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.model.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.model.Workflow.WorkflowAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.model.Workflow.WorkflowStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.repository.ContentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gogidix.corporatecms.domain.repository.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Service for managing workflow approvals.
 */
@Service
@RequiredArgsConstructor
public class WorkflowService {
    private static final Logger log = LoggerFactory.getLogger(WorkflowService.class);

    private final WorkflowRepository workflowRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final WorkflowMapper workflowMapper;

    @Transactional
    public WorkflowDTO initiateWorkflow(String contentId, String requestedBy, String comment) {
        log.info("Initiating workflow for content: {}", contentId);

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", contentId));

        if (workflowRepository.findByContentId(contentId).isPresent()) {
            throw new DuplicateResourceException("Workflow", "contentId", contentId);
        }

        com.gogidix.corporatecms.domain.model.User user =
                getCurrentUser(requestedBy);

        List<WorkflowStep> steps = createWorkflowSteps(content);

        Workflow workflow = Workflow.builder()
                .contentId(contentId)
                .contentType(content.getType().name())
                .contentTitle(content.getTitle())
                .status(WorkflowStatus.PENDING)
                .requestedBy(requestedBy)
                .requestedByName(user.getFullName())
                .requestComment(comment)
                .steps(steps)
                .actions(new ArrayList<>())
                .currentStepIndex(0)
                .startedAt(LocalDateTime.now())
                .build();

        if (!steps.isEmpty()) {
            WorkflowStep firstStep = steps.get(0);
            firstStep.setStatus(WorkflowStatus.PENDING);
            firstStep.setStartedAt(LocalDateTime.now());
            workflow.setCurrentApproverId(firstStep.getApproverId());
            workflow.setCurrentApproverName(firstStep.getApproverName());
        }

        Workflow savedWorkflow = workflowRepository.save(workflow);

        addAction(savedWorkflow, "INITIATED", requestedBy, user.getFullName(),
                "Workflow initiated", null, WorkflowStatus.PENDING);

        log.info("Workflow initiated with ID: {}", savedWorkflow.getId());
        return workflowMapper.toDto(savedWorkflow);
    }

    public WorkflowDTO getWorkflowByContentId(String contentId) {
        log.info("Fetching workflow for content: {}", contentId);
        Workflow workflow = workflowRepository.findByContentId(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow", "contentId", contentId));
        return workflowMapper.toDto(workflow);
    }

    public PageResponse<WorkflowDTO> getWorkflowsByStatus(WorkflowStatus status, int page, int size) {
        log.info("Fetching workflows with status: {}", status);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Workflow> workflowPage = workflowRepository.findByStatus(status, pageable);
        return PageResponse.of(workflowPage.map(workflowMapper::toDto));
    }

    public List<WorkflowDTO> getPendingApprovalsForUser(String userId) {
        log.info("Fetching pending approvals for user: {}", userId);
        List<Workflow> workflows = workflowRepository.findByCurrentApproverIdAndStatus(
                userId, WorkflowStatus.PENDING);
        return workflowMapper.toDtoList(workflows);
    }

    @Transactional
    public WorkflowDTO approveWorkflow(String workflowId, String userId, String comment) {
        log.info("Approving workflow: {} by user: {}", workflowId, userId);

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow", "id", workflowId));

        if (!workflow.canApprove(userId)) {
            throw new UnauthorizedException("You are not authorized to approve this workflow");
        }

        WorkflowStep currentStep = workflow.getCurrentStep();
        if (currentStep == null) {
            throw new InvalidWorkflowTransitionException("No current step found");
        }

        currentStep.setStatus(WorkflowStatus.APPROVED);
        currentStep.setCompletedAt(LocalDateTime.now());
        currentStep.setComment(comment);

        com.gogidix.corporatecms.domain.model.User user = getCurrentUser(userId);

        addAction(workflow, "APPROVE", userId, user.getFullName(), comment,
                workflow.getStatus(), WorkflowStatus.IN_PROGRESS);

        Integer nextStepIndex = workflow.getCurrentStepIndex() + 1;

        if (nextStepIndex >= workflow.getSteps().size()) {
            completeWorkflow(workflow, userId, true, comment);
        } else {
            advanceToNextStep(workflow, nextStepIndex);
        }

        Workflow savedWorkflow = workflowRepository.save(workflow);
        log.info("Workflow approved: {}", workflowId);

        return workflowMapper.toDto(savedWorkflow);
    }

    @Transactional
    public WorkflowDTO rejectWorkflow(String workflowId, String userId, String comment) {
        log.info("Rejecting workflow: {} by user: {}", workflowId, userId);

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow", "id", workflowId));

        if (!workflow.canApprove(userId)) {
            throw new UnauthorizedException("You are not authorized to reject this workflow");
        }

        WorkflowStep currentStep = workflow.getCurrentStep();
        if (currentStep == null) {
            throw new InvalidWorkflowTransitionException("No current step found");
        }

        currentStep.setStatus(WorkflowStatus.REJECTED);
        currentStep.setCompletedAt(LocalDateTime.now());
        currentStep.setComment(comment);

        com.gogidix.corporatecms.domain.model.User user = getCurrentUser(userId);

        addAction(workflow, "REJECT", userId, user.getFullName(), comment,
                workflow.getStatus(), WorkflowStatus.REJECTED);

        workflow.setStatus(WorkflowStatus.REJECTED);
        workflow.setCompletedAt(LocalDateTime.now());
        workflow.setCurrentApproverId(null);
        workflow.setCurrentApproverName(null);

        updateContentStatus(workflow.getContentId(), ContentStatus.REJECTED);

        Workflow savedWorkflow = workflowRepository.save(workflow);
        log.info("Workflow rejected: {}", workflowId);

        return workflowMapper.toDto(savedWorkflow);
    }

    @Transactional
    public WorkflowDTO cancelWorkflow(String workflowId, String userId, String comment) {
        log.info("Cancelling workflow: {} by user: {}", workflowId, userId);

        Workflow workflow = workflowRepository.findById(workflowId)
                .orElseThrow(() -> new ResourceNotFoundException("Workflow", "id", workflowId));

        if (!workflow.getRequestedBy().equals(userId) && !isAdmin(userId)) {
            throw new UnauthorizedException("Only the requester or admin can cancel the workflow");
        }

        workflow.setStatus(WorkflowStatus.CANCELLED);
        workflow.setCompletedAt(LocalDateTime.now());

        com.gogidix.corporatecms.domain.model.User user = getCurrentUser(userId);

        addAction(workflow, "CANCEL", userId, user.getFullName(), comment,
                workflow.getStatus(), WorkflowStatus.CANCELLED);

        Workflow savedWorkflow = workflowRepository.save(workflow);
        log.info("Workflow cancelled: {}", workflowId);

        return workflowMapper.toDto(savedWorkflow);
    }

    public List<WorkflowDTO> getOverdueWorkflows() {
        List<Workflow> workflows = workflowRepository.findOverdueWorkflows(LocalDateTime.now());
        return workflowMapper.toDtoList(workflows);
    }

    private List<WorkflowStep> createWorkflowSteps(Content content) {
        List<WorkflowStep> steps = new ArrayList<>();

        com.gogidix.corporatecms.domain.model.User contentCreator =
                userRepository.findById(content.getAuthorId()).orElse(null);

        String department = contentCreator != null ? contentCreator.getDepartment() : null;

        WorkflowStep step1 = WorkflowStep.builder()
                .stepNumber(1)
                .stepName("Content Review")
                .approverRole("CONTENT_EDITOR")
                .status(WorkflowStatus.PENDING)
                .build();

        WorkflowStep step2 = WorkflowStep.builder()
                .stepNumber(2)
                .stepName("Final Approval")
                .approverRole("ADMIN")
                .status(WorkflowStatus.PENDING)
                .build();

        steps.add(step1);
        steps.add(step2);

        return steps;
    }

    private void advanceToNextStep(Workflow workflow, Integer nextStepIndex) {
        WorkflowStep nextStep = workflow.getSteps().get(nextStepIndex);
        nextStep.setStatus(WorkflowStatus.PENDING);
        nextStep.setStartedAt(LocalDateTime.now());

        workflow.setCurrentStepIndex(nextStepIndex);
        workflow.setCurrentApproverId(nextStep.getApproverId());
        workflow.setCurrentApproverName(nextStep.getApproverName());
    }

    private void completeWorkflow(Workflow workflow, String userId, boolean approved, String comment) {
        workflow.setStatus(approved ? WorkflowStatus.APPROVED : WorkflowStatus.REJECTED);
        workflow.setCompletedAt(LocalDateTime.now());
        workflow.setCurrentStepIndex(workflow.getSteps().size() - 1);
        workflow.setCurrentApproverId(null);
        workflow.setCurrentApproverName(null);

        addAction(workflow, approved ? "COMPLETE" : "REJECT", userId,
                getCurrentUser(userId).getFullName(), comment,
                workflow.getStatus(), approved ? WorkflowStatus.APPROVED : WorkflowStatus.REJECTED);

        if (approved) {
            updateContentStatus(workflow.getContentId(), ContentStatus.APPROVED);
        } else {
            updateContentStatus(workflow.getContentId(), ContentStatus.REJECTED);
        }
    }

    private void updateContentStatus(String contentId, ContentStatus status) {
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new ResourceNotFoundException("Content", "id", contentId));
        content.setStatus(status);
        contentRepository.save(content);
    }

    private void addAction(Workflow workflow, String actionType, String actorId,
                           String actorName, String comment, WorkflowStatus fromStatus,
                           WorkflowStatus toStatus) {
        WorkflowAction action = WorkflowAction.builder()
                .actionType(actionType)
                .actorId(actorId)
                .actorName(actorName)
                .comment(comment)
                .timestamp(LocalDateTime.now())
                .fromStatus(fromStatus)
                .toStatus(toStatus)
                .build();

        workflow.addAction(action);
    }

    private com.gogidix.corporatecms.domain.model.User getCurrentUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    private boolean isAdmin(String userId) {
        return userRepository.findById(userId)
                .map(user -> user.getRole() == UserRole.ADMIN)
                .orElse(false);
    }
}
