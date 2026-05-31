package com.gogidix.shared.infrastructure.services.security.auth.domain.model;

import java.util.Set;

public final class CargoNexusRoles {

    private CargoNexusRoles() {}

    public static final String SYSTEM_ADMINISTRATOR = "CARGONEXUS_SYSTEM_ADMIN";
    public static final String MARKETER_ADMIN = "CARGONEXUS_MARKETER_ADMIN";
    public static final String MARKETER_OPERATOR = "CARGONEXUS_MARKETER_OPERATOR";
    public static final String SALES_REPRESENTATIVE = "CARGONEXUS_SALES_REP";
    public static final String MARKETER_FINANCE_OFFICER = "CARGONEXUS_MARKETER_FINANCE";
    public static final String DEPOT_MANAGER = "CARGONEXUS_DEPOT_MANAGER";
    public static final String DEPOT_REPRESENTATIVE = "CARGONEXUS_DEPOT_REP";
    public static final String TRANSPORT_PARTNER_ADMIN = "CARGONEXUS_TRANSPORT_ADMIN";
    public static final String DRIVER = "CARGONEXUS_DRIVER";
    public static final String INSURANCE_COMPANY = "CARGONEXUS_INSURER";
    public static final String BANK_OFFICER = "CARGONEXUS_BANK_OFFICER";
    public static final String CUSTOMER = "CARGONEXUS_CUSTOMER";
    public static final String COMMAND_CENTER_OPERATOR = "CARGONEXUS_COMMAND_CENTER";
    public static final String REGULATORY_AUDITOR = "CARGONEXUS_REGULATORY_AUDITOR";

    public static final String[] ALL_ROLES = {
        SYSTEM_ADMINISTRATOR, MARKETER_ADMIN, MARKETER_OPERATOR, SALES_REPRESENTATIVE,
        MARKETER_FINANCE_OFFICER, DEPOT_MANAGER, DEPOT_REPRESENTATIVE,
        TRANSPORT_PARTNER_ADMIN, DRIVER, INSURANCE_COMPANY, BANK_OFFICER,
        CUSTOMER, COMMAND_CENTER_OPERATOR, REGULATORY_AUDITOR
    };

    public static final class Permissions {
        private Permissions() {}

        public static final String PETROLEUM_LOADING_MANAGE = "cargonexus:loading:manage";
        public static final String PETROLEUM_LOADING_VIEW = "cargonexus:loading:view";
        public static final String PETROLEUM_DELIVERY_MANAGE = "cargonexus:delivery:manage";
        public static final String PETROLEUM_DELIVERY_VIEW = "cargonexus:delivery:view";
        public static final String INSURANCE_MANAGE = "cargonexus:insurance:manage";
        public static final String INSURANCE_VIEW = "cargonexus:insurance:view";
        public static final String PAYMENT_ADVICE_CREATE = "cargonexus:payment:create";
        public static final String PAYMENT_ADVICE_APPROVE = "cargonexus:payment:approve";
        public static final String PAYMENT_VIEW = "cargonexus:payment:view";
        public static final String CUSTODY_LEDGER_MANAGE = "cargonexus:custody:manage";
        public static final String CUSTODY_LEDGER_VIEW = "cargonexus:custody:view";
        public static final String FLEET_MANAGE = "cargonexus:fleet:manage";
        public static final String FLEET_VIEW = "cargonexus:fleet:view";
        public static final String DEPOT_MANAGE = "cargonexus:depot:manage";
        public static final String DEPOT_VIEW = "cargonexus:depot:view";
        public static final String SHIPMENT_MANAGE = "cargonexus:shipment:manage";
        public static final String SHIPMENT_VIEW = "cargonexus:shipment:view";
        public static final String PROCUREMENT_MANAGE = "cargonexus:procurement:manage";
        public static final String PROCUREMENT_VIEW = "cargonexus:procurement:view";
        public static final String COMMAND_CENTER_OPERATE = "cargonexus:command:operate";
        public static final String COMMAND_CENTER_VIEW = "cargonexus:command:view";
        public static final String AUDIT_TRAIL_VIEW = "cargonexus:audit:view";
        public static final String DPR_COMPLIANCE_MANAGE = "cargonexus:dpr:manage";
        public static final String DPR_COMPLIANCE_VIEW = "cargonexus:dpr:view";
    }

    public static final Set<String> MARKETER_ADMIN_PERMISSIONS = Set.of(
        Permissions.PETROLEUM_LOADING_VIEW, Permissions.PETROLEUM_DELIVERY_VIEW,
        Permissions.INSURANCE_MANAGE, Permissions.INSURANCE_VIEW,
        Permissions.PAYMENT_ADVICE_CREATE, Permissions.PAYMENT_VIEW,
        Permissions.SHIPMENT_MANAGE, Permissions.SHIPMENT_VIEW,
        Permissions.PROCUREMENT_MANAGE, Permissions.PROCUREMENT_VIEW,
        Permissions.CUSTODY_LEDGER_VIEW, Permissions.DPR_COMPLIANCE_VIEW
    );

    public static final Set<String> DEPOT_MANAGER_PERMISSIONS = Set.of(
        Permissions.PETROLEUM_LOADING_MANAGE, Permissions.PETROLEUM_LOADING_VIEW,
        Permissions.DEPOT_MANAGE, Permissions.DEPOT_VIEW,
        Permissions.CUSTODY_LEDGER_MANAGE, Permissions.CUSTODY_LEDGER_VIEW,
        Permissions.DPR_COMPLIANCE_MANAGE, Permissions.DPR_COMPLIANCE_VIEW
    );

    public static final Set<String> DRIVER_PERMISSIONS = Set.of(
        Permissions.PETROLEUM_DELIVERY_VIEW, Permissions.PETROLEUM_LOADING_VIEW,
        Permissions.FLEET_VIEW, Permissions.SHIPMENT_VIEW
    );

    public static final Set<String> BANK_OFFICER_PERMISSIONS = Set.of(
        Permissions.PAYMENT_ADVICE_APPROVE, Permissions.PAYMENT_VIEW,
        Permissions.CUSTODY_LEDGER_VIEW, Permissions.AUDIT_TRAIL_VIEW
    );

    public static final Set<String> COMMAND_CENTER_PERMISSIONS = Set.of(
        Permissions.COMMAND_CENTER_OPERATE, Permissions.COMMAND_CENTER_VIEW,
        Permissions.PETROLEUM_LOADING_VIEW, Permissions.PETROLEUM_DELIVERY_VIEW,
        Permissions.FLEET_VIEW, Permissions.SHIPMENT_VIEW, Permissions.CUSTODY_LEDGER_VIEW
    );
}
