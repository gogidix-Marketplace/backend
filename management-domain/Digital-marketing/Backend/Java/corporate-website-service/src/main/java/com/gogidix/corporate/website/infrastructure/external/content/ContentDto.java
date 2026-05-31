package com.gogidix.corporate.website.infrastructure.external.content;

import com.gogidix.corporate.website.domain.model.Language;
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
public class ContentDto {
    private String id;
    private String slug;
    private String type;
    private Language language;
    private String title;
    private String content;
    private Map<String, Object> metadata;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
