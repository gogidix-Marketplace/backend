package com.gogidix.sales.territory.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Geographic Boundary Value Object
 * Represents geographic boundaries for territories
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeographicBoundary {

    private BoundaryType type;
    private String country;
    private List<String> states;
    private List<String> regions;
    private List<String> postalCodes;
    private List<String> cities;

    // Coordinates for polygon boundaries
    private List<Coordinate> polygon;

    // Boundary box for rectangular regions
    private BoundaryBox boundingBox;

    private Double radius; // For circular boundaries
    private Coordinate center; // Center point for circular boundaries

    public enum BoundaryType {
        COUNTRY,
        STATE,
        REGION,
        POSTAL_CODE,
        CITY,
        CUSTOM_POLYGON,
        RADIUS,
        BOUNDING_BOX
    }

    /**
     * Checks if this boundary overlaps with another
     */
    public boolean overlapsWith(GeographicBoundary other) {
        if (other == null) {
            return false;
        }

        // Simple country-level overlap check
        if (this.country != null && this.country.equals(other.getCountry())) {
            return true;
        }

        // State-level overlap check
        if (this.states != null && other.getStates() != null) {
            for (String state : this.states) {
                if (other.getStates().contains(state)) {
                    return true;
                }
            }
        }

        // Postal code overlap check
        if (this.postalCodes != null && other.getPostalCodes() != null) {
            for (String postalCode : this.postalCodes) {
                if (other.getPostalCodes().contains(postalCode)) {
                    return true;
                }
            }
        }

        // City overlap check
        if (this.cities != null && other.getCities() != null) {
            for (String city : this.cities) {
                if (other.getCities().contains(city)) {
                    return true;
                }
            }
        }

        // Bounding box overlap check
        if (this.boundingBox != null && other.getBoundingBox() != null) {
            return this.boundingBox.overlapsWith(other.getBoundingBox());
        }

        return false;
    }

    /**
     * Checks if a coordinate is within this boundary
     */
    public boolean contains(Coordinate coordinate) {
        if (coordinate == null) {
            return false;
        }

        // Radius-based check
        if (this.type == BoundaryType.RADIUS && this.center != null && this.radius != null) {
            double distance = this.center.distanceTo(coordinate);
            return distance <= this.radius;
        }

        // Bounding box check
        if (this.boundingBox != null) {
            return this.boundingBox.contains(coordinate);
        }

        // Simple checks for other types
        if (this.type == BoundaryType.COUNTRY) {
            return true; // Assuming country match is handled at a higher level
        }

        return false;
    }

    /**
     * Adds a state to the boundary
     */
    public void addState(String state) {
        if (this.states == null) {
            this.states = new ArrayList<>();
        }
        if (!this.states.contains(state)) {
            this.states.add(state);
        }
    }

    /**
     * Adds a postal code to the boundary
     */
    public void addPostalCode(String postalCode) {
        if (this.postalCodes == null) {
            this.postalCodes = new ArrayList<>();
        }
        if (!this.postalCodes.contains(postalCode)) {
            this.postalCodes.add(postalCode);
        }
    }

    /**
     * Adds a city to the boundary
     */
    public void addCity(String city) {
        if (this.cities == null) {
            this.cities = new ArrayList<>();
        }
        if (!this.cities.contains(city)) {
            this.cities.add(city);
        }
    }

    /**
     * Coordinate nested class
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinate {
        private Double latitude;
        private Double longitude;

        /**
         * Calculates distance to another coordinate using Haversine formula
         */
        public double distanceTo(Coordinate other) {
            if (other == null || this.latitude == null || this.longitude == null ||
                other.latitude == null || other.longitude == null) {
                return Double.MAX_VALUE;
            }

            final int R = 6371; // Radius of the earth in km

            double latDistance = Math.toRadians(other.latitude - this.latitude);
            double lonDistance = Math.toRadians(other.longitude - this.longitude);

            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(this.latitude))
                    * Math.cos(Math.toRadians(other.latitude))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            return R * c; // Distance in km
        }
    }

    /**
     * BoundaryBox nested class
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoundaryBox {
        private Coordinate northEast;
        private Coordinate southWest;

        /**
         * Checks if this bounding box overlaps with another
         */
        public boolean overlapsWith(BoundaryBox other) {
            if (other == null || this.northEast == null || this.southWest == null ||
                other.northEast == null || other.southWest == null) {
                return false;
            }

            // Check if there's any overlap
            return !(this.southWest.getLongitude() > other.northEast.getLongitude() ||
                     this.northEast.getLongitude() < other.southWest.getLongitude() ||
                     this.southWest.getLatitude() > other.northEast.getLatitude() ||
                     this.northEast.getLatitude() < other.southWest.getLatitude());
        }

        /**
         * Checks if a coordinate is within this bounding box
         */
        public boolean contains(Coordinate coordinate) {
            if (coordinate == null || this.northEast == null || this.southWest == null) {
                return false;
            }

            return coordinate.getLatitude() >= this.southWest.getLatitude() &&
                   coordinate.getLatitude() <= this.northEast.getLatitude() &&
                   coordinate.getLongitude() >= this.southWest.getLongitude() &&
                   coordinate.getLongitude() <= this.northEast.getLongitude();
        }
    }
}
