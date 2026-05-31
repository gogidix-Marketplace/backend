package com.gogidix.ecommerce.inventory.shared.constants;

public final class InventoryConstants {
    private InventoryConstants() {}
    public static final String SERVICE_NAME = "inventory-service";
    public static final String API_BASE_PATH = "/api/v1/inventorys";
    public static final String HEADER_TENANT_ID = "X-Tenant-ID";
    public static final String HEADER_CORRELATION_ID = "X-Correlation-ID";
    public static final String MONGO_DATABASE = "gogidix_inventory";
}
