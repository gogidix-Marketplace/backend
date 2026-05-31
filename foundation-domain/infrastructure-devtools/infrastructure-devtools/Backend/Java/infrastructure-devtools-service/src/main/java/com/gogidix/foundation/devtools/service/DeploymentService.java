package com.gogidix.foundation.devtools.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.foundation.devtools.domain.entity.DeploymentJob;
import com.gogidix.foundation.devtools.domain.entity.DeploymentExecution;
import com.gogidix.foundation.devtools.domain.repository.DeploymentJobRepository;
import com.gogidix.foundation.devtools.domain.repository.DeploymentExecutionRepository;
import com.gogidix.foundation.devtools.dto.DeploymentJobDto;
import com.gogidix.foundation.devtools.dto.DeploymentExecutionDto;
import com.gogidix.foundation.devtools.dto.DeploymentRequest;
import com.gogidix.foundation.devtools.dto.DeploymentResult;
import com.gogidix.foundation.devtools.exception.DeploymentException;
import com.gogidix.foundation.devtools.mapper.DeploymentMapper;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

/**
 * Service for deployment functionality.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeploymentService {

    private final DeploymentJobRepository jobRepository;
    private final DeploymentExecutionRepository executionRepository;
    private final DeploymentMapper mapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${devtools.deployment.timeout:300000}")
    private int deploymentTimeout;

    @Value("${devtools.deployment.max-concurrent:5}")
    private int maxConcurrentDeployments;

    private final Map<Long, CompletableFuture<DeploymentResult>> runningDeployments = new HashMap<>();

    /**
     * Create a new deployment job.
     */
    @Transactional
    public DeploymentJobDto createJob(DeploymentJobDto dto) {
        log.info("Creating deployment job: {}", dto.getName());

        DeploymentJob entity = mapper.toEntity(dto);
        entity.setUuid(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        entity = jobRepository.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * Update an existing deployment job.
     */
    @Transactional
    @CacheEvict(value = "deploymentJobs", key = "#uuid")
    public DeploymentJobDto updateJob(UUID uuid, DeploymentJobDto dto) {
        log.info("Updating deployment job: {}", uuid);

        DeploymentJob entity = jobRepository.findByUuid(uuid)
                .orElseThrow(() -> new DeploymentException("Deployment job not found: " + uuid));

        mapper.updateEntityFromDto(dto, entity);
        entity.setUpdatedAt(LocalDateTime.now());

        entity = jobRepository.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * Get a deployment job by UUID.
     */
    @Cacheable(value = "deploymentJobs", key = "#uuid")
    @Transactional(readOnly = true)
    public DeploymentJobDto getJob(UUID uuid) {
        DeploymentJob entity = jobRepository.findByUuid(uuid)
                .orElseThrow(() -> new DeploymentException("Deployment job not found: " + uuid));
        return mapper.toDto(entity);
    }

    /**
     * Get all deployment jobs for a project.
     */
    @Transactional(readOnly = true)
    public Page<DeploymentJobDto> getJobsByProject(String projectId, Pageable pageable) {
        return jobRepository.findByProjectId(projectId, pageable)
                .map(mapper::toDto);
    }

    /**
     * Delete a deployment job.
     */
    @Transactional
    @CacheEvict(value = "deploymentJobs", key = "#uuid")
    public void deleteJob(UUID uuid) {
        log.info("Deleting deployment job: {}", uuid);
        jobRepository.findByUuid(uuid).orElseThrow(() -> new DeploymentException("Deployment job not found: " + uuid)); jobRepository.deleteById(jobRepository.findByUuid(uuid).get().getId());
    }

    /**
     * Execute a deployment job.
     */
    @Async
    @Transactional
    public CompletableFuture<DeploymentResult> executeJob(Long jobId, DeploymentRequest request) {
        DeploymentJob job = jobRepository.findById(jobId)
                .orElseThrow(() -> new DeploymentException("Deployment job not found: " + jobId));

        return executeDeployment(job, request);
    }

    /**
     * Internal method to execute a deployment.
     */
    private CompletableFuture<DeploymentResult> executeDeployment(DeploymentJob job, DeploymentRequest request) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            DeploymentResult.DeploymentResultBuilder resultBuilder = DeploymentResult.builder()
                    .jobUuid(job.getUuid())
                    .jobName(job.getName())
                    .environment(job.getTargetEnvironment());

            DeploymentExecution.DeploymentExecutionBuilder executionBuilder =
                    DeploymentExecution.builder()
                            .uuid(UUID.randomUUID())
                            .jobId(job.getId())
                            .executedBy(request.getExecutedBy())
                            .executedAt(LocalDateTime.now())
                            .deploymentVersion(request.getVersion())
                            .commitSha(request.getCommitSha());

            StringBuilder outputLog = new StringBuilder();
            StringBuilder errorLog = new StringBuilder();

            try {
                log.info("Executing deployment job: {} to environment: {}", job.getName(), job.getTargetEnvironment());

                resultBuilder.status("IN_PROGRESS");
                executionBuilder.status("IN_PROGRESS");
                DeploymentExecution inProgressExecution = executionBuilder.build();
                executionRepository.save(inProgressExecution);

                // Execute pre-deployment script
                if (job.getPreDeploymentScript() != null && !job.getPreDeploymentScript().isBlank()) {
                    outputLog.append("=== Pre-deployment ===\n");
                    executeScript(job.getPreDeploymentScript(), request, outputLog, errorLog);
                }

                // Execute main deployment script
                outputLog.append("\n=== Deployment ===\n");
                executeScript(job.getDeploymentScript(), request, outputLog, errorLog);

                // Execute post-deployment script
                if (job.getPostDeploymentScript() != null && !job.getPostDeploymentScript().isBlank()) {
                    outputLog.append("\n=== Post-deployment ===\n");
                    executeScript(job.getPostDeploymentScript(), request, outputLog, errorLog);
                }

                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;

                resultBuilder.status("SUCCESS")
                        .duration(duration)
                        .outputLog(outputLog.toString());

                executionBuilder.status("SUCCESS")
                        .outputLog(truncateLog(outputLog.toString()))
                        .errorLog(truncateLog(errorLog.toString()))
                        .startTime(startTime)
                        .endTime(endTime)
                        .duration(duration);

            } catch (Exception e) {
                log.error("Deployment execution failed", e);
                long duration = System.currentTimeMillis() - startTime;

                errorLog.append("ERROR: ").append(e.getMessage());

                resultBuilder.status("FAILED")
                        .errorMessage(e.getMessage())
                        .duration(duration)
                        .outputLog(outputLog.toString())
                        .errorLog(errorLog.toString());

                executionBuilder.status("FAILED")
                        .outputLog(truncateLog(outputLog.toString()))
                        .errorLog(truncateLog(errorLog.toString()))
                        .startTime(startTime)
                        .duration(duration);

                // Attempt rollback
                if (job.getRollbackScript() != null && !job.getRollbackScript().isBlank()) {
                    outputLog.append("\n=== Rollback ===\n");
                    try {
                        executeScript(job.getRollbackScript(), request, outputLog, errorLog);
                    } catch (Exception rollbackEx) {
                        log.error("Rollback failed", rollbackEx);
                        errorLog.append("ROLLBACK ERROR: ").append(rollbackEx.getMessage());
                    }
                }
            }

            DeploymentResult result = resultBuilder.build();
            DeploymentExecution execution = executionBuilder.build();

            // Save execution
            executionRepository.save(execution);

            return result;
        });
    }

    /**
     * Get deployment execution history.
     */
    @Transactional(readOnly = true)
    public List<DeploymentExecutionDto> getExecutionHistory(Long jobId, Pageable pageable) {
        return executionRepository.findByJobId(jobId, pageable)
                .stream()
                .map(mapper::toExecutionDto)
                .toList();
    }

    /**
     * Get deployment by UUID.
     */
    @Transactional(readOnly = true)
    public DeploymentExecutionDto getExecution(UUID uuid) {
        DeploymentExecution execution = executionRepository.findByUuid(uuid)
                .orElseThrow(() -> new DeploymentException("Execution not found: " + uuid));
        return mapper.toExecutionDto(execution);
    }

    /**
     * Get deployment statistics for a project.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getDeploymentStatistics(String projectId) {
        List<DeploymentJob> jobs = jobRepository.findByProjectId(projectId);

        long totalJobs = jobs.size();
        long enabledJobs = jobs.stream().filter(DeploymentJob::getEnabled).count();

        Map<String, Long> statusCounts = new HashMap<>();
        Map<String, Long> environmentCounts = new HashMap<>();

        for (DeploymentJob job : jobs) {
            Long successCount = executionRepository.countByJobIdAndStatus(job.getId(), "SUCCESS");
            Long failedCount = executionRepository.countByJobIdAndStatus(job.getId(), "FAILED");

            statusCounts.put("success", statusCounts.getOrDefault("success", 0L) + successCount);
            statusCounts.put("failed", statusCounts.getOrDefault("failed", 0L) + failedCount);

            environmentCounts.put(job.getTargetEnvironment(),
                    environmentCounts.getOrDefault(job.getTargetEnvironment(), 0L) + 1L);
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobs", totalJobs);
        stats.put("enabledJobs", enabledJobs);
        stats.put("executions", statusCounts);
        stats.put("environments", environmentCounts);

        return stats;
    }

    /**
     * Rollback a deployment.
     */
    @Async
    @Transactional
    public CompletableFuture<DeploymentResult> rollbackDeployment(Long executionId, String executedBy) {
        DeploymentExecution originalExecution = executionRepository.findById(executionId)
                .orElseThrow(() -> new DeploymentException("Execution not found: " + executionId));

        DeploymentJob job = jobRepository.findById(originalExecution.getJobId())
                .orElseThrow(() -> new DeploymentException("Job not found: " + originalExecution.getJobId()));

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            StringBuilder outputLog = new StringBuilder();
            StringBuilder errorLog = new StringBuilder();

            try {
                outputLog.append("=== Rolling back deployment ===\n");
                executeScript(job.getRollbackScript(), null, outputLog, errorLog);

                return DeploymentResult.builder()
                        .jobUuid(job.getUuid())
                        .jobName(job.getName())
                        .environment(job.getTargetEnvironment())
                        .status("SUCCESS")
                        .duration(System.currentTimeMillis() - startTime)
                        .outputLog(outputLog.toString())
                        .build();
            } catch (Exception e) {
                log.error("Rollback failed", e);
                return DeploymentResult.builder()
                        .jobUuid(job.getUuid())
                        .jobName(job.getName())
                        .environment(job.getTargetEnvironment())
                        .status("FAILED")
                        .errorMessage(e.getMessage())
                        .duration(System.currentTimeMillis() - startTime)
                        .errorLog(e.getMessage())
                        .build();
            }
        });
    }

    private void executeScript(String script, DeploymentRequest request,
                               StringBuilder outputLog, StringBuilder errorLog) throws Exception {
        if (script == null || script.isBlank()) {
            return;
        }

        // Parse script type
        String scriptType = determineScriptType(script);

        switch (scriptType) {
            case "ssh":
                executeSshScript(script, request, outputLog, errorLog);
                break;
            case "local":
                executeLocalScript(script, request, outputLog, errorLog);
                break;
            case "docker":
                executeDockerScript(script, request, outputLog, errorLog);
                break;
            default:
                throw new DeploymentException("Unknown script type: " + scriptType);
        }
    }

    private String determineScriptType(String script) {
        if (script.startsWith("ssh://") || script.startsWith("SSH://")) {
            return "ssh";
        } else if (script.startsWith("docker://") || script.startsWith("DOCKER://")) {
            return "docker";
        }
        return "local";
    }

    private void executeSshScript(String script, DeploymentRequest request,
                                   StringBuilder outputLog, StringBuilder errorLog) throws Exception {
        // Parse SSH connection details
        // Format: ssh://user:password@host:port/command
        try {
            String cleanScript = script.substring(6);
            String[] parts = cleanScript.split("@");
            String userPass = parts[0];
            String hostPortCmd = parts[1];

            String[] userPassParts = userPass.split(":");
            String user = userPassParts[0];
            String password = userPassParts[1];

            String[] hostPortCmdParts = hostPortCmd.split("/", 2);
            String hostPort = hostPortCmdParts[0];
            String command = hostPortCmdParts.length > 1 ? hostPortCmdParts[1] : "";

            String[] hostPortParts = hostPort.split(":");
            String host = hostPortParts[0];
            int port = hostPortParts.length > 1 ? Integer.parseInt(hostPortParts[1]) : 22;

            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();

            ChannelExec channel = (ChannelExec) session.openChannel("exec");
            channel.setCommand(command);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

            channel.setOutputStream(outputStream);
            channel.setErrStream(errorStream);
            channel.connect();

            while (!channel.isClosed()) {
                Thread.sleep(100);
            }

            outputLog.append(outputStream.toString());
            errorLog.append(errorStream.toString());

            channel.disconnect();
            session.disconnect();

            if (channel.getExitStatus() != 0) {
                throw new DeploymentException("SSH command failed with exit code: " + channel.getExitStatus());
            }
        } catch (Exception e) {
            throw new DeploymentException("SSH execution failed: " + e.getMessage(), e);
        }
    }

    private void executeLocalScript(String script, DeploymentRequest request,
                                     StringBuilder outputLog, StringBuilder errorLog) throws Exception {
        ProcessBuilder pb = new ProcessBuilder();
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            pb.command("cmd.exe", "/c", script);
        } else {
            pb.command("sh", "-c", script);
        }

        Process process = pb.start();

        java.util.Scanner stdout = new java.util.Scanner(process.getInputStream());
        java.util.Scanner stderr = new java.util.Scanner(process.getErrorStream());

        while (stdout.hasNextLine()) {
            outputLog.append(stdout.nextLine()).append("\n");
        }
        while (stderr.hasNextLine()) {
            errorLog.append(stderr.nextLine()).append("\n");
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new DeploymentException("Local script failed with exit code: " + exitCode);
        }
    }

    private void executeDockerScript(String script, DeploymentRequest request,
                                      StringBuilder outputLog, StringBuilder errorLog) throws Exception {
        String cleanScript = script.substring(8);
        String[] parts = cleanScript.split(" ");

        ProcessBuilder pb = new ProcessBuilder("docker");
        for (String part : parts) {
            pb.command().add(part);
        }

        Process process = pb.start();

        java.util.Scanner stdout = new java.util.Scanner(process.getInputStream());
        java.util.Scanner stderr = new java.util.Scanner(process.getErrorStream());

        while (stdout.hasNextLine()) {
            outputLog.append(stdout.nextLine()).append("\n");
        }
        while (stderr.hasNextLine()) {
            errorLog.append(stderr.nextLine()).append("\n");
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new DeploymentException("Docker command failed with exit code: " + exitCode);
        }
    }

    private String truncateLog(String log) {
        if (log == null) {
            return null;
        }
        if (log.length() > 50000) {
            return log.substring(0, 50000) + "\n... [truncated]";
        }
        return log;
    }
}
