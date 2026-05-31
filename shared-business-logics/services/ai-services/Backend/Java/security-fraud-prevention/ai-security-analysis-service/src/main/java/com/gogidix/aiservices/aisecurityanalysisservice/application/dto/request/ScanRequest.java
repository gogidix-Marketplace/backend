package com.gogidix.aiservices.aisecurityanalysisservice.application.dto.request;

import com.gogidix.aiservices.aisecurityanalysisservice.domain.model.ScanType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ScanRequest {
    @NotBlank
    private String target;

    private ScanType scanType = ScanType.QUICK;
}
