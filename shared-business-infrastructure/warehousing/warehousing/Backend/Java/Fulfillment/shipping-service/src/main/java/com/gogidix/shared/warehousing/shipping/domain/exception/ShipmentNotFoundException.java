package com.gogidix.shared.warehousing.shipping.domain.exception;

/**
 * Exception thrown when a shipment is not found
 */
public class ShipmentNotFoundException extends RuntimeException {

    public ShipmentNotFoundException(String message) {
        super(message);
    }

    public ShipmentNotFoundException(String shipmentId, String tenantId) {
        super(String.format("Shipment not found: %s for tenant: %s", shipmentId, tenantId));
    }
}
