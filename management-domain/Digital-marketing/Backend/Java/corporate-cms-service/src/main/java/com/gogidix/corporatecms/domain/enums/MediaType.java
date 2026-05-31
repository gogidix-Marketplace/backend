package com.gogidix.corporatecms.domain.enums;

import lombok.Getter;

/**
 * Enumeration representing different types of media files.
 */
@Getter
public enum MediaType {
    IMAGE("image", "Image file"),
    VIDEO("video", "Video file"),
    DOCUMENT("document", "Document file"),
    AUDIO("audio", "Audio file"),
    ARCHIVE("archive", "Archive file");

    private final String code;
    private final String description;

    MediaType(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static MediaType fromMimeType(String mimeType) {
        if (mimeType == null) {
            return DOCUMENT;
        }
        if (mimeType.startsWith("image/")) {
            return IMAGE;
        } else if (mimeType.startsWith("video/")) {
            return VIDEO;
        } else if (mimeType.startsWith("audio/")) {
            return AUDIO;
        } else if (mimeType.contains("zip") || mimeType.contains("rar") || mimeType.contains("tar") || mimeType.contains("gzip")) {
            return ARCHIVE;
        }
        return DOCUMENT;
    }

    public static MediaType fromCode(String code) {
        for (MediaType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown media type: " + code);
    }
}
