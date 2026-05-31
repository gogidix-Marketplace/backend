package com.gogidix.corporate.website.infrastructure.external.lead;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadCaptureResponse {
    private boolean success;
    private String message;
    private String leadId;
}
