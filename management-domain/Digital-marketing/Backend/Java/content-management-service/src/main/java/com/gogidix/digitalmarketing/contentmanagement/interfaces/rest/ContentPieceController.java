package com.gogidix.digitalmarketing.contentmanagement.interfaces.rest;

import com.gogidix.digitalmarketing.contentmanagement.application.dto.ContentPieceRequestDto;
import com.gogidix.digitalmarketing.contentmanagement.application.dto.ContentPieceResponseDto;
import com.gogidix.digitalmarketing.contentmanagement.application.service.ContentPieceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/content-pieces")
@RequiredArgsConstructor
@Tag(name = "Content Management", description = "Content Management")
public class ContentPieceController {

    private final ContentPieceService service;

    @PostMapping
    @Operation(summary = "Create a new ContentPiece")
    public ResponseEntity<ContentPieceResponseDto> create(@RequestBody ContentPieceRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get ContentPiece by ID")
    public ResponseEntity<ContentPieceResponseDto> getById(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping
    @Operation(summary = "Get all ContentPieces")
    public ResponseEntity<List<ContentPieceResponseDto>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update ContentPiece")
    public ResponseEntity<ContentPieceResponseDto> update(@PathVariable String id, @RequestBody ContentPieceRequestDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete ContentPiece")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}