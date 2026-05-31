package com.gogidix.dashboard.shared.adapter.entity;

public final class CargoNexusDashboardWidgets {

    private CargoNexusDashboardWidgets() {}

    public static final String TANKER_TRACKING_MAP = "cargonexus-tanker-tracking-map";
    public static final String DEPOT_LOADING_STATUS = "cargonexus-depot-loading-status";
    public static final String INSURANCE_EXPOSURE_SUMMARY = "cargonexus-insurance-exposure";
    public static final String BANK_SETTLEMENT_STATUS = "cargonexus-bank-settlement";
    public static final String CUSTODY_LEDGER_OVERVIEW = "cargonexus-custody-ledger";
    public static final String LOADING_QUEUE = "cargonexus-loading-queue";
    public static final String DELIVERY_ROUTES = "cargonexus-delivery-routes";
    public static final String DISCREPANCY_ALERTS = "cargonexus-discrepancy-alerts";
    public static final String FUEL_LOSS_MONITOR = "cargonexus-fuel-loss-monitor";
    public static final String VOLUME_RECONCILIATION = "cargonexus-volume-reconciliation";

    public static final String WIDGET_GROUP_OPERATIONS = "OPERATIONS";
    public static final String WIDGET_GROUP_FINANCIAL = "FINANCIAL";
    public static final String WIDGET_GROUP_COMPLIANCE = "COMPLIANCE";
    public static final String WIDGET_GROUP_LOGISTICS = "LOGISTICS";

    public static final String[] ALL_WIDGETS = {
        TANKER_TRACKING_MAP, DEPOT_LOADING_STATUS, INSURANCE_EXPOSURE_SUMMARY,
        BANK_SETTLEMENT_STATUS, CUSTODY_LEDGER_OVERVIEW, LOADING_QUEUE,
        DELIVERY_ROUTES, DISCREPANCY_ALERTS, FUEL_LOSS_MONITOR, VOLUME_RECONCILIATION
    };
}
