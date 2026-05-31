package com.gogidix.shared.courier.ecommerce.domain.valueobject;

public enum HubStageStatus {
    PICKUP_ASSIGNED,
    PICKED_UP,
    AT_ORIGIN_HUB,
    IN_TRANSIT_BETWEEN_HUBS,
    AT_DESTINATION_HUB,
    LASTMILE_ASSIGNED,
    DELIVERED
}
