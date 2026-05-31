package com.gogidix.globalbusinessmanagement.countryingestion.interfaces.rest;

import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.CountryDataDto;
import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.IngestionBatchDto;
import com.gogidix.globalbusinessmanagement.countryingestion.application.dto.ValidationErrorDto;
import com.gogidix.globalbusinessmanagement.countryingestion.application.service.CountryIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/country-ingestion")
@RequiredArgsConstructor
public class CountryIngestionController {

    private final CountryIngestionService countryIngestionService;

    @PostMapping("/batches")
    public ResponseEntity<IngestionBatchDto> createBatch(
            @RequestParam String source,
            @RequestParam(defaultValue = "CSV") String format,
            @RequestParam(required = false) Long fileSize,
            @RequestParam(required = false) String fileName,
            @RequestParam(required = false) String schemaId,
            @RequestParam String createdBy) {
        return ResponseEntity.ok(countryIngestionService.createBatch(source, format, fileSize, fileName, schemaId, createdBy));
    }

    @PostMapping("/batches/{batchId}/csv")
    public ResponseEntity<IngestionBatchDto> ingestCsv(
            @PathVariable String batchId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) Map<String, String> columnMappings,
            @RequestParam(defaultValue = "false") boolean validateOnly) throws IOException {
        return ResponseEntity.ok(countryIngestionService.ingestFromCsv(batchId, file, columnMappings, validateOnly));
    }

    @PostMapping("/batches/{batchId}/json")
    public ResponseEntity<IngestionBatchDto> ingestJson(
            @PathVariable String batchId,
            @RequestBody String jsonData,
            @RequestParam(defaultValue = "false") boolean validateOnly) throws IOException {
        return ResponseEntity.ok(countryIngestionService.ingestFromJson(batchId, jsonData, validateOnly));
    }

    @GetMapping("/batches/{batchId}")
    public ResponseEntity<IngestionBatchDto> getBatch(@PathVariable String batchId) {
        return ResponseEntity.ok(countryIngestionService.getBatch(batchId));
    }

    @GetMapping("/batches/{batchId}/errors")
    public ResponseEntity<List<ValidationErrorDto>> getValidationErrors(@PathVariable String batchId) {
        return ResponseEntity.ok(countryIngestionService.getValidationErrorsForBatch(batchId));
    }

    @PostMapping("/batches/{batchId}/reprocess")
    public ResponseEntity<IngestionBatchDto> reprocessFailedRecords(
            @PathVariable String batchId,
            @RequestParam String createdBy) {
        return ResponseEntity.ok(countryIngestionService.reprocessFailedRecords(batchId, createdBy));
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity<CountryDataDto> getCountryById(@PathVariable String id) {
        return ResponseEntity.ok(countryIngestionService.getCountryById(id));
    }

    @GetMapping("/countries/code/{countryCode}")
    public ResponseEntity<CountryDataDto> getCountryByCode(@PathVariable String countryCode) {
        return ResponseEntity.ok(countryIngestionService.getCountryByCode(countryCode));
    }

    @GetMapping("/countries")
    public ResponseEntity<Page<CountryDataDto>> searchCountries(
            @RequestParam String searchTerm, Pageable pageable) {
        return ResponseEntity.ok(countryIngestionService.searchCountries(searchTerm, pageable));
    }

    @GetMapping("/countries/region/{region}")
    public ResponseEntity<List<CountryDataDto>> getCountriesByRegion(@PathVariable String region) {
        return ResponseEntity.ok(countryIngestionService.getCountriesByRegion(region));
    }

    @PutMapping("/countries/{id}")
    public ResponseEntity<CountryDataDto> updateCountry(
            @PathVariable String id, @RequestBody CountryDataDto dto) {
        return ResponseEntity.ok(countryIngestionService.updateCountry(id, dto));
    }

    @DeleteMapping("/countries/{id}")
    public ResponseEntity<Void> deleteCountry(@PathVariable String id) {
        countryIngestionService.deleteCountry(id);
        return ResponseEntity.noContent().build();
    }
}
