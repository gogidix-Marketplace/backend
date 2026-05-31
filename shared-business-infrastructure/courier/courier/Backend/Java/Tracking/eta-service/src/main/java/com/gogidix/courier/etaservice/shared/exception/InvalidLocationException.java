package com.gogidix.courier.etaservice.shared.exception;

/**
 * Exception thrown when location data is invalid.
 */
public class InvalidLocationException extends ValidationException {

    private final Double latitude;
    private final Double longitude;

    public InvalidLocationException(String message) {
        super(message);
        this.latitude = null;
        this.longitude = null;
    }

    public InvalidLocationException(Double latitude, Double longitude) {
        super(String.format("Invalid location: latitude=%s, longitude=%s", latitude, longitude));
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public InvalidLocationException(String message, Double latitude, Double longitude) {
        super(message);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    @Override
    protected String deriveErrorCode() {
        return "INVALID_LOCATION";
    }
}
