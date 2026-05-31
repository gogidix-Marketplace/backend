package com.gogidix.shared.warehousing.access.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to grant access
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrantAccessCommand {

    @NotBlank(message = "Request ID is required")
    private String requestId;

    @NotBlank(message = "Approved by is required")
    private String approvedBy;

    private String notes;
}
