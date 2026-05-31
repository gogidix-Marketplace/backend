package com.gogidix.corporatecms.domain.enums;

import lombok.Getter;

/**
 * Enumeration representing different types of content managed by the CMS.
 */
@Getter
public enum ContentType {
    PAGE("page", "Static page content"),
    BLOG_POST("blog_post", "Blog article"),
    PRESS_RELEASE("press_release", "Press release"),
    RESOURCE("resource", "Downloadable resource"),
    NEWS("news", "News article"),
    CASE_STUDY("case_study", "Customer case study"),
    EVENT("event", "Upcoming event"),
    TESTIMONIAL("testimonial", "Customer testimonial"),
    CAREER("career", "Job posting"),
    PRODUCT("product", "Product catalog entry"),
    DEVELOPER_RESOURCE("developer_resource", "Developer documentation");

    private final String code;
    private final String description;

    ContentType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static ContentType fromCode(String code) {
        for (ContentType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown content type: " + code);
    }
}
