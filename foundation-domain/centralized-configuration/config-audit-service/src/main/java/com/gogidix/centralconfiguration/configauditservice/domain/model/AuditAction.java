package com.gogidix.centralconfiguration.configauditservice.domain.model;

/**
 * Audit Action enumeration.
 */
public enum AuditAction {
    CREATE("create", "Entity created"),
    UPDATE("update", "Entity updated"),
    DELETE("delete", "Entity deleted"),
    READ("read", "Entity read"),
    ENABLE("enable", "Entity enabled"),
    DISABLE("disable", "Entity disabled"),
    ROLLOUT("rollout", "Feature flag rolled out"),
    ROLLBACK("rollback", "Feature flag rolled back"),
    EXPORT("export", "Data exported"),
    IMPORT("import", "Data imported");

    private final String code;
    private final String description;

    AuditAction(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
