package com.gogidix.sysadmin.accessrequest.interfaces.rest;

import com.gogidix.sysadmin.accessrequest.application.dto.AccessRequestDto;
import com.gogidix.sysadmin.accessrequest.application.dto.AccessRequestResponseDto;
import com.gogidix.sysadmin.accessrequest.application.service.AccessRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/access-requests")
@RequiredArgsConstructor
public class AccessRequestController {

    private final AccessRequestService service;

    @PostMapping
    public ResponseEntity<AccessRequestResponseDto> create(@RequestBody AccessRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<AccessRequestResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<AccessRequestResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
