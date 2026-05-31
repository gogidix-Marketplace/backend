package com.gogidix.customersupport.feedback.interfaces.rest;

import com.gogidix.customersupport.feedback.application.dto.request.CSATSurveyRequestDto;
import com.gogidix.customersupport.feedback.application.dto.response.CSATSurveyResponseDto;
import com.gogidix.customersupport.feedback.application.service.CSATSurveyService;
import com.gogidix.customersupport.feedback.domain.model.CSATSurvey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for CSAT Survey management
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/surveys/csats")
@RequiredArgsConstructor
@Tag(name = "CSAT Survey Management", description = "APIs for managing CSAT surveys")
public class CSATSurveyController {

    private final CSATSurveyService surveyService;

    @GetMapping
    @Operation(summary = "Get all CSAT surveys", description = "Retrieve all CSAT surveys with pagination")
    public ResponseEntity<Page<CSATSurveyResponseDto>> getAllSurveys(
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(surveyService.getAllSurveys(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get CSAT survey by ID", description = "Retrieve a specific CSAT survey by ID")
    public ResponseEntity<CSATSurveyResponseDto> getSurveyById(
            @Parameter(description = "Survey ID") @PathVariable String id) {
        return ResponseEntity.ok(surveyService.getSurveyById(id));
    }

    @GetMapping("/survey-id/{surveyId}")
    @Operation(summary = "Get CSAT survey by survey ID", description = "Retrieve a specific CSAT survey by survey ID")
    public ResponseEntity<CSATSurveyResponseDto> getSurveyBySurveyId(
            @Parameter(description = "Survey ID") @PathVariable String surveyId) {
        return ResponseEntity.ok(surveyService.getSurveyBySurveyId(surveyId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get CSAT surveys by status", description = "Retrieve CSAT surveys filtered by status")
    public ResponseEntity<List<CSATSurveyResponseDto>> getSurveysByStatus(
            @Parameter(description = "Survey status") @PathVariable CSATSurvey.SurveyStatus status) {
        return ResponseEntity.ok(surveyService.getSurveysByStatus(status));
    }

    @GetMapping("/active")
    @Operation(summary = "Get active CSAT surveys", description = "Retrieve all active CSAT surveys")
    public ResponseEntity<List<CSATSurveyResponseDto>> getActiveSurveys() {
        return ResponseEntity.ok(surveyService.getActiveSurveys());
    }

    @GetMapping("/locale/{locale}")
    @Operation(summary = "Get CSAT surveys by locale", description = "Retrieve CSAT surveys filtered by locale")
    public ResponseEntity<List<CSATSurveyResponseDto>> getSurveysByLocale(
            @Parameter(description = "Locale") @PathVariable String locale) {
        return ResponseEntity.ok(surveyService.getSurveysByLocale(locale));
    }

    @PostMapping
    @Operation(summary = "Create CSAT survey", description = "Create a new CSAT survey")
    public ResponseEntity<CSATSurveyResponseDto> createSurvey(
            @Valid @RequestBody CSATSurveyRequestDto request) {
        CSATSurveyResponseDto created = surveyService.createSurvey(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update CSAT survey", description = "Update an existing CSAT survey")
    public ResponseEntity<CSATSurveyResponseDto> updateSurvey(
            @Parameter(description = "Survey ID") @PathVariable String id,
            @Valid @RequestBody CSATSurveyRequestDto request) {
        return ResponseEntity.ok(surveyService.updateSurvey(id, request));
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate CSAT survey", description = "Activate a CSAT survey")
    public ResponseEntity<CSATSurveyResponseDto> activateSurvey(
            @Parameter(description = "Survey ID") @PathVariable String id) {
        return ResponseEntity.ok(surveyService.activateSurvey(id));
    }

    @PutMapping("/{id}/pause")
    @Operation(summary = "Pause CSAT survey", description = "Pause a CSAT survey")
    public ResponseEntity<CSATSurveyResponseDto> pauseSurvey(
            @Parameter(description = "Survey ID") @PathVariable String id) {
        return ResponseEntity.ok(surveyService.pauseSurvey(id));
    }

    @PutMapping("/{id}/close")
    @Operation(summary = "Close CSAT survey", description = "Close a CSAT survey")
    public ResponseEntity<CSATSurveyResponseDto> closeSurvey(
            @Parameter(description = "Survey ID") @PathVariable String id) {
        return ResponseEntity.ok(surveyService.closeSurvey(id));
    }

    @PostMapping("/{surveyId}/responses/increment")
    @Operation(summary = "Increment survey response count", description = "Increment the response count for a survey")
    public ResponseEntity<Void> incrementResponseCount(
            @Parameter(description = "Survey ID") @PathVariable String surveyId) {
        surveyService.incrementResponseCount(surveyId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete CSAT survey", description = "Delete a CSAT survey")
    public ResponseEntity<Void> deleteSurvey(
            @Parameter(description = "Survey ID") @PathVariable String id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.noContent().build();
    }
}
