package com.gogidix.shared.warehousing.access.application.service;

import com.gogidix.shared.warehousing.access.application.dto.*;
import com.gogidix.shared.warehousing.access.domain.entity.AccessLog;
import com.gogidix.shared.warehousing.access.domain.entity.AccessPermission;
import com.gogidix.shared.warehousing.access.domain.entity.AccessRequest;
import com.gogidix.shared.warehousing.access.domain.exception.AccessDeniedException;
import com.gogidix.shared.warehousing.access.domain.exception.AccessNotFoundException;
import com.gogidix.shared.warehousing.access.domain.repository.AccessLogRepository;
import com.gogidix.shared.warehousing.access.domain.repository.AccessPermissionRepository;
import com.gogidix.shared.warehousing.access.domain.repository.AccessRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Access Service
 *
 * Handles storage access control and management
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccessService {

    private final AccessRequestRepository accessRequestRepository;
    private final AccessLogRepository accessLogRepository;
    private final AccessPermissionRepository accessPermissionRepository;

    @Autowired(required = false)
    private MongoTemplate mongoTemplate;

    /**
     * Request access to storage area
     */
    @Transactional
    public AccessRequestDTO requestAccess(RequestAccessCommand command) {
        log.info("Access request for warehouse: {} by user: {}", command.getWarehouseId(), command.getRequestedBy());

        String tenantId = "current-tenant"; // From context in real implementation

        // Check if user has permission
        if (!checkPermission(tenantId, command.getWarehouseId(), command.getZoneId(),
                command.getRequestedBy(), command.getAccessType())) {
            throw new AccessDeniedException("User does not have permission for this access type");
        }

        AccessRequest request = AccessRequest.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .zoneId(command.getZoneId())
            .requestId(generateRequestId())
            .accessType(command.getAccessType())
            .purpose(command.getPurpose())
            .requestedBy(command.getRequestedBy())
            .requestedFor(command.getRequestedFor())
            .requestedStartTime(command.getRequestedStartTime() != null ?
                command.getRequestedStartTime() : LocalDateTime.now())
            .requestedEndTime(command.getRequestedEndTime() != null ?
                command.getRequestedEndTime() : LocalDateTime.now().plusMinutes(
                    command.getEstimatedDurationMinutes() != null ? command.getEstimatedDurationMinutes() : 60))
            .estimatedDurationMinutes(command.getEstimatedDurationMinutes())
            .status(AccessRequest.RequestStatus.PENDING)
            .notes(command.getNotes())
            .referenceType(command.getReferenceType())
            .referenceId(command.getReferenceId())
            .build();

        // Auto-approve if permission allows
        if (!requiresApproval(tenantId, command.getWarehouseId(), command.getRequestedBy())) {
            request.approve("SYSTEM");
        }

        AccessRequest saved = accessRequestRepository.save(request);
        return toDTO(saved);
    }

    /**
     * Grant access (approve request)
     */
    @Transactional
    public AccessRequestDTO grantAccess(String requestId, GrantAccessCommand command) {
        log.info("Granting access for request: {}", requestId);

        AccessRequest request = accessRequestRepository.findById(requestId)
            .orElseThrow(() -> new AccessNotFoundException("Access request not found: " + requestId));

        request.approve(command.getApprovedBy());
        if (command.getNotes() != null) {
            request.setNotes(command.getNotes());
        }

        AccessRequest saved = accessRequestRepository.save(request);
        return toDTO(saved);
    }

    /**
     * Deny access request
     */
    @Transactional
    public AccessRequestDTO denyAccess(String requestId, String reason, String deniedBy) {
        log.info("Denying access for request: {}", requestId);

        AccessRequest request = accessRequestRepository.findById(requestId)
            .orElseThrow(() -> new AccessNotFoundException("Access request not found: " + requestId));

        request.deny(reason);

        AccessRequest saved = accessRequestRepository.save(request);
        return toDTO(saved);
    }

    /**
     * Log access event
     */
    @Transactional
    public AccessLogDTO logAccess(LogAccessCommand command) {
        log.info("Logging access for warehouse: {} by user: {}", command.getWarehouseId(), command.getUserId());

        String tenantId = "current-tenant";

        AccessLog log = AccessLog.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .zoneId(command.getZoneId())
            .accessRequestId(command.getAccessRequestId())
            .userId(command.getUserId())
            .userName(command.getUserName())
            .userType(command.getUserType())
            .accessType(command.getAccessType())
            .accessedAt(LocalDateTime.now())
            .result(command.getResult())
            .failureReason(command.getFailureReason())
            .badgeId(command.getBadgeId())
            .gateId(command.getGateId())
            .metadata(command.getMetadata())
            .referenceType(command.getReferenceType())
            .referenceId(command.getReferenceId())
            .build();

        AccessLog saved = accessLogRepository.save(log);

        // If access was granted, update request status
        if (command.getAccessRequestId() != null && command.getResult() == AccessLog.AccessResult.SUCCESS) {
            accessRequestRepository.findById(command.getAccessRequestId()).ifPresent(request -> {
                if (command.getAccessType() == AccessLog.AccessType.ENTRY) {
                    request.start();
                } else if (command.getAccessType() == AccessLog.AccessType.EXIT) {
                    request.complete();
                }
                accessRequestRepository.save(request);
            });
        }

        return toDTO(saved);
    }

    /**
     * Complete access (log exit)
     */
    @Transactional
    public AccessLogDTO completeAccess(String logId) {
        log.info("Completing access log: {}", logId);

        AccessLog log = accessLogRepository.findById(logId)
            .orElseThrow(() -> new AccessNotFoundException("Access log not found: " + logId));

        log.complete();
        AccessLog saved = accessLogRepository.save(log);

        return toDTO(saved);
    }

    /**
     * Get access request by ID
     */
    @Transactional(readOnly = true)
    public AccessRequestDTO getAccessRequest(String requestId) {
        AccessRequest request = accessRequestRepository.findById(requestId)
            .orElseThrow(() -> new AccessNotFoundException("Access request not found: " + requestId));
        return toDTO(request);
    }

    /**
     * Get access logs for warehouse
     */
    @Transactional(readOnly = true)
    public List<AccessLogDTO> getAccessLogs(String warehouseId, LocalDateTime from, LocalDateTime to) {
        String tenantId = "current-tenant";

        if (from != null && to != null) {
            return accessLogRepository
                .findByTenantIdAndWarehouseIdAndAccessedAtBetweenOrderByAccessedAtDesc(
                    tenantId, warehouseId, from, to)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        }

        return accessLogRepository.findByTenantIdAndWarehouseIdOrderByAccessedAtDesc(tenantId, warehouseId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get access logs by user
     */
    @Transactional(readOnly = true)
    public List<AccessLogDTO> getUserAccessLogs(String userId) {
        String tenantId = "current-tenant";
        return accessLogRepository.findByTenantIdAndUserIdOrderByAccessedAtDesc(tenantId, userId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get pending requests for warehouse
     */
    @Transactional(readOnly = true)
    public List<AccessRequestDTO> getPendingRequests(String warehouseId) {
        String tenantId = "current-tenant";
        return accessRequestRepository
            .findByTenantIdAndWarehouseIdAndStatusOrderByCreatedAtAsc(
                tenantId, warehouseId, AccessRequest.RequestStatus.PENDING)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Create access permission
     */
    @Transactional
    public AccessPermissionDTO createPermission(CreatePermissionCommand command) {
        log.info("Creating access permission for user: {}", command.getUserId());

        String tenantId = "current-tenant";

        AccessPermission permission = AccessPermission.builder()
            .tenantId(tenantId)
            .warehouseId(command.getWarehouseId())
            .userId(command.getUserId())
            .userName(command.getUserName())
            .role(command.getRole())
            .permissionType(command.getPermissionType())
            .allowedZones(command.getAllowedZones())
            .deniedZones(command.getDeniedZones())
            .allowedAccessTypes(command.getAllowedAccessTypes())
            .timeRestriction(command.getTimeRestriction())
            .requiresApproval(command.getRequiresApproval())
            .approverRole(command.getApproverRole())
            .validFrom(command.getValidFrom())
            .validUntil(command.getValidUntil())
            .active(true)
            .notes(command.getNotes())
            .build();

        AccessPermission saved = accessPermissionRepository.save(permission);
        return toDTO(saved);
    }

    /**
     * Get permissions for user
     */
    @Transactional(readOnly = true)
    public List<AccessPermissionDTO> getUserPermissions(String userId) {
        String tenantId = "current-tenant";
        return accessPermissionRepository.findByTenantIdAndUserId(tenantId, userId)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Cancel access request
     */
    @Transactional
    public AccessRequestDTO cancelRequest(String requestId) {
        log.info("Cancelling access request: {}", requestId);

        AccessRequest request = accessRequestRepository.findById(requestId)
            .orElseThrow(() -> new AccessNotFoundException("Access request not found: " + requestId));

        request.cancel();
        AccessRequest saved = accessRequestRepository.save(request);

        return toDTO(saved);
    }

    private boolean checkPermission(String tenantId, String warehouseId, String zoneId,
                                   String userId, AccessRequest.AccessType accessType) {
        List<AccessPermission> permissions = accessPermissionRepository
            .findActivePermissionsForUser(tenantId, userId, LocalDateTime.now());

        for (AccessPermission permission : permissions) {
            if (permission.getWarehouseId().equals(warehouseId) ||
                "*".equals(permission.getWarehouseId())) {

                if (permission.hasZoneAccess(zoneId != null ? zoneId : "*") &&
                    permission.isAccessTypeAllowed(convertAccessType(accessType))) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean requiresApproval(String tenantId, String warehouseId, String userId) {
        List<AccessPermission> permissions = accessPermissionRepository
            .findActivePermissionsForUser(tenantId, userId, LocalDateTime.now());

        for (AccessPermission permission : permissions) {
            if (permission.getWarehouseId().equals(warehouseId)) {
                return Boolean.TRUE.equals(permission.getRequiresApproval());
            }
        }

        return true; // Default to requiring approval
    }

    private AccessPermission.AccessType convertAccessType(AccessRequest.AccessType accessType) {
        try {
            return AccessPermission.AccessType.valueOf(accessType.name());
        } catch (IllegalArgumentException e) {
            return AccessPermission.AccessType.ENTRY;
        }
    }

    private String generateRequestId() {
        return "AR-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private AccessRequestDTO toDTO(AccessRequest request) {
        return AccessRequestDTO.builder()
            .id(request.getId())
            .tenantId(request.getTenantId())
            .warehouseId(request.getWarehouseId())
            .zoneId(request.getZoneId())
            .requestId(request.getRequestId())
            .accessType(request.getAccessType())
            .purpose(request.getPurpose())
            .requestedBy(request.getRequestedBy())
            .requestedFor(request.getRequestedFor())
            .requestedStartTime(request.getRequestedStartTime())
            .requestedEndTime(request.getRequestedEndTime())
            .estimatedDurationMinutes(request.getEstimatedDurationMinutes())
            .status(request.getStatus())
            .approvedBy(request.getApprovedBy())
            .approvedAt(request.getApprovedAt())
            .rejectionReason(request.getRejectionReason())
            .completedAt(request.getCompletedAt())
            .notes(request.getNotes())
            .referenceType(request.getReferenceType())
            .referenceId(request.getReferenceId())
            .createdAt(request.getCreatedAt())
            .updatedAt(request.getUpdatedAt())
            .build();
    }

    private AccessLogDTO toDTO(AccessLog log) {
        return AccessLogDTO.builder()
            .id(log.getId())
            .tenantId(log.getTenantId())
            .warehouseId(log.getWarehouseId())
            .zoneId(log.getZoneId())
            .accessRequestId(log.getAccessRequestId())
            .userId(log.getUserId())
            .userName(log.getUserName())
            .userType(log.getUserType())
            .accessType(log.getAccessType())
            .accessedAt(log.getAccessedAt())
            .exitAt(log.getExitAt())
            .durationSeconds(log.getDurationSeconds())
            .result(log.getResult())
            .failureReason(log.getFailureReason())
            .badgeId(log.getBadgeId())
            .gateId(log.getGateId())
            .metadata(log.getMetadata())
            .referenceType(log.getReferenceType())
            .referenceId(log.getReferenceId())
            .createdAt(log.getCreatedAt())
            .build();
    }

    private AccessPermissionDTO toDTO(AccessPermission permission) {
        return AccessPermissionDTO.builder()
            .id(permission.getId())
            .tenantId(permission.getTenantId())
            .warehouseId(permission.getWarehouseId())
            .userId(permission.getUserId())
            .userName(permission.getUserName())
            .role(permission.getRole())
            .permissionType(permission.getPermissionType())
            .allowedZones(permission.getAllowedZones())
            .deniedZones(permission.getDeniedZones())
            .allowedAccessTypes(permission.getAllowedAccessTypes())
            .timeRestriction(permission.getTimeRestriction())
            .requiresApproval(permission.getRequiresApproval())
            .approverRole(permission.getApproverRole())
            .validFrom(permission.getValidFrom())
            .validUntil(permission.getValidUntil())
            .active(permission.getActive())
            .notes(permission.getNotes())
            .createdAt(permission.getCreatedAt())
            .updatedAt(permission.getUpdatedAt())
            .build();
    }

    /**
     * Command to create access permission
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class CreatePermissionCommand {
        private String warehouseId;
        private String userId;
        private String userName;
        private String role;
        private AccessPermission.PermissionType permissionType;
        private java.util.List<String> allowedZones;
        private java.util.List<String> deniedZones;
        private java.util.List<AccessPermission.AccessType> allowedAccessTypes;
        private AccessPermission.TimeRestriction timeRestriction;
        private Boolean requiresApproval;
        private String approverRole;
        private LocalDateTime validFrom;
        private LocalDateTime validUntil;
        private String notes;
    }
}
