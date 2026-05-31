package com.gogidix.globalbusinessmanagement.localization.interfaces.rest;
import com.gogidix.globalbusinessmanagement.localization.application.dto.LocalizationEntryRequestDto;
import com.gogidix.globalbusinessmanagement.localization.application.dto.LocalizationEntryResponseDto;
import com.gogidix.globalbusinessmanagement.localization.application.service.LocalizationEntryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/localizations")
@RequiredArgsConstructor
public class LocalizationEntryController {
    private final LocalizationEntryService service;
    @PostMapping
    public ResponseEntity<LocalizationEntryResponseDto> create(@RequestBody LocalizationEntryRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<LocalizationEntryResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<LocalizationEntryResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}")
    public ResponseEntity<LocalizationEntryResponseDto> update(@PathVariable String id, @RequestBody LocalizationEntryRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
