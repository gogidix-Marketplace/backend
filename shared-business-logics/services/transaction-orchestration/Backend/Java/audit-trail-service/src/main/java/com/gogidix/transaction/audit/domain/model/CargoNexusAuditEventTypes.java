package com.gogidix.transaction.audit.domain.model;

public final class CargoNexusAuditEventTypes {

    private CargoNexusAuditEventTypes() {}

    public static final String CUSTODY_VOLUME_CHANGED = "CUSTODY_VOLUME_CHANGED";
    public static final String CUSTODY_TRANSFER = "CUSTODY_TRANSFER";
    public static final String SEAL_VERIFIED = "SEAL_VERIFIED";
    public static final String SEAL_BROKEN = "SEAL_BROKEN";
    public static final String LOADING_STARTED = "LOADING_STARTED";
    public static final String LOADING_COMPLETED = "LOADING_COMPLETED";
    public static final String LOADING_STEP_COMPLETED = "LOADING_STEP_COMPLETED";
    public static final String DELIVERY_STOP_COMPLETED = "DELIVERY_STOP_COMPLETED";
    public static final String DISCREPANCY_DETECTED = "DISCREPANCY_DETECTED";
    public static final String INSURANCE_QUOTE_REQUESTED = "INSURANCE_QUOTE_REQUESTED";
    public static final String INSURANCE_POLICY_ACTIVATED = "INSURANCE_POLICY_ACTIVATED";
    public static final String PAYMENT_ADVICE_CREATED = "PAYMENT_ADVICE_CREATED";
    public static final String PAYMENT_ESCROW_CHANGED = "PAYMENT_ESCROW_CHANGED";
    public static final String SHIPMENT_STATUS_CHANGED = "SHIPMENT_STATUS_CHANGED";

    public static final String AGGREGATE_TYPE_LOADING_SAGA = "LOADING_SAGA";
    public static final String AGGREGATE_TYPE_DELIVERY_SAGA = "DELIVERY_SAGA";
    public static final String AGGREGATE_TYPE_SHIPMENT_SAGA = "SHIPMENT_SAGA";
    public static final String AGGREGATE_TYPE_INSURANCE_SAGA = "INSURANCE_SAGA";
    public static final String AGGREGATE_TYPE_PAYMENT_SAGA = "PAYMENT_SAGA";
    public static final String AGGREGATE_TYPE_CUSTODY_LEDGER = "CUSTODY_LEDGER";
}
