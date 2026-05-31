package com.gogidix.platform.platform.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for platform announcement responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementDto {

    private String id;
    private String tenantId;
    private String title;
    private String content;
    private String announcementType;
    private String priority;
    private boolean isActive;
    private LocalDateTime effectiveFrom;
    private LocalDateTime effectiveUntil;
    private String[] affectedServices;
    private String[] affectedTenants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
