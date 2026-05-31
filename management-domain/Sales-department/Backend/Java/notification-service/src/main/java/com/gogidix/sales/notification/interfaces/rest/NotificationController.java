package com.gogidix.sales.notification.interfaces.rest;

import com.gogidix.sales.notification.application.dto.request.NotificationRequestDto;
import com.gogidix.sales.notification.application.dto.response.NotificationResponseDto;
import com.gogidix.sales.notification.application.service.NotificationAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "Sales Notification API")
public class NotificationController {

    private final NotificationAppService service;

    @PostMapping
    @Operation(summary = "Send notification")
    public ResponseEntity<NotificationResponseDto> create(@RequestBody NotificationRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }

    @GetMapping("/{id}")
    @Operation(summary = "Get notification")
    public ResponseEntity<NotificationResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }

    @GetMapping
    @Operation(summary = "Get all notifications")
    public ResponseEntity<List<NotificationResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete notification")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
