package com.gogidix.aiservices.leadgenerationaiservice.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignLeadRequest {
    @NotBlank(message = "Lead ID is required")
    private String leadId;

    @NotBlank(message = "Owner ID is required")
    private String ownerId;

    private String ownerName;
}
