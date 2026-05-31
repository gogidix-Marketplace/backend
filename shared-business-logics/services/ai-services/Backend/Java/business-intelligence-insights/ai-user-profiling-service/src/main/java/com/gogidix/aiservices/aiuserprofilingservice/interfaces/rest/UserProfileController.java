package com.gogidix.aiservices.aiuserprofilingservice.interfaces.rest;

import com.gogidix.aiservices.aiuserprofilingservice.application.command.AddUsersToProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.AnalyzeProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.CreateProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.DeleteProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.RemoveUsersFromProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.command.UpdateProfileCommand;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.AnalyzeProfileRequestDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.CreateProfileRequestDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.UserProfileResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.ModifyUsersRequestDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.PagedResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.ProfileAnalysisResponseDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.ProfileSearchRequestDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.dto.UpdateProfileRequestDto;
import com.gogidix.aiservices.aiuserprofilingservice.application.service.UserProfileApplicationService;
import com.gogidix.aiservices.aiuserprofilingservice.domain.model.ProfileType;
import com.gogidix.aiservices.aiuserprofilingservice.shared.context.RequestContext;
import com.gogidix.aiservices.aiuserprofilingservice.shared.context.RequestContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * REST controller for customer segment operations.
 */
@RestController
@RequestMapping("/segments")
@Tag(name = "User Profiles", description = "APIs for managing customer segments")
public class UserProfileController {

    private final UserProfileApplicationService service;

    public UserProfileController(UserProfileApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Create a new segment", description = "Creates a new customer segment with the provided criteria")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Profile created successfully",
                    headers = @Header(name = "X-Correlation-Id", description = "Correlation ID for tracking"),
                    content = @Content(schema = @Schema(implementation = UserProfileResponseDto.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "409", description = "Maximum segments per tenant reached")
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserProfileResponseDto> createProfile(
            @Valid @RequestBody CreateProfileRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        CreateProfileCommand command = new CreateProfileCommand(
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                context.tenantId(),
                context.userId()
        );

        UserProfileResponseDto response = service.createProfile(command);

        return ResponseEntity
                .created(URI.create("/segments/" + response.id()))
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping("/{segmentId}")
    @Operation(summary = "Get a segment by ID", description = "Retrieves a customer segment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UserProfileResponseDto> getProfile(
            @Parameter(description = "Profile ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();
        UserProfileResponseDto response = service.getProfileById(segmentId, context.tenantId());

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @GetMapping
    @Operation(summary = "List segments", description = "Retrieves a paginated list of segments for the current tenant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profiles retrieved successfully")
    })
    public ResponseEntity<PagedResponseDto<UserProfileResponseDto>> listProfiles(
            @Parameter(description = "Filter by segment type")
            @RequestParam(required = false) ProfileType segmentType,

            @Parameter(description = "Filter by active status")
            @RequestParam(required = false) Boolean active,

            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(required = false, defaultValue = "0") int page,

            @Parameter(description = "Page size", example = "20")
            @RequestParam(required = false, defaultValue = "20") int size,

            @Parameter(description = "Sort field", example = "createdAt")
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,

            @Parameter(description = "Sort direction (ASC/DESC)", example = "DESC")
            @RequestParam(required = false, defaultValue = "DESC") String sortDirection
    ) {
        RequestContext context = RequestContextHolder.getContext();

        PagedResponseDto<UserProfileResponseDto> response = service.getProfilesByTenant(
                context.tenantId(),
                segmentType,
                active,
                page,
                size,
                sortBy,
                sortDirection
        );

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PutMapping("/{segmentId}")
    @Operation(summary = "Update a segment", description = "Updates an existing customer segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UserProfileResponseDto> updateProfile(
            @Parameter(description = "Profile ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody UpdateProfileRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        UpdateProfileCommand command = new UpdateProfileCommand(
                segmentId,
                request.name(),
                request.description(),
                request.segmentType(),
                request.criteria(),
                request.active(),
                context.tenantId(),
                context.userId()
        );

        UserProfileResponseDto response = service.updateProfile(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}")
    @Operation(summary = "Delete a segment", description = "Deletes a customer segment by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Profile deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteProfile(
            @Parameter(description = "Profile ID", required = true)
            @PathVariable @NotBlank String segmentId
    ) {
        RequestContext context = RequestContextHolder.getContext();

        service.deleteProfile(segmentId, context.tenantId(), context.userId());

        return ResponseEntity.noContent()
                .header("X-Correlation-Id", context.correlationId())
                .build();
    }

    @PostMapping("/{segmentId}/analyze")
    @Operation(summary = "Analyze a segment", description = "Performs analysis on a customer segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Analysis completed successfully"),
            @ApiResponse(responseCode = "404", description = "Profile not found"),
            @ApiResponse(responseCode = "400", description = "Profile is not active")
    })
    public ResponseEntity<ProfileAnalysisResponseDto> analyzeProfile(
            @Parameter(description = "Profile ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @RequestBody(required = false) AnalyzeProfileRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AnalyzeProfileRequestDto actualRequest = request != null ? request : AnalyzeProfileRequestDto.create();

        AnalyzeProfileCommand command = new AnalyzeProfileCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                actualRequest.analysisOptions()
        );

        ProfileAnalysisResponseDto response = service.analyzeProfile(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @PostMapping("/{segmentId}/customers")
    @Operation(summary = "Add customers to segment", description = "Adds customers to an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or too many customers"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UserProfileResponseDto> addUsers(
            @Parameter(description = "Profile ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyUsersRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        AddUsersToProfileCommand command = new AddUsersToProfileCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        UserProfileResponseDto response = service.addUsersToProfile(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }

    @DeleteMapping("/{segmentId}/customers")
    @Operation(summary = "Remove customers from segment", description = "Removes customers from an existing segment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users removed successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "404", description = "Profile not found")
    })
    public ResponseEntity<UserProfileResponseDto> removeUsers(
            @Parameter(description = "Profile ID", required = true)
            @PathVariable @NotBlank String segmentId,

            @Valid @RequestBody ModifyUsersRequestDto request
    ) {
        RequestContext context = RequestContextHolder.getContext();

        RemoveUsersFromProfileCommand command = new RemoveUsersFromProfileCommand(
                segmentId,
                context.tenantId(),
                context.userId(),
                request.customerIds()
        );

        UserProfileResponseDto response = service.removeUsersFromProfile(command);

        return ResponseEntity.ok()
                .header("X-Correlation-Id", context.correlationId())
                .body(response);
    }
}
