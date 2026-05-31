package com.gogidix.hr.employeeselfservice.domain.model;

import com.gogidix.hr.employeeselfservice.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Quick Action Domain Entity
 * Multi-tenant quick action configuration for employee self-service dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "quick_actions")
public class QuickAction extends BaseEntity {

    private String tenantId;

    private String actionCode;

    private String actionName;

    private String description;

    private ActionCategory category;

    private String route;

    private String icon;

    private String iconType;

    private Boolean enabled = true;

    private Integer displayOrder;

    private String countryCode;

    private List<String> allowedRoles = new ArrayList<>();

    private List<String> requiredPermissions = new ArrayList<>();

    private Map<String, Object> metadata = new HashMap<>();

    private String backgroundColor;

    private String textColor;

    private Boolean requiresApproval;

    private String target;

    private String badge;

    private String externalLink;

    private Boolean isNew;

    private Boolean isFeatured;

    private LocalDate validFrom;

    private LocalDate validUntil;

    private String parentActionCode;

    private List<String> dependencies = new ArrayList<>();

    public enum ActionCategory {
        PERSONAL,
        PAYROLL,
        BENEFITS,
        LEAVE,
        DOCUMENTS,
        TAX,
        TRAINING,
        PERFORMANCE,
        RECRUITMENT,
        TIME_TRACKING,
        EXPENSES,
        REPORTS,
        SETTINGS,
        COMMUNICATION
    }

    /**
     * Creates a new quick action
     */
    public static QuickAction create(String tenantId, String actionCode, String actionName,
                                      ActionCategory category, String route, String icon,
                                      Integer displayOrder) {
        QuickAction action = new QuickAction();
        action.setTenantId(tenantId);
        action.setActionCode(actionCode);
        action.setActionName(actionName);
        action.setCategory(category);
        action.setRoute(route);
        action.setIcon(icon);
        action.setEnabled(true);
        action.setDisplayOrder(displayOrder);
        action.setAllowedRoles(new ArrayList<>());
        action.setRequiredPermissions(new ArrayList<>());
        action.setMetadata(new HashMap<>());
        action.setDependencies(new ArrayList<>());
        action.setIsNew(false);
        action.setIsFeatured(false);
        action.setTarget("_self");
        action.setIconType("material");
        return action;
    }

    /**
     * Enables the quick action
     */
    public void enable() {
        this.enabled = true;
    }

    /**
     * Disables the quick action
     */
    public void disable() {
        this.enabled = false;
    }

    /**
     * Checks if the quick action is enabled
     */
    public boolean isEnabled() {
        if (!Boolean.TRUE.equals(this.enabled)) {
            return false;
        }
        LocalDate now = LocalDate.now();
        if (validFrom != null && now.isBefore(validFrom)) {
            return false;
        }
        if (validUntil != null && now.isAfter(validUntil)) {
            return false;
        }
        return true;
    }

    /**
     * Updates the quick action details
     */
    public void updateDetails(String actionName, String description, String route,
                               String icon, ActionCategory category) {
        if (actionName != null && !actionName.isBlank()) {
            this.actionName = actionName;
        }
        this.description = description;
        this.route = route;
        this.icon = icon;
        if (category != null) {
            this.category = category;
        }
    }

    /**
     * Sets the display order
     */
    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * Adds an allowed role
     */
    public void addAllowedRole(String role) {
        if (this.allowedRoles == null) {
            this.allowedRoles = new ArrayList<>();
        }
        if (!this.allowedRoles.contains(role)) {
            this.allowedRoles.add(role);
        }
    }

    /**
     * Removes an allowed role
     */
    public void removeAllowedRole(String role) {
        if (this.allowedRoles != null) {
            this.allowedRoles.remove(role);
        }
    }

    /**
     * Checks if a role is allowed
     */
    public boolean isRoleAllowed(String role) {
        if (this.allowedRoles == null || this.allowedRoles.isEmpty()) {
            return true;
        }
        return this.allowedRoles.contains(role) || this.allowedRoles.contains("*");
    }

    /**
     * Adds a required permission
     */
    public void addRequiredPermission(String permission) {
        if (this.requiredPermissions == null) {
            this.requiredPermissions = new ArrayList<>();
        }
        if (!this.requiredPermissions.contains(permission)) {
            this.requiredPermissions.add(permission);
        }
    }

    /**
     * Removes a required permission
     */
    public void removeRequiredPermission(String permission) {
        if (this.requiredPermissions != null) {
            this.requiredPermissions.remove(permission);
        }
    }

    /**
     * Checks if user has all required permissions
     */
    public boolean hasRequiredPermissions(List<String> userPermissions) {
        if (this.requiredPermissions == null || this.requiredPermissions.isEmpty()) {
            return true;
        }
        return userPermissions != null && userPermissions.containsAll(this.requiredPermissions);
    }

    /**
     * Sets the country code for localization
     */
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    /**
     * Sets styling information
     */
    public void setStyling(String backgroundColor, String textColor) {
        this.backgroundColor = backgroundColor;
        this.textColor = textColor;
    }

    /**
     * Sets the badge text
     */
    public void setBadge(String badge) {
        this.badge = badge;
    }

    /**
     * Marks as new
     */
    public void markAsNew(boolean isNew) {
        this.isNew = isNew;
    }

    /**
     * Marks as featured
     */
    public void markAsFeatured(boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    /**
     * Sets the target (window opening behavior)
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Sets external link
     */
    public void setExternalLink(String externalLink) {
        this.externalLink = externalLink;
    }

    /**
     * Checks if this is an external link
     */
    public boolean isExternalLink() {
        return this.externalLink != null && !this.externalLink.isBlank();
    }

    /**
     * Sets validity period
     */
    public void setValidityPeriod(LocalDate validFrom, LocalDate validUntil) {
        this.validFrom = validFrom;
        this.validUntil = validUntil;
    }

    /**
     * Sets parent action (for sub-actions)
     */
    public void setParentAction(String parentActionCode) {
        this.parentActionCode = parentActionCode;
    }

    /**
     * Adds a dependency
     */
    public void addDependency(String dependencyActionCode) {
        if (this.dependencies == null) {
            this.dependencies = new ArrayList<>();
        }
        if (!this.dependencies.contains(dependencyActionCode)) {
            this.dependencies.add(dependencyActionCode);
        }
    }

    /**
     * Removes a dependency
     */
    public void removeDependency(String dependencyActionCode) {
        if (this.dependencies != null) {
            this.dependencies.remove(dependencyActionCode);
        }
    }

    /**
     * Checks if all dependencies are satisfied
     */
    public boolean areDependenciesSatisfied(List<String> availableActions) {
        if (this.dependencies == null || this.dependencies.isEmpty()) {
            return true;
        }
        return availableActions != null && availableActions.containsAll(this.dependencies);
    }

    /**
     * Adds metadata
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Gets metadata value
     */
    public Object getMetadata(String key) {
        if (this.metadata != null) {
            return this.metadata.get(key);
        }
        return null;
    }

    /**
     * Sets the icon type
     */
    public void setIconType(String iconType) {
        this.iconType = iconType;
    }

    /**
     * Checks if this action requires approval
     */
    public boolean requiresApproval() {
        return Boolean.TRUE.equals(this.requiresApproval);
    }

    /**
     * Sets whether approval is required
     */
    public void setRequiresApproval(Boolean requiresApproval) {
        this.requiresApproval = requiresApproval;
    }

    /**
     * Gets the full route (considering external links)
     */
    public String getFullRoute() {
        if (isExternalLink()) {
            return this.externalLink;
        }
        return this.route;
    }

    /**
     * Clones this quick action for a new tenant
     */
    public QuickAction cloneForTenant(String newTenantId) {
        QuickAction cloned = new QuickAction();
        cloned.setTenantId(newTenantId);
        cloned.setActionCode(this.actionCode);
        cloned.setActionName(this.actionName);
        cloned.setDescription(this.description);
        cloned.setCategory(this.category);
        cloned.setRoute(this.route);
        cloned.setIcon(this.icon);
        cloned.setIconType(this.iconType);
        cloned.setEnabled(this.enabled);
        cloned.setDisplayOrder(this.displayOrder);
        cloned.setCountryCode(this.countryCode);
        cloned.setAllowedRoles(this.allowedRoles != null ? new ArrayList<>(this.allowedRoles) : new ArrayList<>());
        cloned.setRequiredPermissions(this.requiredPermissions != null ? new ArrayList<>(this.requiredPermissions) : new ArrayList<>());
        cloned.setMetadata(this.metadata != null ? new HashMap<>(this.metadata) : new HashMap<>());
        cloned.setBackgroundColor(this.backgroundColor);
        cloned.setTextColor(this.textColor);
        cloned.setRequiresApproval(this.requiresApproval);
        cloned.setTarget(this.target);
        cloned.setBadge(this.badge);
        cloned.setExternalLink(this.externalLink);
        cloned.setIsNew(this.isNew);
        cloned.setIsFeatured(this.isFeatured);
        cloned.setValidFrom(this.validFrom);
        cloned.setValidUntil(this.validUntil);
        cloned.setParentActionCode(this.parentActionCode);
        cloned.setDependencies(this.dependencies != null ? new ArrayList<>(this.dependencies) : new ArrayList<>());
        return cloned;
    }
}
