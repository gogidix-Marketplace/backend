package com.gogidix.aiservices.aicontentgenerationservice.domain.model;

import lombok.Getter;

@Getter
public enum ContentType {
    PRODUCT_DESCRIPTION("product_description", "Detailed product description for e-commerce"),
    BLOG_POST("blog_post", "SEO-optimized blog article"),
    SOCIAL_MEDIA("social_media", "Engaging social media content"),
    EMAIL("email", "Marketing email copy"),
    AD_COPY("ad_copy", "Advertisement text"),
    LANDING_PAGE("landing_page", "Landing page content"),
    FAQ("faq", "Frequently asked questions"),
    REVIEW("review", "Product or service review"),
    NEWS("news", "News article"),
    TUTORIAL("tutorial", "Step-by-step tutorial");

    private final String value;
    private final String description;

    ContentType(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ContentType fromString(String value) {
        for (ContentType type : ContentType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown content type: " + value);
    }
}
