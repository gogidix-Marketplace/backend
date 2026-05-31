package com.gogidix.shared.warehousing.availability.domain.exception;

/**
 * Exception thrown when insufficient capacity is available
 */
public class InsufficientCapacityException extends RuntimeException {

    private final String warehouseId;
    private final Integer requested;
    private final Integer available;

    public InsufficientCapacityException(String message) {
        super(message);
        this.warehouseId = null;
        this.requested = null;
        this.available = null;
    }

    public InsufficientCapacityException(String warehouseId, Integer requested, Integer available) {
        super(String.format("Insufficient capacity in warehouse %s: requested=%d, available=%d",
            warehouseId, requested, available));
        this.warehouseId = warehouseId;
        this.requested = requested;
        this.available = available;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public Integer getRequested() {
        return requested;
    }

    public Integer getAvailable() {
        return available;
    }
}
