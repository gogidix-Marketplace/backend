package com.gogidix.infrastructure.lockservice.interfaces.rest;

import com.gogidix.infrastructure.lockservice.application.dto.*;
import com.gogidix.infrastructure.lockservice.application.service.DistributedLockService;
import com.gogidix.infrastructure.lockservice.application.service.LockCleanupService;
import com.gogidix.infrastructure.lockservice.domain.model.Lock;
import com.gogidix.infrastructure.lockservice.domain.model.LockAcquisitionResult;
import com.gogidix.infrastructure.lockservice.domain.model.LockReleaseResult;
import com.gogidix.infrastructure.lockservice.domain.model.LockRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * REST controller for distributed lock operations.
 * Provides APIs for acquiring, releasing, extending, and monitoring locks.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/locks")
@RequiredArgsConstructor
@Tag(name = "Distributed Locks", description = "APIs for distributed resource locking")
public class LockController {

    private final DistributedLockService lockService;
    private final LockCleanupService cleanupService;

    private static final String DEFAULT_TENANT_HEADER = "X-Tenant-ID";

    /**
     * Acquire a distributed lock.
     *
     * @param request    the lock request
     * @param tenantId   the tenant ID from header
     * @return acquisition response
     */
    @PostMapping("/acquire")
    @Operation(summary = "Acquire a lock", description = "Attempt to acquire a distributed lock on a resource")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lock acquisition processed"),
        @ApiResponse(responseCode = "400", description = "Invalid request"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LockAcquisitionResponseDto> acquireLock(
        @Parameter(description = "Lock acquisition request")
        @Valid @RequestBody LockRequestDto request,

        @Parameter(description = "Tenant ID header (overrides request tenantId)")
        @RequestHeader(value = DEFAULT_TENANT_HEADER, required = false) String tenantId) {

        // Use header tenant ID if provided, otherwise use request tenant ID
        if (tenantId != null && !tenantId.isEmpty()) {
            request.setTenantId(tenantId);
        }

        log.debug("Lock acquire request for resource: {} in tenant: {}",
            request.getResourceKey(), request.getTenantId());

        LockRequest lockRequest = mapToLockRequest(request);
        LockAcquisitionResult result;

        if (Boolean.TRUE.equals(request.getWaitForLock())) {
            result = lockService.acquireLockWithWait(lockRequest);
        } else {
            result = lockService.acquireLock(lockRequest);
        }

        LockAcquisitionResponseDto response = mapToAcquisitionResponse(result);
        HttpStatus status = result.isAcquired() ? HttpStatus.OK : HttpStatus.CONFLICT;

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Release a distributed lock.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @param holderId    the holder ID
     * @param lockId      the lock ID
     * @return release response
     */
    @DeleteMapping("/{resourceKey}/release")
    @Operation(summary = "Release a lock", description = "Release a previously acquired lock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lock released successfully"),
        @ApiResponse(responseCode = "404", description = "Lock not found"),
        @ApiResponse(responseCode = "403", description = "Lock held by different holder"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LockReleaseResponseDto> releaseLock(
        @Parameter(description = "Tenant ID", required = true)
        @RequestHeader(DEFAULT_TENANT_HEADER) String tenantId,

        @Parameter(description = "Resource key", required = true)
        @PathVariable String resourceKey,

        @Parameter(description = "Holder ID", required = true)
        @RequestParam String holderId,

        @Parameter(description = "Lock ID", required = true)
        @RequestParam UUID lockId) {

        log.debug("Lock release request for resource: {} by holder: {}", resourceKey, holderId);

        LockReleaseResult result = lockService.releaseLock(tenantId, resourceKey, holderId, lockId.toString());
        LockReleaseResponseDto response = mapToReleaseResponse(result);

        HttpStatus status = result.isSuccess() ? HttpStatus.OK :
            (result.getErrorMessage() != null && result.getErrorMessage().contains("not found")
                ? HttpStatus.NOT_FOUND : HttpStatus.FORBIDDEN);

        return ResponseEntity.status(status).body(response);
    }

    /**
     * Extend a lock's TTL.
     *
     * @param tenantId           the tenant ID
     * @param resourceKey        the resource key
     * @param holderId           the holder ID
     * @param lockId             the lock ID
     * @param additionalTtlSeconds additional TTL
     * @return success status
     */
    @PutMapping("/{resourceKey}/extend")
    @Operation(summary = "Extend lock TTL", description = "Extend the time-to-live of an existing lock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lock extended successfully"),
        @ApiResponse(responseCode = "404", description = "Lock not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> extendLock(
        @Parameter(description = "Tenant ID", required = true)
        @RequestHeader(DEFAULT_TENANT_HEADER) String tenantId,

        @Parameter(description = "Resource key", required = true)
        @PathVariable String resourceKey,

        @Parameter(description = "Holder ID", required = true)
        @RequestParam String holderId,

        @Parameter(description = "Lock ID", required = true)
        @RequestParam UUID lockId,

        @Parameter(description = "Additional TTL in seconds", required = true)
        @RequestParam Long additionalTtlSeconds) {

        log.debug("Lock extend request for resource: {} by {} seconds", resourceKey, additionalTtlSeconds);

        boolean extended = lockService.extendLock(tenantId, resourceKey, holderId, lockId.toString(), additionalTtlSeconds);

        return ResponseEntity.ok(Map.of(
            "extended", extended,
            "message", extended ? "Lock extended successfully" : "Failed to extend lock"
        ));
    }

    /**
     * Check if a resource is locked.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @return lock status
     */
    @GetMapping("/{resourceKey}/status")
    @Operation(summary = "Check lock status", description = "Check if a resource is currently locked")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lock status retrieved"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> checkLockStatus(
        @Parameter(description = "Tenant ID", required = true)
        @RequestHeader(DEFAULT_TENANT_HEADER) String tenantId,

        @Parameter(description = "Resource key", required = true)
        @PathVariable String resourceKey) {

        boolean locked = lockService.isLocked(tenantId, resourceKey);

        return ResponseEntity.ok(Map.of(
            "locked", locked,
            "resourceKey", resourceKey,
            "tenantId", tenantId
        ));
    }

    /**
     * Get lock details for a resource.
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @return lock details
     */
    @GetMapping("/{resourceKey}")
    @Operation(summary = "Get lock details", description = "Get detailed information about a lock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lock details retrieved"),
        @ApiResponse(responseCode = "404", description = "Lock not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LockResponseDto> getLock(
        @Parameter(description = "Tenant ID", required = true)
        @RequestHeader(DEFAULT_TENANT_HEADER) String tenantId,

        @Parameter(description = "Resource key", required = true)
        @PathVariable String resourceKey) {

        return lockService.getLock(tenantId, resourceKey)
            .map(lock -> ResponseEntity.ok(LockResponseDto.fromDomain(lock)))
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Get all active locks for a tenant.
     *
     * @param tenantId the tenant ID
     * @return list of active locks
     */
    @GetMapping
    @Operation(summary = "Get all active locks", description = "Retrieve all active locks for a tenant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locks retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LockResponseDto>> getActiveLocks(
        @Parameter(description = "Tenant ID", required = true)
        @RequestHeader(DEFAULT_TENANT_HEADER) String tenantId) {

        List<Lock> locks = lockService.getActiveLocks(tenantId);
        List<LockResponseDto> response = locks.stream()
            .map(LockResponseDto::fromDomain)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Get locks held by a specific holder.
     *
     * @param tenantId the tenant ID
     * @param holderId the holder ID
     * @return list of locks
     */
    @GetMapping("/by-holder/{holderId}")
    @Operation(summary = "Get locks by holder", description = "Get all locks held by a specific holder")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Locks retrieved successfully"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<LockResponseDto>> getLocksByHolder(
        @Parameter(description = "Tenant ID", required = true)
        @RequestHeader(DEFAULT_TENANT_HEADER) String tenantId,

        @Parameter(description = "Holder ID", required = true)
        @PathVariable String holderId) {

        List<Lock> locks = lockService.getLocksByHolder(tenantId, holderId);
        List<LockResponseDto> response = locks.stream()
            .map(LockResponseDto::fromDomain)
            .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * Force unlock a resource (admin operation).
     *
     * @param tenantId    the tenant ID
     * @param resourceKey the resource key
     * @return success status
     */
    @DeleteMapping("/{resourceKey}/force-unlock")
    @Operation(summary = "Force unlock", description = "Forcefully release a lock (admin operation)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resource unlocked"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> forceUnlock(
        @Parameter(description = "Tenant ID", required = true)
        @RequestHeader(DEFAULT_TENANT_HEADER) String tenantId,

        @Parameter(description = "Resource key", required = true)
        @PathVariable String resourceKey) {

        log.warn("Force unlock requested for resource: {} in tenant: {}", resourceKey, tenantId);

        boolean unlocked = lockService.forceUnlock(tenantId, resourceKey);

        return ResponseEntity.ok(Map.of(
            "unlocked", unlocked,
            "resourceKey", resourceKey,
            "tenantId", tenantId
        ));
    }

    /**
     * Get lock statistics for a tenant.
     *
     * @param tenantId the tenant ID
     * @return lock statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get lock statistics", description = "Get lock statistics for a tenant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LockStatisticsResponseDto> getStatistics(
        @Parameter(description = "Tenant ID", required = true)
        @RequestHeader(DEFAULT_TENANT_HEADER) String tenantId) {

        return ResponseEntity.ok(
            LockStatisticsResponseDto.fromDomain(lockService.getStatistics(tenantId))
        );
    }

    /**
     * Get global lock statistics.
     *
     * @return global statistics
     */
    @GetMapping("/statistics/global")
    @Operation(summary = "Get global statistics", description = "Get aggregated lock statistics across all tenants")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Global statistics retrieved"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<LockStatisticsResponseDto> getGlobalStatistics() {
        return ResponseEntity.ok(
            LockStatisticsResponseDto.fromDomain(lockService.getGlobalStatistics())
        );
    }

    /**
     * Get active lock counts by tenant.
     *
     * @return tenant lock counts
     */
    @GetMapping("/statistics/by-tenant")
    @Operation(summary = "Get lock counts by tenant", description = "Get active lock counts for each tenant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tenant counts retrieved"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Long>> getActiveLockCountsByTenant() {
        return ResponseEntity.ok(lockService.getActiveLockCountsByTenant());
    }

    /**
     * Trigger cleanup of expired locks.
     *
     * @param tenantId the tenant ID (optional, if null cleans all)
     * @return cleanup result
     */
    @PostMapping("/cleanup")
    @Operation(summary = "Cleanup expired locks", description = "Trigger cleanup of expired locks")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cleanup completed"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Object>> cleanupExpiredLocks(
        @Parameter(description = "Tenant ID (optional)")
        @RequestParam(required = false) String tenantId) {

        long cleanedUp = tenantId != null
            ? cleanupService.cleanupTenantLocks(tenantId)
            : lockService.cleanupExpiredLocks(null);

        return ResponseEntity.ok(Map.of(
            "cleanedUp", cleanedUp,
            "tenantId", tenantId != null ? tenantId : "all"
        ));
    }

    // Helper methods

    private LockRequest mapToLockRequest(LockRequestDto dto) {
        return LockRequest.builder()
            .tenantId(dto.getTenantId())
            .resourceKey(dto.getResourceKey())
            .holderId(dto.getHolderId())
            .holderName(dto.getHolderName())
            .lockType(dto.getLockType() != null ? dto.getLockType() : com.gogidix.infrastructure.lockservice.domain.model.LockType.EXCLUSIVE)
            .ttlSeconds(dto.getTtlSeconds())
            .waitTimeSeconds(dto.getWaitTimeSeconds())
            .maxRetries(dto.getMaxRetries())
            .retryIntervalMs(dto.getRetryIntervalMs())
            .metadata(dto.getMetadata())
            .build();
    }

    private LockAcquisitionResponseDto mapToAcquisitionResponse(LockAcquisitionResult result) {
        return LockAcquisitionResponseDto.builder()
            .acquired(result.isAcquired())
            .lock(result.getLock() != null ? LockResponseDto.fromDomain(result.getLock()) : null)
            .errorMessage(result.getErrorMessage())
            .attemptedAt(result.getAttemptedAt())
            .acquiredAt(result.getAcquiredAt())
            .retryCount(result.getRetryCount())
            .timedOut(result.isTimedOut())
            .build();
    }

    private LockReleaseResponseDto mapToReleaseResponse(LockReleaseResult result) {
        return LockReleaseResponseDto.builder()
            .success(result.isSuccess())
            .releasedLock(result.getReleasedLock() != null ? LockResponseDto.fromDomain(result.getReleasedLock()) : null)
            .errorMessage(result.getErrorMessage())
            .releasedAt(result.getReleasedAt())
            .wasExpired(result.isWasExpired())
            .build();
    }
}
