package com.gogidix.finance.exchangerate.shared.requestcontext;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestContext {
    private String correlationId;
    private String userId;
    private String tenantId;
    private LocalDateTime timestamp;
    private Map<String, String> metadata;
}
