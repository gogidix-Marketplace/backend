package com.gogidix.shared.warehousing.spaceallocation.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command to release space
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReleaseSpaceCommand {

    @NotBlank(message = "Allocation ID is required")
    private String allocationId;

    private String reason;

    private String releasedBy;
}
