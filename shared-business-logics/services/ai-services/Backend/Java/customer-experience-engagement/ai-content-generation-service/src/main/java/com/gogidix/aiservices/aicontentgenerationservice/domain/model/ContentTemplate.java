package com.gogidix.aiservices.aicontentgenerationservice.domain.model;

import lombok.Builder;

import java.util.Map;
import java.util.Objects;

@Builder
public class ContentTemplate {
    private final String templateId;
    private final String name;
    private final ContentType contentType;
    private final String template;
    private final Map<String, String> defaultParameters;
    private final boolean isActive;

    public ContentTemplate(String templateId, String name, ContentType contentType,
                          String template, Map<String, String> defaultParameters, boolean isActive) {
        if (templateId == null || templateId.trim().isEmpty()) {
            throw new IllegalArgumentException("Template ID cannot be null or empty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Template name cannot be null or empty");
        }
        if (contentType == null) {
            throw new IllegalArgumentException("Content type cannot be null");
        }
        this.templateId = templateId;
        this.name = name;
        this.contentType = contentType;
        this.template = template;
        this.defaultParameters = defaultParameters;
        this.isActive = isActive;
    }

    public String getTemplateId() {
        return templateId;
    }

    public String getName() {
        return name;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public String getTemplate() {
        return template;
    }

    public Map<String, String> getDefaultParameters() {
        return defaultParameters;
    }

    public boolean isActive() {
        return isActive;
    }

    public String applyParameters(Map<String, String> parameters) {
        String result = template;
        if (defaultParameters != null) {
            for (Map.Entry<String, String> entry : defaultParameters.entrySet()) {
                result = result.replace("{{" + entry.getKey() + "}}",
                        parameters.getOrDefault(entry.getKey(), entry.getValue()));
            }
        }
        if (parameters != null) {
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                result = result.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContentTemplate that = (ContentTemplate) o;
        return Objects.equals(templateId, that.templateId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(templateId);
    }
}
