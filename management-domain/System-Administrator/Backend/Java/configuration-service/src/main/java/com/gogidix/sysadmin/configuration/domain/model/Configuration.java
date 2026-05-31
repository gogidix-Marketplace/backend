package com.gogidix.sysadmin.configuration.domain.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Document(collection = "configurations")
public class Configuration {

    @Id
    private String id;
    @Indexed
    private String tenantId;
    private String name;
    private String description;
    private ConfigType type;
    private ConfigScope scope;
    private String scopeId;
    private Map<String, Object> values;
    private Boolean isEncrypted;
    private Boolean isRequired;
    private String dataType;
    private List<String> allowedValues;
    private String validationRegex;
    private Object defaultValue;
    private ConfigStatus status;
    private String version;
    private Integer versionNumber;
    private String parentConfigId;
    private List<String> dependentConfigIds;
    private String environment;
    private String component;
    private String owner;
    private String lastModifiedBy;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant effectiveFrom;
    private Instant effectiveTo;
    private Map<String, String> tags;

    public enum ConfigType {
        STRING, NUMBER, BOOLEAN, JSON, ENCRYPTED, FILE, CERTIFICATE
    }

    public enum ConfigScope {
        GLOBAL, TENANT, ENVIRONMENT, SERVICE, INSTANCE, USER
    }

    public enum ConfigStatus {
        ACTIVE, INACTIVE, DRAFT, DEPRECATED
    }

    public Configuration() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.status = ConfigStatus.ACTIVE;
        this.versionNumber = 1;
        this.isEncrypted = false;
        this.isRequired = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ConfigType getType() { return type; }
    public void setType(ConfigType type) { this.type = type; }

    public ConfigScope getScope() { return scope; }
    public void setScope(ConfigScope scope) { this.scope = scope; }

    public String getScopeId() { return scopeId; }
    public void setScopeId(String scopeId) { this.scopeId = scopeId; }

    public Map<String, Object> getValues() { return values; }
    public void setValues(Map<String, Object> values) { this.values = values; }

    public Boolean getIsEncrypted() { return isEncrypted; }
    public void setIsEncrypted(Boolean isEncrypted) { this.isEncrypted = isEncrypted; }

    public Boolean getIsRequired() { return isRequired; }
    public void setIsRequired(Boolean isRequired) { this.isRequired = isRequired; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) { this.dataType = dataType; }

    public List<String> getAllowedValues() { return allowedValues; }
    public void setAllowedValues(List<String> allowedValues) { this.allowedValues = allowedValues; }

    public String getValidationRegex() { return validationRegex; }
    public void setValidationRegex(String validationRegex) { this.validationRegex = validationRegex; }

    public Object getDefaultValue() { return defaultValue; }
    public void setDefaultValue(Object defaultValue) { this.defaultValue = defaultValue; }

    public ConfigStatus getStatus() { return status; }
    public void setStatus(ConfigStatus status) { this.status = status; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public Integer getVersionNumber() { return versionNumber; }
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }

    public String getParentConfigId() { return parentConfigId; }
    public void setParentConfigId(String parentConfigId) { this.parentConfigId = parentConfigId; }

    public List<String> getDependentConfigIds() { return dependentConfigIds; }
    public void setDependentConfigIds(List<String> dependentConfigIds) { this.dependentConfigIds = dependentConfigIds; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public String getComponent() { return component; }
    public void setComponent(String component) { this.component = component; }

    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }

    public String getLastModifiedBy() { return lastModifiedBy; }
    public void setLastModifiedBy(String lastModifiedBy) { this.lastModifiedBy = lastModifiedBy; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public Instant getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(Instant effectiveFrom) { this.effectiveFrom = effectiveFrom; }

    public Instant getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(Instant effectiveTo) { this.effectiveTo = effectiveTo; }

    public Map<String, String> getTags() { return tags; }
    public void setTags(Map<String, String> tags) { this.tags = tags; }

    public void activate() {
        this.status = ConfigStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.status = ConfigStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deprecate() {
        this.status = ConfigStatus.DEPRECATED;
        this.updatedAt = Instant.now();
    }

    public void incrementVersion() {
        this.versionNumber++;
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Configuration configuration = (Configuration) o;
        return Objects.equals(id, configuration.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
