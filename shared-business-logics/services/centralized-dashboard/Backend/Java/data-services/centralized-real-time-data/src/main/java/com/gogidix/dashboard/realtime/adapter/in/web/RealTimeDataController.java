package com.gogidix.dashboard.realtime.adapter.in.web;

import com.gogidix.dashboard.realtime.domain.model.*;
import com.gogidix.dashboard.realtime.domain.port.in.RealTimeDataManagementUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Real-Time Data REST Controller
 * 
 * Inbound adapter for HTTP/REST and WebSocket requests
 * Provides REST API and WebSocket endpoints for real-time data streaming
 */
@RestController
@RequestMapping("/api/realtime")
@CrossOrigin(origins = "*")
public class RealTimeDataController {
    
    private final RealTimeDataManagementUseCase realTimeService;
    
    public RealTimeDataController(RealTimeDataManagementUseCase realTimeService) {
        this.realTimeService = realTimeService;
    }
    
    /**
     * Create a new stream
     */
    @PostMapping("/streams")
    public ResponseEntity<RealTimeDataStream> createStream(
            @Valid @RequestBody CreateStreamRequestDTO request) {
        
        RealTimeDataManagementUseCase.CreateStreamCommand command = 
            new RealTimeDataManagementUseCase.CreateStreamCommand(
                request.getStreamName(),
                request.getDomain(),
                request.getConfiguration()
            );
        
        RealTimeDataStream stream = realTimeService.createStream(command);
        return ResponseEntity.ok(stream);
    }
    
    /**
     * Start a stream
     */
    @PostMapping("/streams/{id}/start")
    public ResponseEntity<Void> startStream(@PathVariable String id) {
        realTimeService.startStream(StreamId.fromString(id));
        return ResponseEntity.ok().build();
    }
    
    /**
     * Stop a stream
     */
    @PostMapping("/streams/{id}/stop")
    public ResponseEntity<Void> stopStream(@PathVariable String id) {
        realTimeService.stopStream(StreamId.fromString(id));
        return ResponseEntity.ok().build();
    }
    
    /**
     * Get stream by ID
     */
    @GetMapping("/streams/{id}")
    public ResponseEntity<RealTimeDataStream> getStream(@PathVariable String id) {
        return realTimeService.getStream(StreamId.fromString(id))
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Get all active streams
     */
    @GetMapping("/streams/active")
    public ResponseEntity<List<RealTimeDataStream>> getActiveStreams() {
        List<RealTimeDataStream> streams = realTimeService.getActiveStreams();
        return ResponseEntity.ok(streams);
    }
    
    /**
     * Get streams by domain
     */
    @GetMapping("/streams/domain/{domain}")
    public ResponseEntity<List<RealTimeDataStream>> getStreamsByDomain(@PathVariable String domain) {
        List<RealTimeDataStream> streams = realTimeService.getStreamsByDomain(domain);
        return ResponseEntity.ok(streams);
    }
    
    /**
     * Push data to stream (REST endpoint, WebSocket removed for now)
     */
    @PostMapping("/streams/{streamId}/push")
    public ResponseEntity<Void> handleStreamData(@PathVariable String streamId, @RequestBody Object data) {
        realTimeService.pushData(StreamId.fromString(streamId), data);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Push data to stream via REST
     */
    @PostMapping("/streams/{id}/data")
    public ResponseEntity<Void> pushData(@PathVariable String id, @RequestBody Object data) {
        realTimeService.pushData(StreamId.fromString(id), data);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Delete stream
     */
    @DeleteMapping("/streams/{id}")
    public ResponseEntity<Void> deleteStream(@PathVariable String id) {
        realTimeService.deleteStream(StreamId.fromString(id));
        return ResponseEntity.ok().build();
    }
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Real-Time Data Service is running");
    }
    
    // Simple DTO for request
    public static class CreateStreamRequestDTO {
        private String streamName;
        private String domain;
        private StreamConfiguration configuration;
        
        public String getStreamName() { return streamName; }
        public void setStreamName(String streamName) { this.streamName = streamName; }
        
        public String getDomain() { return domain; }
        public void setDomain(String domain) { this.domain = domain; }
        
        public StreamConfiguration getConfiguration() { return configuration; }
        public void setConfiguration(StreamConfiguration configuration) { this.configuration = configuration; }
    }
}