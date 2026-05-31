package com.gogidix.corporate.website.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaAsset {
    private String type;
    private String url;
    private String title;
    private String description;
    private Long sizeBytes;
    private String mimeType;
}
