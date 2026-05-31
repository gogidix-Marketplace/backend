package com.gogidix.platform.platform.domain.port.in;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * Input port: Command to create platform announcement.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAnnouncementCommand {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Announcement type is required")
    private com.gogidix.platform.platform.domain.model.PlatformAnnouncement.AnnouncementType announcementType;

    @NotNull(message = "Priority is required")
    private com.gogidix.platform.platform.domain.model.PlatformAnnouncement.Priority priority;

    private LocalDateTime effectiveFrom;

    private LocalDateTime effectiveUntil;

    private String[] affectedServices;

    private String[] affectedTenants;
}
