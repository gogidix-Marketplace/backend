package com.gogidix.aiservices.leadgenerationaiservice.application.dto.request;

import com.gogidix.aiservices.leadgenerationaiservice.domain.model.QualificationCriteria;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddQualificationRequest {
    @NotNull(message = "Lead ID is required")
    private String leadId;

    @NotNull(message = "Criteria is required")
    private QualificationCriteria criteria;
}
