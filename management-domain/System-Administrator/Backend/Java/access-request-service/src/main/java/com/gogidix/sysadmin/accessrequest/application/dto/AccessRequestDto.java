package com.gogidix.sysadmin.accessrequest.application.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestDto {
    private String tenantId;
    private String requestedFor;
    private String requestType;
    private String resourceType;
    private List<String> resourceIds;
    private String accessLevel;
    private String justification;
    private Instant startDateTime;
    private Instant endDateTime;
}
