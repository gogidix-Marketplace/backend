package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentRequest {
    @NotNull
    private Set<Region> availableRegions;
    private ContentStatus status;
    private LocalDateTime publishDate;
    private LocalDateTime unpublishDate;
    private List<String> tags;
}
