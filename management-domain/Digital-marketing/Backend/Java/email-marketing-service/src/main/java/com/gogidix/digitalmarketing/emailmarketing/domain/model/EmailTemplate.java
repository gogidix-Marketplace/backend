package com.gogidix.digitalmarketing.emailmarketing.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EmailTemplate - Reusable email template with dynamic content support.
 *
 * <p>Templates define the structure and design of emails. They support
 * variable substitution and can be reused across multiple campaigns.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "email_templates")
@TypeAlias("email_template")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "template_tenant_category_idx", def = "{'tenantId': 1, 'category': 1, 'name': 1}")
@CompoundIndex(name = "template_tenant_active_idx", def = "{'tenantId': 1, 'isActive': 1, 'name': 1}")
public class EmailTemplate extends BaseEntity {

    /**
     * Template name (e.g., "Welcome Email", "Newsletter Template")
     */
    @Indexed
    private String name;

    /**
     * Template description
     */
    private String description;

    /**
     * Template category (TRANSACTIONAL, MARKETING, ANNOUNCEMENT, NEWSLETTER)
     */
    @Indexed
    private String category;

    /**
     * Template subject line (can include variables like {{firstName}})
     */
    private String subject;

    /**
     * Preheader text
     */
    private String preheader;

    /**
     * HTML content of the template
     */
    private String htmlContent;

    /**
     * Plain text content
     */
    private String textContent;

    /**
     * Template type (HTML, TEXT, HTML_AND_TEXT)
     */
    private String templateType;

    /**
     * Template format (THYMELEAF, FREEMARKER, HANDLEBARS, RAW)
     */
    private String format;

    /**
     * Variable definitions for template substitution
     */
    private List<TemplateVariable> variables;

    /**
     * CSS styles (embedded)
     */
    private String cssStyles;

    /**
     * Template version
     */
    private String version;

    /**
     * Parent template ID (for inheritance)
     */
    @Indexed
    private String parentTemplateId;

    /**
     * Template tags
     */
    private List<String> tags;

    /**
     * Template thumbnail URL
     */
    private String thumbnailUrl;

    /**
     * Preview HTML
     */
    private String previewHtml;

    /**
     * Whether this template is active
     */
        @Indexed
    private Boolean isActive = true;

    /**
     * Whether this template is a system template (read-only)
     */
        private Boolean isSystem = false;

    /**
     * Whether this is a default template
     */
        private Boolean isDefault = false;

    /**
     * Template locale (for multilingual templates)
     */
    private String locale;

    /**
     * Mobile responsive flag
     */
        private Boolean isResponsive = true;

    /**
     * Width of the template (in pixels)
     */
    private Integer width;

    /**
     * Background color
     */
    private String backgroundColor;

    /**
     * Font family
     */
    private String fontFamily;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Usage count (how many campaigns use this template)
     */
    private Integer usageCount;

    /**
     * Last used date
     */
    private Instant lastUsedAt;

    /**
     * Editor used to create this template
     */
    private String createdByEditor;

    /**
     * Template sections (for modular templates)
     */
    private List<TemplateSection> sections;

    /**
     * Template variables inner class
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateVariable {
        private String name;
        private String type;
        private String defaultValue;
        private String description;
        private Boolean required;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Boolean getRequired() { return required; }
        public void setRequired(Boolean required) { this.required = required; }
    }

    /**
     * Template section inner class
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateSection {
        private String name;
        private String content;
        private Integer order;
        private Boolean editable;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public Integer getOrder() { return order; }
        public void setOrder(Integer order) { this.order = order; }
        public Boolean getEditable() { return editable; }
        public void setEditable(Boolean editable) { this.editable = editable; }
    }

    /**
     * Create a new template for a tenant.
     *
     * @param tenantId    the tenant ID
     * @param name        the template name
     * @param htmlContent the HTML content
     */
    public EmailTemplate(String tenantId, String name, String htmlContent) {
        super(tenantId);
        this.name = name;
        this.htmlContent = htmlContent;
        this.isActive = true;
        this.isResponsive = true;
        this.templateType = "HTML";
        this.format = "RAW";
        this.variables = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.sections = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.usageCount = 0;
    }

    /**
     * Create a new template with category.
     *
     * @param tenantId    the tenant ID
     * @param name        the template name
     * @param category    the category
     * @param htmlContent the HTML content
     */
    public EmailTemplate(String tenantId, String name, String category, String htmlContent) {
        this(tenantId, name, htmlContent);
        this.category = category;
    }

    /**
     * Check if template is active.
     *
     * @return true if active
     */
    public boolean isActiveTemplate() {
        return Boolean.TRUE.equals(this.isActive);
    }

    /**
     * Check if template is a system template.
     *
     * @return true if system template
     */
    public boolean isSystemTemplate() {
        return Boolean.TRUE.equals(this.isSystem);
    }

    /**
     * Check if template is editable.
     *
     * @return true if not a system template
     */
    public boolean isEditable() {
        return !Boolean.TRUE.equals(this.isSystem);
    }

    /**
     * Check if template supports variables.
     *
     * @return true if has variables
     */
    public boolean hasVariables() {
        return this.variables != null && !this.variables.isEmpty();
    }

    /**
     * Get a variable by name.
     *
     * @param name the variable name
     * @return the variable, or null if not found
     */
    public TemplateVariable getVariable(String name) {
        if (this.variables == null) {
            return null;
        }
        return this.variables.stream()
            .filter(v -> name.equals(v.getName()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Add a variable.
     *
     * @param variable the variable to add
     */
    public void addVariable(TemplateVariable variable) {
        if (this.variables == null) {
            this.variables = new ArrayList<>();
        }
        this.variables.add(variable);
    }

    /**
     * Add a simple variable.
     *
     * @param name         the variable name
     * @param type         the variable type
     * @param defaultValue the default value
     */
    public void addVariable(String name, String type, String defaultValue) {
        TemplateVariable variable = new TemplateVariable();
        variable.setName(name);
        variable.setType(type);
        variable.setDefaultValue(defaultValue);
        variable.setRequired(false);
        addVariable(variable);
    }

    /**
     * Add a tag.
     *
     * @param tag the tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Add a section.
     *
     * @param section the section to add
     */
    public void addSection(TemplateSection section) {
        if (this.sections == null) {
            this.sections = new ArrayList<>();
        }
        this.sections.add(section);
    }

    /**
     * Get sections ordered by order field.
     *
     * @return ordered list of sections
     */
    public List<TemplateSection> getOrderedSections() {
        if (this.sections == null) {
            return new ArrayList<>();
        }
        return this.sections.stream()
            .sorted((a, b) -> {
                int orderA = a.getOrder() != null ? a.getOrder() : 0;
                int orderB = b.getOrder() != null ? b.getOrder() : 0;
                return Integer.compare(orderA, orderB);
            })
            .toList();
    }

    /**
     * Increment usage count.
     */
    public void incrementUsage() {
        this.usageCount = (this.usageCount != null ? this.usageCount : 0) + 1;
        this.lastUsedAt = Instant.now();
        this.touch();
    }

    /**
     * Add metadata.
     *
     * @param key   the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Get variable names.
     *
     * @return list of variable names
     */
    public List<String> getVariableNames() {
        if (this.variables == null) {
            return new ArrayList<>();
        }
        return this.variables.stream()
            .map(TemplateVariable::getName)
            .toList();
    }

    /**
     * Check if template has required variables.
     *
     * @return list of required variable names
     */
    public List<String> getRequiredVariableNames() {
        if (this.variables == null) {
            return new ArrayList<>();
        }
        return this.variables.stream()
            .filter(v -> Boolean.TRUE.equals(v.getRequired()))
            .map(TemplateVariable::getName)
            .toList();
    }

    /**
     * Deactivate template.
     */
    public void deactivate() {
        this.isActive = false;
        this.touch();
    }

    /**
     * Activate template.
     */
    public void activate() {
        this.isActive = true;
        this.touch();
    }

    /**
     * Create a copy of this template.
     *
     * @param newName the name for the copy
     * @return a new template with copied content
     */
    public EmailTemplate createCopy(String newName) {
        EmailTemplate copy = new EmailTemplate(this.getTenantId(), newName, this.htmlContent);
        copy.category = this.category;
        copy.subject = this.subject;
        copy.preheader = this.preheader;
        copy.textContent = this.textContent;
        copy.templateType = this.templateType;
        copy.format = this.format;
        copy.variables = this.variables != null ? new ArrayList<>(this.variables) : new ArrayList<>();
        copy.cssStyles = this.cssStyles;
        copy.parentTemplateId = this.getId();
        copy.tags = this.tags != null ? new ArrayList<>(this.tags) : new ArrayList<>();
        copy.isResponsive = this.isResponsive;
        copy.width = this.width;
        copy.backgroundColor = this.backgroundColor;
        copy.fontFamily = this.fontFamily;
        copy.locale = this.locale;
        copy.sections = this.sections != null ? new ArrayList<>(this.sections) : new ArrayList<>();
        copy.isSystem = false;
        copy.isDefault = false;
        copy.isActive = true;
        return copy;
    }

    /**
     * Validate template content.
     *
     * @return true if template has valid content
     */
    public boolean isValid() {
        return htmlContent != null && !htmlContent.isBlank() &&
            ("HTML_AND_TEXT".equals(templateType) || (textContent != null && !textContent.isBlank()));
    }

    /**
     * Get template as HTML with variable substitution.
     *
     * @param variables the variable values
     * @return the rendered HTML
     */
    public String renderHtml(Map<String, Object> variables) {
        String rendered = this.htmlContent;
        if (variables != null && this.variables != null) {
            for (TemplateVariable var : this.variables) {
                String name = var.getName();
                Object value = variables.get(name);
                if (value == null && var.getDefaultValue() != null) {
                    value = var.getDefaultValue();
                }
                if (value != null) {
                    rendered = rendered.replace("{{" + name + "}}", String.valueOf(value));
                    rendered = rendered.replace("${" + name + "}", String.valueOf(value));
                }
            }
        }
        return rendered;
    }
}
