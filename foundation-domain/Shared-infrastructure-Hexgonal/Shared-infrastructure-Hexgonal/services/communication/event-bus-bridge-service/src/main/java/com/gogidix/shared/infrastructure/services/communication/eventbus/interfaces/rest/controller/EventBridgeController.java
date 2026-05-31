package com.gogidix.shared.infrastructure.services.communication.eventbus.interfaces.rest.controller;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.request.CreateEventBridgeRequestDto;
import com.gogidix.shared.infrastructure.services.communication.eventbus.application.dto.response.EventBridgeResponseDto;
import com.gogidix.shared.infrastructure.services.communication.eventbus.domain.port.in.IEventBridgeUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * REST Controller for EventBridge management
 */
@RestController
@RequestMapping("/api/v1/event-bridges")
@CrossOrigin(origins = "*")
public class EventBridgeController {
    private final IEventBridgeUseCase eventBridgePort;
    public EventBridgeController(IEventBridgeUseCase eventBridgePort) {
        this.eventBridgePort = eventBridgePort;
    }
    @PostMapping
    public ResponseEntity<EventBridgeResponseDto> create(
            @Valid @RequestBody CreateEventBridgeRequestDto dto) {
        EventBridgeResponseDto created = eventBridgePort.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/{id}")
    public ResponseEntity<EventBridgeResponseDto> getById(@PathVariable String id) {
        EventBridgeResponseDto entity = eventBridgePort.findById(id);
        return ResponseEntity.ok(entity);
    }
    @GetMapping
    public ResponseEntity<List<EventBridgeResponseDto>> getAll() {
        List<EventBridgeResponseDto> entities = eventBridgePort.findAll();
        return ResponseEntity.ok(entities);
    }
    @GetMapping("/by-status/{status}")
    public ResponseEntity<List<EventBridgeResponseDto>> getByStatus(@PathVariable String status) {
        List<EventBridgeResponseDto> entities = eventBridgePort.findByStatus(status);
        return ResponseEntity.ok(entities);
    }
    @GetMapping("/by-source-type/{sourceType}")
    public ResponseEntity<List<EventBridgeResponseDto>> getBySourceType(@PathVariable String sourceType) {
        List<EventBridgeResponseDto> entities = eventBridgePort.findBySourceType(sourceType);
        return ResponseEntity.ok(entities);
    }
    @GetMapping("/by-target-type/{targetType}")
    public ResponseEntity<List<EventBridgeResponseDto>> getByTargetType(@PathVariable String targetType) {
        List<EventBridgeResponseDto> entities = eventBridgePort.findByTargetType(targetType);
        return ResponseEntity.ok(entities);
    }
    @GetMapping("/enabled")
    public ResponseEntity<List<EventBridgeResponseDto>> getEnabled() {
        List<EventBridgeResponseDto> entities = eventBridgePort.findEnabled();
        return ResponseEntity.ok(entities);
    }
    @GetMapping("/stats")
    public ResponseEntity<IEventBridgeUseCase.EventBridgeStatsDto> getStats() {
        IEventBridgeUseCase.EventBridgeStatsDto stats = eventBridgePort.getStats();
        return ResponseEntity.ok(stats);
    }
    @PutMapping("/{id}")
    public ResponseEntity<EventBridgeResponseDto> update(
            @PathVariable String id,
            @Valid @RequestBody CreateEventBridgeRequestDto dto) {
        EventBridgeResponseDto updated = eventBridgePort.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        eventBridgePort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
