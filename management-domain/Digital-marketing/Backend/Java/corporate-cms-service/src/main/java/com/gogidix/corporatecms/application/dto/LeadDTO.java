package com.gogidix.corporatecms.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.corporatecms.domain.enums.LeadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Lead entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Lead DTO for managing marketing leads")
public class LeadDTO {

    @Schema(description = "Lead ID")
    private String id;

    @Schema(description = "Lead status")
    private LeadStatus status;

    @Schema(description = "First name")
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name must not exceed 100 characters")
    private String firstName;

    @Schema(description = "Last name")
    @Size(max = 100, message = "Last name must not exceed 100 characters")
    private String lastName;

    @Schema(description = "Email address")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Phone number")
    private String phone;

    @Schema(description = "Company name")
    private String company;

    @Schema(description = "Job title")
    private String jobTitle;

    @Schema(description = "Industry")
    private String industry;

    @Schema(description = "Company size")
    private String companySize;

    @Schema(description = "Country")
    private String country;

    @Schema(description = "Lead source")
    private String source;

    @Schema(description = "Medium")
    private String medium;

    @Schema(description = "Campaign")
    private String campaign;

    @Schema(description = "Content ID")
    private String contentId;

    @Schema(description = "Form ID")
    private String formId;

    @Schema(description = "Lead magnet")
    private String leadMagnet;

    @Schema(description = "Assigned to user ID")
    private String assignedTo;

    @Schema(description = "Assigned to user name")
    private String assignedToName;

    @Schema(description = "Notes")
    @Size(max = 2000, message = "Notes must not exceed 2000 characters")
    private String notes;

    @Schema(description = "Custom fields")
    private Map<String, Object> customFields;

    @Schema(description = "Lead score")
    private Integer score;

    @Schema(description = "Converted at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime convertedAt;

    @Schema(description = "Customer ID")
    private String customerId;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
