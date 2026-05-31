package com.gogidix.shared.infrastructure.services.security.tenantmanagement.domain.model;

public final class CargoNexusTenantTypes {

    private CargoNexusTenantTypes() {}

    public static final String PETROLEUM_MARKETER = "PETROLEUM_MARKETER";
    public static final String DEPOT_OPERATOR = "DEPOT_OPERATOR";
    public static final String TRANSPORT_PARTNER = "TRANSPORT_PARTNER";
    public static final String INSURANCE_COMPANY = "INSURANCE_COMPANY";
    public static final String BANK = "BANK";

    public static final String FEATURE_PETROLEUM_LOADING = "petroleum_loading";
    public static final String FEATURE_PETROLEUM_DELIVERY = "petroleum_delivery";
    public static final String FEATURE_INSURANCE_MARKETPLACE = "insurance_marketplace";
    public static final String FEATURE_BANKING_PAYMENT = "banking_payment";
    public static final String FEATURE_CUSTODY_LEDGER = "custody_ledger";
    public static final String FEATURE_IOT_MONITORING = "iot_monitoring";
    public static final String FEATURE_COMMAND_CENTER = "command_center";
    public static final String FEATURE_VEHICLE_OPERATIONS = "vehicle_operations";

    public static final String SETTINGS_LOADING_MODE = "loading_mode";
    public static final String SETTINGS_FLEET_TYPE = "fleet_type";
    public static final String SETTINGS_CURRENCY = "currency";
    public static final String SETTINGS_TIMEZONE = "timezone";
    public static final String SETTINGS_COUNTRY = "country";

    public static final String[] ALL_TENANT_TYPES = {
        PETROLEUM_MARKETER, DEPOT_OPERATOR, TRANSPORT_PARTNER,
        INSURANCE_COMPANY, BANK
    };
}
