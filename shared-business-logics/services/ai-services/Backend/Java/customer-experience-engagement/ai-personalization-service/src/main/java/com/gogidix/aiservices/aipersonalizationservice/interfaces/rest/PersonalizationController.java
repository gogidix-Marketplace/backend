package com.gogidix.aiservices.aipersonalizationservice.interfaces.rest;

import com.gogidix.aiservices.aipersonalizationservice.application.dto.request.CreateProfileRequest;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.request.TrackBehaviorRequest;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.request.UpdateAttributesRequest;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.response.ProfileResponse;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.response.RecommendationResponse;
import com.gogidix.aiservices.aipersonalizationservice.application.dto.response.BehaviorTrackingResponse;
import com.gogidix.aiservices.aipersonalizationservice.application.service.PersonalizationService;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.PersonalizationException;
import com.gogidix.aiservices.aipersonalizationservice.shared.exception.ProfileNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/personalization")
@RequiredArgsConstructor
@Validated
public class PersonalizationController {

    private final PersonalizationService personalizationService;

    @PostMapping("/profiles")
    public ResponseEntity<ProfileResponse> createProfile(@Valid @RequestBody CreateProfileRequest request) {
        ProfileResponse response = personalizationService.createProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/profiles/{userId}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable String userId) {
        ProfileResponse response = personalizationService.getProfile(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profiles/{userId}")
    public ResponseEntity<ProfileResponse> updateAttributes(
            @PathVariable String userId,
            @Valid @RequestBody UpdateAttributesRequest request) {
        // Override userId in request with path variable
        UpdateAttributesRequest updatedRequest = request.toBuilder()
                .userId(userId)
                .build();
        ProfileResponse response = personalizationService.updateAttributes(updatedRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/profiles/{userId}")
    public ResponseEntity<Void> deleteProfile(@PathVariable String userId) {
        personalizationService.deleteProfile(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<RecommendationResponse> getRecommendations(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") Integer limit,
            @RequestParam(defaultValue = "content") String type,
            @RequestParam(defaultValue = "homepage") String context) {

        if (limit < 1 || limit > 100) {
            throw new IllegalArgumentException("Limit must be between 1 and 100");
        }

        if (!Set.of("content", "product", "user").contains(type)) {
            throw new IllegalArgumentException("Invalid recommendation type: " + type);
        }

        RecommendationResponse response = personalizationService.getRecommendations(userId, limit, type, context);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/behaviors")
    public ResponseEntity<BehaviorTrackingResponse> trackBehaviors(
            @Valid @RequestBody TrackBehaviorRequest request) {
        BehaviorTrackingResponse response = personalizationService.trackBehaviors(request);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProfileNotFound(ProfileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(PersonalizationException.class)
    public ResponseEntity<Map<String, Object>> handlePersonalizationException(PersonalizationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(createErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse("An unexpected error occurred"));
    }

    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", message);
        error.put("message", message);
        error.put("timestamp", Instant.now());
        return error;
    }
}
