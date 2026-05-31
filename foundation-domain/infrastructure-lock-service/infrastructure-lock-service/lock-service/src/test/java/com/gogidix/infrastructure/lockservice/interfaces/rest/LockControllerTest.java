package com.gogidix.infrastructure.lockservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.infrastructure.lockservice.application.dto.*;
import com.gogidix.infrastructure.lockservice.application.service.DistributedLockService;
import com.gogidix.infrastructure.lockservice.application.service.LockCleanupService;
import com.gogidix.infrastructure.lockservice.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LockController Tests")
class LockControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private DistributedLockService lockService;

    @Mock
    private LockCleanupService cleanupService;

    @InjectMocks
    private LockController lockController;

    private LockRequestDto validLockRequest;
    private Lock sampleLock;
    private UUID lockId;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(lockController).build();

        lockId = UUID.randomUUID();

        validLockRequest = LockRequestDto.builder()
            .tenantId("tenant-1")
            .resourceKey("resource-123")
            .holderId("holder-1")
            .holderName("Test Holder")
            .lockType(LockType.EXCLUSIVE)
            .ttlSeconds(300L)
            .waitForLock(false)
            .build();

        sampleLock = Lock.builder("tenant-1", "resource-123", "holder-1")
            .lockId(lockId)
            .status(LockStatus.LOCKED)
            .lockType(LockType.EXCLUSIVE)
            .holderName("Test Holder")
            .acquiredAt(LocalDateTime.now())
            .expiresAt(LocalDateTime.now().plusSeconds(300))
            .build();
    }

    @Test
    @DisplayName("Should acquire lock successfully")
    void shouldAcquireLockSuccessfully() throws Exception {
        LockAcquisitionResult result = LockAcquisitionResult.success(sampleLock, 0);
        when(lockService.acquireLock(any(LockRequest.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/locks/acquire")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Tenant-ID", "tenant-1")
                .content(objectMapper.writeValueAsString(validLockRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.acquired").value(true))
            .andExpect(jsonPath("$.lock.resourceKey").value("resource-123"))
            .andExpect(jsonPath("$.lock.status").value("LOCKED"));
    }

    @Test
    @DisplayName("Should return conflict when lock acquisition fails")
    void shouldReturnConflictWhenLockAcquisitionFails() throws Exception {
        LockAcquisitionResult result = LockAcquisitionResult.failure("Resource already locked");
        when(lockService.acquireLock(any(LockRequest.class))).thenReturn(result);

        mockMvc.perform(post("/api/v1/locks/acquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validLockRequest)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.acquired").value(false))
            .andExpect(jsonPath("$.errorMessage").value("Resource already locked"));
    }

    @Test
    @DisplayName("Should release lock successfully")
    void shouldReleaseLockSuccessfully() throws Exception {
        LockReleaseResult result = LockReleaseResult.success(sampleLock);
        when(lockService.releaseLock(eq("tenant-1"), eq("resource-123"), eq("holder-1"), anyString()))
            .thenReturn(result);

        mockMvc.perform(delete("/api/v1/locks/resource-123/release")
                .header("X-Tenant-ID", "tenant-1")
                .param("holderId", "holder-1")
                .param("lockId", lockId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should return 404 when releasing non-existent lock")
    void shouldReturnNotFoundWhenReleasingNonExistentLock() throws Exception {
        LockReleaseResult result = LockReleaseResult.failure("Lock not found");
        when(lockService.releaseLock(anyString(), anyString(), anyString(), anyString()))
            .thenReturn(result);

        mockMvc.perform(delete("/api/v1/locks/resource-123/release")
                .header("X-Tenant-ID", "tenant-1")
                .param("holderId", "holder-1")
                .param("lockId", lockId.toString()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    @DisplayName("Should extend lock successfully")
    void shouldExtendLockSuccessfully() throws Exception {
        when(lockService.extendLock(eq("tenant-1"), eq("resource-123"), eq("holder-1"), anyString(), eq(300L)))
            .thenReturn(true);

        mockMvc.perform(put("/api/v1/locks/resource-123/extend")
                .header("X-Tenant-ID", "tenant-1")
                .param("holderId", "holder-1")
                .param("lockId", lockId.toString())
                .param("additionalTtlSeconds", "300"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.extended").value(true));
    }

    @Test
    @DisplayName("Should check lock status")
    void shouldCheckLockStatus() throws Exception {
        when(lockService.isLocked("tenant-1", "resource-123")).thenReturn(true);

        mockMvc.perform(get("/api/v1/locks/resource-123/status")
                .header("X-Tenant-ID", "tenant-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.locked").value(true))
            .andExpect(jsonPath("$.resourceKey").value("resource-123"));
    }

    @Test
    @DisplayName("Should get lock details")
    void shouldGetLockDetails() throws Exception {
        when(lockService.getLock("tenant-1", "resource-123")).thenReturn(Optional.of(sampleLock));

        mockMvc.perform(get("/api/v1/locks/resource-123")
                .header("X-Tenant-ID", "tenant-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.resourceKey").value("resource-123"))
            .andExpect(jsonPath("$.holderId").value("holder-1"))
            .andExpect(jsonPath("$.status").value("LOCKED"));
    }

    @Test
    @DisplayName("Should return 404 when lock not found")
    void shouldReturnNotFoundWhenLockNotFound() throws Exception {
        when(lockService.getLock("tenant-1", "resource-123")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/locks/resource-123")
                .header("X-Tenant-ID", "tenant-1"))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should get all active locks for tenant")
    void shouldGetAllActiveLocksForTenant() throws Exception {
        when(lockService.getActiveLocks("tenant-1")).thenReturn(List.of(sampleLock));

        mockMvc.perform(get("/api/v1/locks")
                .header("X-Tenant-ID", "tenant-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].resourceKey").value("resource-123"));
    }

    @Test
    @DisplayName("Should get locks by holder")
    void shouldGetLocksByHolder() throws Exception {
        when(lockService.getLocksByHolder("tenant-1", "holder-1")).thenReturn(List.of(sampleLock));

        mockMvc.perform(get("/api/v1/locks/by-holder/holder-1")
                .header("X-Tenant-ID", "tenant-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].holderId").value("holder-1"));
    }

    @Test
    @DisplayName("Should force unlock resource")
    void shouldForceUnlockResource() throws Exception {
        when(lockService.forceUnlock("tenant-1", "resource-123")).thenReturn(true);

        mockMvc.perform(delete("/api/v1/locks/resource-123/force-unlock")
                .header("X-Tenant-ID", "tenant-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.unlocked").value(true));
    }

    @Test
    @DisplayName("Should get statistics")
    void shouldGetStatistics() throws Exception {
        LockStatistics stats = LockStatistics.builder()
            .tenantId("tenant-1")
            .activeLocks(5L)
            .expiredLocks(2L)
            .releasedLocks(10L)
            .failedAttempts(1L)
            .calculatedAt(LocalDateTime.now())
            .build();
        when(lockService.getStatistics("tenant-1")).thenReturn(stats);

        mockMvc.perform(get("/api/v1/locks/statistics")
                .header("X-Tenant-ID", "tenant-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tenantId").value("tenant-1"))
            .andExpect(jsonPath("$.activeLocks").value(5));
    }

    @Test
    @DisplayName("Should get global statistics")
    void shouldGetGlobalStatistics() throws Exception {
        LockStatistics stats = LockStatistics.builder()
            .tenantId("global")
            .activeLocks(100L)
            .calculatedAt(LocalDateTime.now())
            .build();
        when(lockService.getGlobalStatistics()).thenReturn(stats);

        mockMvc.perform(get("/api/v1/locks/statistics/global"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tenantId").value("global"))
            .andExpect(jsonPath("$.activeLocks").value(100));
    }

    @Test
    @DisplayName("Should trigger cleanup")
    void shouldTriggerCleanup() throws Exception {
        when(cleanupService.cleanupTenantLocks("tenant-1")).thenReturn(5L);

        mockMvc.perform(post("/api/v1/locks/cleanup")
                .param("tenantId", "tenant-1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.cleanedUp").value(5));
    }

    @Test
    @DisplayName("Should validate required fields in lock request")
    void shouldValidateRequiredFieldsInLockRequest() throws Exception {
        LockRequestDto invalidRequest = LockRequestDto.builder()
            .resourceKey("resource-123")
            .holderId("holder-1")
            .build();

        mockMvc.perform(post("/api/v1/locks/acquire")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest());
    }
}
