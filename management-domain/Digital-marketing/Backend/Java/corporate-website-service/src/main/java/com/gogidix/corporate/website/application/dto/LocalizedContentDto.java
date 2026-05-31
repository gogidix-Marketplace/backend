package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.domain.model.Language;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocalizedContentDto {
    private Language language;
    private String title;
    private String content;
    private String excerpt;
    private String slug;
}
