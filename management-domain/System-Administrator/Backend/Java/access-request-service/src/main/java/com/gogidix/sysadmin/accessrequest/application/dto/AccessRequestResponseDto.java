package com.gogidix.sysadmin.accessrequest.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestResponseDto {
    private String id;
    private String tenantId;
    private String requestNumber;
    private String requestedBy;
    private String requestedFor;
    private String requestType;
    private String status;
    private String resourceType;
    private List<String> resourceIds;
    private String accessLevel;
    private String justification;
    private Instant createdAt;
    private Instant updatedAt;
}
