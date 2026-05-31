package com.gogidix.foundation.devtools.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.foundation.devtools.domain.entity.DevToolLogEntry;
import com.gogidix.foundation.devtools.domain.repository.DevToolLogEntryRepository;
import com.gogidix.foundation.devtools.dto.LogEntryDto;
import com.gogidix.foundation.devtools.dto.LogQuery;
import com.gogidix.foundation.devtools.dto.LogStatistics;
import com.gogidix.foundation.devtools.mapper.LogEntryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Service for logging and debugging functionality.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LoggingService {

    private final DevToolLogEntryRepository logEntryRepository;
    private final LogEntryMapper mapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${devtools.logging.retention-days:7}")
    private int retentionDays;

    @Value("${devtools.logging.max-log-size-mb:500}")
    private int maxLogSizeMb;

    private final Map<String, LinkedBlockingQueue<DevToolLogEntry>> sessionLogs = new ConcurrentHashMap<>();
    private static final int MAX_SESSION_LOGS = 1000;

    /**
     * Create a log entry.
     */
    @Transactional
    public LogEntryDto createLogEntry(LogEntryDto dto) {
        DevToolLogEntry entity = mapper.toEntity(dto);
        entity.setUuid(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());

        entity = logEntryRepository.save(entity);

        // Also add to session cache for real-time access
        if (entity.getSessionId() != null) {
            sessionLogs.computeIfAbsent(entity.getSessionId(), k -> new LinkedBlockingQueue<>(MAX_SESSION_LOGS))
                    .offer(entity);
        }

        return mapper.toDto(entity);
    }

    /**
     * Batch create log entries.
     */
    @Transactional
    public List<LogEntryDto> createLogEntries(List<LogEntryDto> entries) {
        List<DevToolLogEntry> entities = entries.stream()
                .map(dto -> {
                    DevToolLogEntry entity = mapper.toEntity(dto);
                    entity.setUuid(UUID.randomUUID());
                    entity.setCreatedAt(LocalDateTime.now());
                    return entity;
                })
                .toList();

        List<DevToolLogEntry> saved = logEntryRepository.saveAll(entities);
        return saved.stream().map(mapper::toDto).toList();
    }

    /**
     * Query log entries.
     */
    @Transactional(readOnly = true)
    public Page<LogEntryDto> queryLogs(LogQuery query, Pageable pageable) {
        LocalDateTime startDate = query.getStartDate() != null
                ? query.getStartDate()
                : LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = query.getEndDate() != null
                ? query.getEndDate()
                : LocalDateTime.now();

        if (query.getSearch() != null && !query.getSearch().isBlank()) {
            return logEntryRepository.search(query.getSearch(), startDate, endDate, pageable)
                    .map(mapper::toDto);
        }

        if (query.getLevels() != null && !query.getLevels().isEmpty()) {
            List<DevToolLogEntry> entries = logEntryRepository.findByLevelsAndDateRange(
                    query.getLevels(), startDate, endDate);
            List<LogEntryDto> dtos = entries.stream().map(mapper::toDto).toList();
            return mockPage(dtos, pageable);
        }

        List<DevToolLogEntry> entries = logEntryRepository.findByDateRange(startDate, endDate);
        List<LogEntryDto> dtos = entries.stream().map(mapper::toDto).toList();
        return mockPage(dtos, pageable);
    }

    /**
     * Get logs by session ID.
     */
    @Transactional(readOnly = true)
    public List<LogEntryDto> getLogsBySession(String sessionId) {
        // Check in-memory cache first
        LinkedBlockingQueue<DevToolLogEntry> cached = sessionLogs.get(sessionId);
        if (cached != null && !cached.isEmpty()) {
            return cached.stream().map(mapper::toDto).toList();
        }

        return logEntryRepository.findBySessionId(sessionId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Get logs by request ID.
     */
    @Transactional(readOnly = true)
    public List<LogEntryDto> getLogsByRequest(String requestId) {
        return logEntryRepository.findByRequestId(requestId)
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    /**
     * Get log statistics.
     */
    @Transactional(readOnly = true)
    public LogStatistics getStatistics(LocalDateTime startDate, LocalDateTime endDate) {
        if (endDate == null) {
            endDate = LocalDateTime.now();
        }
        if (startDate == null) {
            startDate = endDate.minusDays(1);
        }

        LogStatistics stats = new LogStatistics();
        stats.setStartDate(startDate);
        stats.setEndDate(endDate);

        Long errorCount = logEntryRepository.countByLevel("ERROR");
        Long warnCount = logEntryRepository.countByLevel("WARN");
        Long infoCount = logEntryRepository.countByLevel("INFO");
        Long debugCount = logEntryRepository.countByLevel("DEBUG");

        stats.setErrorCount(errorCount != null ? errorCount : 0);
        stats.setWarnCount(warnCount != null ? warnCount : 0);
        stats.setInfoCount(infoCount != null ? infoCount : 0);
        stats.setDebugCount(debugCount != null ? debugCount : 0);
        stats.setTotalCount(stats.getErrorCount() + stats.getWarnCount()
                + stats.getInfoCount() + stats.getDebugCount());

        return stats;
    }

    /**
     * Clear old log entries based on retention policy.
     */
    @Scheduled(cron = "0 0 2 * * ?") // Run at 2 AM daily
    @Transactional
    public void cleanupOldLogs() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(retentionDays);
        log.info("Cleaning up log entries older than {}", cutoffDate);

        logEntryRepository.deleteByCreatedAtBefore(cutoffDate);
        log.info("Deleted old log entries older than {}", cutoffDate);
    }

    /**
     * Get log levels.
     */
    public List<String> getLogLevels() {
        return List.of("ERROR", "WARN", "INFO", "DEBUG", "TRACE");
    }

    /**
     * Get log sources.
     */
    public List<String> getLogSources() {
        return List.of("API", "DATABASE", "DEPLOYMENT", "DOCUMENTATION", "SYSTEM", "SECURITY");
    }

    /**
     * Export logs as JSON.
     */
    @Transactional(readOnly = true)
    public String exportLogs(LogQuery query) {
        LocalDateTime startDate = query.getStartDate() != null
                ? query.getStartDate()
                : LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = query.getEndDate() != null
                ? query.getEndDate()
                : LocalDateTime.now();

        List<DevToolLogEntry> entries;
        if (query.getLevels() != null && !query.getLevels().isEmpty()) {
            entries = logEntryRepository.findByLevelsAndDateRange(query.getLevels(), startDate, endDate);
        } else {
            entries = logEntryRepository.findByDateRange(startDate, endDate);
        }

        try {
            return objectMapper.writeValueAsString(entries);
        } catch (Exception e) {
            log.error("Failed to export logs", e);
            return "[]";
        }
    }

    /**
     * Create a system log entry.
     */
    public void logSystemEvent(String level, String source, String message, String requestId) {
        LogEntryDto dto = LogEntryDto.builder()
                .level(level)
                .source(source)
                .category("SYSTEM")
                .message(message)
                .requestId(requestId)
                .build();

        createLogEntry(dto);
    }

    /**
     * Create an error log entry with stack trace.
     */
    public void logError(String source, String message, Throwable throwable, String requestId) {
        LogEntryDto dto = LogEntryDto.builder()
                .level("ERROR")
                .source(source)
                .category("EXCEPTION")
                .message(message)
                .stackTrace(throwable != null ? getStackTrace(throwable) : null)
                .requestId(requestId)
                .build();

        createLogEntry(dto);
    }

    private String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    @SuppressWarnings("unchecked")
    private <T> Page<T> mockPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        List<T> sublist = start < list.size() ? list.subList(start, end) : List.of();

        return new org.springframework.data.domain.PageImpl<>(sublist, pageable, list.size());
    }
}
