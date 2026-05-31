package com.gogidix.shared.model.domain.model.user.enums;

/**
 * Enumeration of user roles across the entire GOGIDX ecosystem.
 * Defines permissions and access levels for different types of users.
 */
public enum UserRole {
    
    // Customer Roles
    /**
     * Standard customer with basic shopping privileges.
     */
    CUSTOMER("Customer", "Standard customer with basic privileges", 1),
    
    /**
     * Premium customer with enhanced features and support.
     */
    PREMIUM_USER("Premium User", "Premium customer with enhanced features", 2),
    
    /**
     * VIP customer with exclusive access and personalized service.
     */
    VIP_CUSTOMER("VIP Customer", "VIP customer with exclusive privileges", 3),
    
    // Vendor Roles
    /**
     * Individual seller on the platform.
     */
    VENDOR("Vendor", "Individual seller on the platform", 10),
    
    /**
     * Enterprise vendor with bulk selling capabilities.
     */
    ENTERPRISE_VENDOR("Enterprise Vendor", "Large-scale vendor with enterprise features", 11),
    
    /**
     * Verified vendor with enhanced trust rating.
     */
    VERIFIED_VENDOR("Verified Vendor", "Vendor with verified business credentials", 12),
    
    // Logistics Roles
    /**
     * Courier for package delivery.
     */
    COURIER("Courier", "Package delivery personnel", 20),
    
    /**
     * Logistics coordinator managing shipments.
     */
    LOGISTICS_COORDINATOR("Logistics Coordinator", "Manages shipping and logistics", 21),
    
    /**
     * Warehouse operator managing inventory.
     */
    WAREHOUSE_OPERATOR("Warehouse Operator", "Manages warehouse operations", 22),
    
    /**
     * Fleet manager overseeing delivery vehicles.
     */
    FLEET_MANAGER("Fleet Manager", "Manages delivery fleet operations", 23),
    
    // Staff Roles
    /**
     * Customer support representative.
     */
    CUSTOMER_SUPPORT("Customer Support", "Handles customer inquiries and issues", 30),
    
    /**
     * Sales representative managing customer relationships.
     */
    SALES_REP("Sales Representative", "Manages customer sales and relationships", 31),
    
    /**
     * Marketing specialist managing campaigns.
     */
    MARKETING_SPECIALIST("Marketing Specialist", "Manages marketing campaigns and content", 32),
    
    /**
     * Business analyst with reporting access.
     */
    BUSINESS_ANALYST("Business Analyst", "Analyzes business data and generates reports", 33),
    
    // Manager Roles
    /**
     * Team supervisor with staff management capabilities.
     */
    SUPERVISOR("Supervisor", "Supervises team operations", 40),
    
    /**
     * Department manager with broader responsibilities.
     */
    MANAGER("Manager", "Manages department operations", 41),
    
    /**
     * Regional manager overseeing multiple locations.
     */
    REGIONAL_MANAGER("Regional Manager", "Manages regional operations", 42),
    
    /**
     * Operations director with executive responsibilities.
     */
    OPERATIONS_DIRECTOR("Operations Director", "Directs operational activities", 43),
    
    // Administrative Roles
    /**
     * System administrator with technical privileges.
     */
    ADMIN("Administrator", "System administrator with elevated privileges", 50),
    
    /**
     * Super administrator with highest system access.
     */
    SUPER_ADMIN("Super Administrator", "Highest level system administrator", 51),
    
    /**
     * Security administrator managing security policies.
     */
    SECURITY_ADMIN("Security Administrator", "Manages security policies and access", 52),
    
    /**
     * Compliance officer ensuring regulatory adherence.
     */
    COMPLIANCE_OFFICER("Compliance Officer", "Ensures regulatory compliance", 53),
    
    // Special Roles
    /**
     * API client for system integration.
     */
    API_CLIENT("API Client", "Automated system client with API access", 60),
    
    /**
     * Beta tester with access to new features.
     */
    BETA_TESTER("Beta Tester", "Tests new features before public release", 61),
    
    /**
     * Developer with development environment access.
     */
    DEVELOPER("Developer", "Development team member", 62),
    
    /**
     * External auditor with read-only access.
     */
    AUDITOR("Auditor", "External auditor with read-only access", 63),
    
    /**
     * Guest user with limited temporary access.
     */
    GUEST("Guest", "Temporary user with limited access", 70);
    
    private final String displayName;
    private final String description;
    private final int hierarchyLevel;
    
    UserRole(String displayName, String description, int hierarchyLevel) {
        this.displayName = displayName;
        this.description = description;
        this.hierarchyLevel = hierarchyLevel;
    }
    
    /**
     * Gets the human-readable display name.
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Gets the detailed description of the role.
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Gets the hierarchy level (higher numbers = higher authority).
     */
    public int getHierarchyLevel() {
        return hierarchyLevel;
    }
    
    /**
     * Checks if this role has administrative privileges.
     */
    public boolean isAdmin() {
        return this == ADMIN || this == SUPER_ADMIN || this == SECURITY_ADMIN;
    }
    
    /**
     * Checks if this role can access management features.
     */
    public boolean isManager() {
        return hierarchyLevel >= 40 && hierarchyLevel < 50;
    }
    
    /**
     * Checks if this role is a vendor type.
     */
    public boolean isVendor() {
        return hierarchyLevel >= 10 && hierarchyLevel < 20;
    }
    
    /**
     * Checks if this role is customer-facing.
     */
    public boolean isCustomer() {
        return hierarchyLevel >= 1 && hierarchyLevel < 10;
    }
    
    /**
     * Checks if this role is internal staff.
     */
    public boolean isStaff() {
        return hierarchyLevel >= 30 && hierarchyLevel < 60 && this != GUEST;
    }
    
    /**
     * Checks if this role can approve financial transactions.
     */
    public boolean canApproveTransactions() {
        return this == MANAGER || this == REGIONAL_MANAGER || 
               this == OPERATIONS_DIRECTOR || isAdmin();
    }
    
    /**
     * Checks if this role can access customer data.
     */
    public boolean canAccessCustomerData() {
        return this == CUSTOMER_SUPPORT || this == SALES_REP || 
               this == BUSINESS_ANALYST || isManager() || isAdmin();
    }
    
    /**
     * Checks if this role can modify system settings.
     */
    public boolean canModifySystemSettings() {
        return isAdmin() || this == OPERATIONS_DIRECTOR;
    }
    
    /**
     * Checks if this role can access financial reports.
     */
    public boolean canAccessFinancialReports() {
        return this == BUSINESS_ANALYST || isManager() || isAdmin();
    }
    
    /**
     * Checks if this role can manage other users.
     */
    public boolean canManageUsers() {
        return isManager() || isAdmin();
    }
    
    /**
     * Checks if this role has higher authority than another role.
     */
    public boolean hasHigherAuthorityThan(UserRole otherRole) {
        return this.hierarchyLevel > otherRole.hierarchyLevel;
    }
    
    /**
     * Gets the role category.
     */
    public RoleCategory getCategory() {
        return switch (hierarchyLevel / 10) {
            case 0 -> RoleCategory.CUSTOMER;
            case 1 -> RoleCategory.VENDOR;
            case 2 -> RoleCategory.LOGISTICS;
            case 3 -> RoleCategory.STAFF;
            case 4 -> RoleCategory.MANAGEMENT;
            case 5 -> RoleCategory.ADMINISTRATIVE;
            case 6 -> RoleCategory.SPECIAL;
            case 7 -> RoleCategory.GUEST;
            default -> RoleCategory.OTHER;
        };
    }
    
    /**
     * Gets all roles in the same category.
     */
    public UserRole[] getCategoryRoles() {
        RoleCategory category = getCategory();
        return switch (category) {
            case CUSTOMER -> new UserRole[]{CUSTOMER, PREMIUM_USER, VIP_CUSTOMER};
            case VENDOR -> new UserRole[]{VENDOR, ENTERPRISE_VENDOR, VERIFIED_VENDOR};
            case LOGISTICS -> new UserRole[]{COURIER, LOGISTICS_COORDINATOR, WAREHOUSE_OPERATOR, FLEET_MANAGER};
            case STAFF -> new UserRole[]{CUSTOMER_SUPPORT, SALES_REP, MARKETING_SPECIALIST, BUSINESS_ANALYST};
            case MANAGEMENT -> new UserRole[]{SUPERVISOR, MANAGER, REGIONAL_MANAGER, OPERATIONS_DIRECTOR};
            case ADMINISTRATIVE -> new UserRole[]{ADMIN, SUPER_ADMIN, SECURITY_ADMIN, COMPLIANCE_OFFICER};
            case SPECIAL -> new UserRole[]{API_CLIENT, BETA_TESTER, DEVELOPER, AUDITOR};
            case GUEST -> new UserRole[]{GUEST};
            case OTHER -> new UserRole[]{};
        };
    }
    
    /**
     * Role categories for grouping and organization.
     */
    public enum RoleCategory {
        CUSTOMER, VENDOR, LOGISTICS, STAFF, MANAGEMENT, ADMINISTRATIVE, SPECIAL, GUEST, OTHER
    }
    
    /**
     * Gets the default permissions for this role.
     */
    public String[] getDefaultPermissions() {
        return switch (this.getCategory()) {
            case CUSTOMER -> new String[]{"VIEW_PRODUCTS", "PLACE_ORDERS", "VIEW_ORDER_HISTORY"};
            case VENDOR -> new String[]{"MANAGE_PRODUCTS", "VIEW_SALES", "PROCESS_ORDERS"};
            case LOGISTICS -> new String[]{"VIEW_SHIPMENTS", "UPDATE_DELIVERY_STATUS", "MANAGE_ROUTES"};
            case STAFF -> new String[]{"VIEW_CUSTOMERS", "PROCESS_SUPPORT_TICKETS", "VIEW_REPORTS"};
            case MANAGEMENT -> new String[]{"MANAGE_TEAMS", "VIEW_ANALYTICS", "APPROVE_TRANSACTIONS"};
            case ADMINISTRATIVE -> new String[]{"MANAGE_USERS", "SYSTEM_CONFIG", "VIEW_ALL_DATA"};
            case SPECIAL -> new String[]{"API_ACCESS", "BETA_FEATURES", "DEVELOPMENT_TOOLS"};
            case GUEST -> new String[]{"VIEW_PRODUCTS", "BROWSE_CATALOG"};
            case OTHER -> new String[]{};
        };
    }
}