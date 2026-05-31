package com.gogidix.sales.communication.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Message Content Value Object
 * Represents the content of a message
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageContent {

    private String subject;
    private String plainText;
    private String htmlContent;
    private String templateId;
    private Map<String, Object> templateVariables;
    private List<AttachmentInfo> attachments;

    /**
     * Validates if the content is valid
     */
    public boolean isValid() {
        return (plainText != null && !plainText.isBlank()) ||
                (htmlContent != null && !htmlContent.isBlank()) ||
                (templateId != null && !templateId.isBlank());
    }

    /**
     * Gets the display text
     */
    public String getDisplayText() {
        if (plainText != null && !plainText.isBlank()) {
            return plainText;
        }
        if (htmlContent != null && !htmlContent.isBlank()) {
            // Strip HTML tags for plain text display
            return htmlContent.replaceAll("<[^>]*>", "");
        }
        return "[Template Message]";
    }

    /**
     * Checks if content exceeds maximum length
     */
    public boolean exceedsMaxLength(int maxLength) {
        int length = 0;
        if (plainText != null) length += plainText.length();
        if (htmlContent != null) length += htmlContent.length();
        return length > maxLength;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentInfo {
        private String fileName;
        private String fileType;
        private Long fileSize;
        private String fileUrl;
    }
}
