package com.gogidix.corporate.website.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeoMetadataDto {
    private String metaTitle;
    private String metaDescription;
    private String metaKeywords;
    private String ogTitle;
    private String ogDescription;
    private String ogImage;
    private String ogType;
    private String twitterCard;
    private String twitterTitle;
    private String twitterDescription;
    private String twitterImage;
    private String canonicalUrl;
    private boolean noIndex;
    private boolean noFollow;
    private Map<String, String> alternateLanguages;
}
