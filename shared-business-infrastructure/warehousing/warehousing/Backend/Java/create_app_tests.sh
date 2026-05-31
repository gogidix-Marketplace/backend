#!/bin/bash

# Analytics services (4)
create_test "Analytics/fulfillment-analytics-service" "com.gogidix.shared.warehousing.analytics.fulfillment" "FulfillmentAnalyticsServiceApplication"
create_test "Analytics/inventory-analytics-service" "com.gogidix.shared.warehousing.analytics.inventory" "InventoryAnalyticsServiceApplication"
create_test "Analytics/reporting-service" "com.gogidix.shared.warehousing.reporting" "ReportingServiceApplication"
create_test "Analytics/warehouse-analytics-service" "com.gogidix.shared.warehousing.analytics.warehouse" "WarehouseAnalyticsServiceApplication"

echo "Analytics tests created"
