package com.gogidix.digitalmarketing.leadgeneration.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * LeadSource - Configuration for lead capture sources
 *
 * <p>Defines where leads come from and how they should be processed.</p>
 *
 * <p>Examples:</p>
 * <ul>
 *   <li>Website contact form</li>
 *   <li>Email campaign</li>
 *   <li>Social media platform</li>
 *   <li>Event registration</li>
 *   <li>Partner referral</li>
 * </ul>
 */
@Document(collection = "lead_sources")
@TypeAlias("lead_source")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "source_tenant_type_idx", def = "{'tenantId': 1, 'type': 1}")
@CompoundIndex(name = "source_tenant_active_idx", def = "{'tenantId': 1, 'active': 1}")
public class LeadSource extends BaseEntity {

    /**
     * Source name (e.g., "Website Contact Form", "LinkedIn Ads")
     */
    @Indexed
    private String name;

    /**
     * Source type (WEBSITE, EMAIL, SOCIAL_MEDIA, EVENT, PARTNER, PAID_AD, ORGANIC_SEARCH, DIRECT, WEBINAR, CONTENT_DOWNLOAD, OTHER)
     */
    @Indexed
    private String type;

    /**
     * Source description
     */
    private String description;

    /**
     * Whether this source is currently active
     */
    @Builder.Default
    private Boolean active = true;

    /**
     * Default lead score for this source
     */
    @Builder.Default
    private Integer defaultScore = 10;

    /**
     * Cost per lead (CPL) for this source
     */
    private BigDecimal costPerLead;

    /**
     * Monthly budget for this source
     */
    private BigDecimal monthlyBudget;

    /**
     * Currency code
     */
    private String currency;

    /**
     * Expected conversion rate (percentage)
     */
    private Double expectedConversionRate;

    /**
     * Source URL or endpoint
     */
    private String endpointUrl;

    /**
     * Webhook URL for notifications
     */
    private String webhookUrl;

    /**
     * API key for external integrations
     */
    private String apiKey;

    /**
     * Default campaign ID for this source
     */
    @Indexed
    private String defaultCampaignId;

    /**
     * Default assigned sales representative for leads from this source
     */
    private String defaultSalesRepId;

    /**
     * Assignment priority (1-10, higher = more priority)
     */
    @Builder.Default
    private Integer assignmentPriority = 5;

    /**
     * Auto-qualification enabled
     */
    @Builder.Default
    private Boolean autoQualify = false;

    /**
     * Qualification threshold for this source
     */
    private Integer qualificationThreshold;

    /**
     * Lead capture form configuration
     */
    private Map<String, Object> formConfig;

    /**
     * Field mappings for incoming data
     */
    private Map<String, String> fieldMappings;

    /**
     * Validation rules for this source
     */
    private Map<String, Object> validationRules;

    /**
     * Processing rules for this source
     */
    private Map<String, Object> processingRules;

    /**
     * Total leads captured from this source
     */
    @Builder.Default
    private Long totalLeads = 0L;

    /**
     * Qualified leads from this source
     */
    @Builder.Default
    private Long qualifiedLeads = 0L;

    /**
     * Converted leads from this source
     */
    @Builder.Default
    private Long convertedLeads = 0L;

    /**
     * Last lead capture timestamp
     */
    private Instant lastLeadAt;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Create a new LeadSource for a tenant.
     *
     * @param tenantId the tenant ID
     * @param name the source name
     * @param type the source type
     */
    public LeadSource(String tenantId, String name, String type) {
        super(tenantId);
        this.name = name;
        this.type = type;
        this.active = true;
        this.defaultScore = 10;
        this.assignmentPriority = 5;
        this.autoQualify = false;
        this.totalLeads = 0L;
        this.qualifiedLeads = 0L;
        this.convertedLeads = 0L;
        this.formConfig = new HashMap<>();
        this.fieldMappings = new HashMap<>();
        this.validationRules = new HashMap<>();
        this.processingRules = new HashMap<>();
        this.metadata = new HashMap<>();
    }

    /**
     * Check if source is active.
     *
     * @return true if source is active
     */
    public boolean isActive() {
        return this.active != null && this.active;
    }

    /**
     * Calculate conversion rate.
     *
     * @return conversion rate as percentage
     */
    public double calculateConversionRate() {
        if (this.totalLeads == null || this.totalLeads == 0) {
            return 0.0;
        }
        long converted = this.convertedLeads != null ? this.convertedLeads : 0;
        return (double) converted / this.totalLeads * 100;
    }

    /**
     * Calculate qualification rate.
     *
     * @return qualification rate as percentage
     */
    public double calculateQualificationRate() {
        if (this.totalLeads == null || this.totalLeads == 0) {
            return 0.0;
        }
        long qualified = this.qualifiedLeads != null ? this.qualifiedLeads : 0;
        return (double) qualified / this.totalLeads * 100;
    }

    /**
     * Calculate ROI (Return on Investment).
     *
     * @return ROI percentage or 0 if budget not set
     */
    public double calculateROI() {
        if (this.monthlyBudget == null || this.monthlyBudget.compareTo(BigDecimal.ZERO) == 0) {
            return 0.0;
        }
        double costPerLead = this.costPerLead != null ? this.costPerLead.doubleValue() : 0;
        long totalLeads = this.totalLeads != null ? this.totalLeads : 0;
        double totalCost = costPerLead * totalLeads;
        if (totalCost == 0) {
            return 0.0;
        }
        double value = this.convertedLeads != null ? this.convertedLeads * 1000 : 0;
        return ((value - totalCost) / totalCost) * 100;
    }

    /**
     * Increment lead counters.
     */
    public void incrementLeadCounters() {
        this.totalLeads = (this.totalLeads != null ? this.totalLeads : 0) + 1;
        this.lastLeadAt = Instant.now();
        this.touch();
    }

    /**
     * Increment qualified counter.
     */
    public void incrementQualifiedCounters() {
        this.qualifiedLeads = (this.qualifiedLeads != null ? this.qualifiedLeads : 0) + 1;
        this.touch();
    }

    /**
     * Increment converted counter.
     */
    public void incrementConvertedCounters() {
        this.convertedLeads = (this.convertedLeads != null ? this.convertedLeads : 0) + 1;
        this.touch();
    }

    /**
     * Activate this source.
     */
    public void activate() {
        this.active = true;
        this.touch();
    }

    /**
     * Deactivate this source.
     */
    public void deactivate() {
        this.active = false;
        this.touch();
    }

    /**
     * Add form configuration.
     *
     * @param key the config key
     * @param value the config value
     */
    public void addFormConfig(String key, Object value) {
        if (this.formConfig == null) {
            this.formConfig = new HashMap<>();
        }
        this.formConfig.put(key, value);
    }

    /**
     * Add field mapping.
     *
     * @param sourceField the source field name
     * @param targetField the target field name
     */
    public void addFieldMapping(String sourceField, String targetField) {
        if (this.fieldMappings == null) {
            this.fieldMappings = new HashMap<>();
        }
        this.fieldMappings.put(sourceField, targetField);
    }

    /**
     * Add validation rule.
     *
     * @param field the field name
     * @param rule the validation rule
     */
    public void addValidationRule(String field, Object rule) {
        if (this.validationRules == null) {
            this.validationRules = new HashMap<>();
        }
        this.validationRules.put(field, rule);
    }

    /**
     * Add processing rule.
     *
     * @param key the rule key
     * @param value the rule value
     */
    public void addProcessingRule(String key, Object value) {
        if (this.processingRules == null) {
            this.processingRules = new HashMap<>();
        }
        this.processingRules.put(key, value);
    }

    /**
     * Add metadata.
     *
     * @param key the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Get metadata value.
     *
     * @param key the metadata key
     * @return the metadata value, or null if not set
     */
    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }
}
