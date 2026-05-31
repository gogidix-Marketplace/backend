package com.gogidix.hr.documentmanagement.domain.model;

import com.gogidix.hr.documentmanagement.shared.base.BaseEntity;
import com.gogidix.hr.documentmanagement.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Document Template Domain Entity
 * Represents reusable document templates
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "document_templates")
public class DocumentTemplate extends BaseEntity {

    @Indexed(unique = true)
    private String templateId;

    @Indexed
    private String tenantId;

    private String templateName;
    private String templateCode;

    @Indexed
    private DocumentType documentType;

    @Indexed
    private DocumentCategory category;

    @Indexed
    private String countryCode;

    private String description;
    private String storagePath;
    private StorageProvider storageProvider;

    @Indexed
    private Boolean active;

    private List<TemplateField> fields;

    private String createdBy;

    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;

    private Integer version = 1;

    /**
     * Creates a new document template
     */
    public static DocumentTemplate create(String tenantId, String templateName, String templateCode,
                                          DocumentType documentType, DocumentCategory category,
                                          String countryCode, String storagePath, String createdBy,
                                          List<TemplateField> fields) {
        String templateId = generateTemplateId(templateCode, countryCode);

        DocumentTemplate template = new DocumentTemplate();
        template.setTenantId(tenantId);
        template.setTemplateId(templateId);
        template.setTemplateName(templateName);
        template.setTemplateCode(templateCode);
        template.setDocumentType(documentType);
        template.setCategory(category);
        template.setCountryCode(countryCode);
        template.setStoragePath(storagePath);
        template.setStorageProvider(StorageProvider.LOCAL);
        template.setActive(true);
        template.setFields(fields != null ? fields : new ArrayList<>());
        template.setCreatedBy(createdBy);
        template.setEffectiveFrom(LocalDate.now());
        template.setVersion(1);

        template.validate();

        return template;
    }

    /**
     * Activates the template
     */
    public void activate() {
        if (this.effectiveTo != null && LocalDate.now().isAfter(this.effectiveTo)) {
            throw new IllegalStateException("Cannot activate expired template");
        }

        this.active = true;
        if (this.effectiveFrom == null || LocalDate.now().isBefore(this.effectiveFrom)) {
            this.effectiveFrom = LocalDate.now();
        }
    }

    /**
     * Deactivates the template
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Updates template fields
     */
    public void updateFields(List<TemplateField> newFields) {
        if (newFields != null) {
            this.fields = newFields;
        }
    }

    /**
     * Adds a field to the template
     */
    public void addField(TemplateField field) {
        if (this.fields == null) {
            this.fields = new ArrayList<>();
        }
        this.fields.add(field);
    }

    /**
     * Removes a field from the template
     */
    public void removeField(String fieldId) {
        if (this.fields != null) {
            this.fields.removeIf(field -> field.getFieldId().equals(fieldId));
        }
    }

    /**
     * Checks if template is currently effective
     */
    public boolean isEffective() {
        LocalDate now = LocalDate.now();

        if (effectiveFrom != null && now.isBefore(effectiveFrom)) {
            return false;
        }

        if (effectiveTo != null && now.isAfter(effectiveTo)) {
            return false;
        }

        return true;
    }

    /**
     * Checks if template is expired
     */
    public boolean isExpired() {
        return effectiveTo != null && LocalDate.now().isAfter(effectiveTo);
    }

    /**
     * Sets effective period
     */
    public void setEffectivePeriod(LocalDate from, LocalDate to) {
        if (from != null && to != null && from.isAfter(to)) {
            throw new ValidationException("effectiveFrom", "Effective from date must be before effective to date");
        }

        this.effectiveFrom = from;
        this.effectiveTo = to;
    }

    /**
     * Gets field by ID
     */
    public TemplateField getField(String fieldId) {
        if (this.fields == null) {
            return null;
        }
        return this.fields.stream()
                .filter(field -> field.getFieldId().equals(fieldId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Gets required fields
     */
    public List<TemplateField> getRequiredFields() {
        if (this.fields == null) {
            return new ArrayList<>();
        }
        return this.fields.stream()
                .filter(TemplateField::getRequired)
                .toList();
    }

    /**
     * Gets optional fields
     */
    public List<TemplateField> getOptionalFields() {
        if (this.fields == null) {
            return new ArrayList<>();
        }
        return this.fields.stream()
                .filter(field -> !field.getRequired())
                .toList();
    }

    /**
     * Creates a new version of the template
     */
    public DocumentTemplate createNewVersion(String updatedBy) {
        DocumentTemplate newVersion = new DocumentTemplate();
        newVersion.setTenantId(this.tenantId);
        newVersion.setTemplateId(generateTemplateId(this.templateCode, this.countryCode));
        newVersion.setTemplateName(this.templateName);
        newVersion.setTemplateCode(this.templateCode);
        newVersion.setDocumentType(this.documentType);
        newVersion.setCategory(this.category);
        newVersion.setCountryCode(this.countryCode);
        newVersion.setDescription(this.description);
        newVersion.setStoragePath(this.storagePath);
        newVersion.setStorageProvider(this.storageProvider);
        newVersion.setActive(false);
        newVersion.setFields(new ArrayList<>(this.fields));
        newVersion.setCreatedBy(updatedBy);
        newVersion.setEffectiveFrom(LocalDate.now());
        newVersion.setVersion(this.version + 1);

        // Deactivate old version if new one is activated
        if (this.active) {
            this.active = false;
        }

        return newVersion;
    }

    /**
     * Validates template data
     */
    public void validate() {
        if (this.templateName == null || this.templateName.isBlank()) {
            throw new ValidationException("templateName", "Template name is required");
        }
        if (this.templateCode == null || this.templateCode.isBlank()) {
            throw new ValidationException("templateCode", "Template code is required");
        }
        if (this.documentType == null) {
            throw new ValidationException("documentType", "Document type is required");
        }
        if (this.category == null) {
            throw new ValidationException("category", "Category is required");
        }
        if (this.countryCode == null || this.countryCode.isBlank()) {
            throw new ValidationException("countryCode", "Country code is required");
        }
        if (this.storagePath == null || this.storagePath.isBlank()) {
            throw new ValidationException("storagePath", "Storage path is required");
        }

        if (effectiveFrom != null && effectiveTo != null && effectiveFrom.isAfter(effectiveTo)) {
            throw new ValidationException("effectivePeriod", "Effective from date must be before effective to date");
        }
    }

    /**
     * Checks if template matches criteria
     */
    public boolean matches(DocumentType documentType, DocumentCategory category, String countryCode) {
        return this.documentType == documentType &&
               this.category == category &&
               this.countryCode.equals(countryCode) &&
               this.active &&
               isEffective();
    }

    private static String generateTemplateId(String templateCode, String countryCode) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        return "TPL-" + countryCode.toUpperCase() + "-" + templateCode.toUpperCase() + "-" + timestamp.substring(timestamp.length() - 6);
    }

    /**
     * Inner class for template fields
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TemplateField {
        private String fieldId;
        private String fieldName;
        private String fieldType; // TEXT, NUMBER, DATE, SELECT, CHECKBOX, TEXTAREA, FILE
        private String label;
        private String placeholder;
        private Boolean required;
        private String defaultValue;
        private List<String> options; // For SELECT type
        private Integer order;
        private String validation; // Regex or validation rule
        private String helpText;
        private Integer maxLength;
        private Boolean readOnly;
    }
}
