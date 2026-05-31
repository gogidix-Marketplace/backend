package com.gogidix.ecommerce.inventorysync.shared.constants;

public final class InventorySyncConstants {
    private InventorySyncConstants() {}
    public static final String SERVICE_NAME = "inventory-sync-service";
    public static final String API_BASE_PATH = "/api/v1/inventory-syncs";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_inventorysync";
}
