package com.gogidix.globalbusinessmanagement.currencyconversion.interfaces.rest;
import com.gogidix.globalbusinessmanagement.currencyconversion.application.dto.CurrencyConversionRequestDto;
import com.gogidix.globalbusinessmanagement.currencyconversion.application.dto.CurrencyConversionResponseDto;
import com.gogidix.globalbusinessmanagement.currencyconversion.application.service.CurrencyConversionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/v1/currency-conversions")
@RequiredArgsConstructor
public class CurrencyConversionController {
    private final CurrencyConversionService service;
    @PostMapping
    public ResponseEntity<CurrencyConversionResponseDto> create(@RequestBody CurrencyConversionRequestDto dto) { return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto)); }
    @GetMapping("/{id}")
    public ResponseEntity<CurrencyConversionResponseDto> getById(@PathVariable String id) { return ResponseEntity.ok(service.getById(id)); }
    @GetMapping
    public ResponseEntity<List<CurrencyConversionResponseDto>> getAll() { return ResponseEntity.ok(service.getAll()); }
    @PutMapping("/{id}")
    public ResponseEntity<CurrencyConversionResponseDto> update(@PathVariable String id, @RequestBody CurrencyConversionRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
