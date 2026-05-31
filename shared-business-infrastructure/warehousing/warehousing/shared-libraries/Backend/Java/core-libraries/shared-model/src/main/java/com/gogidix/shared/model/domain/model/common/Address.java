package com.gogidix.shared.model.domain.model.common;

import lombok.Builder;
import lombok.Data;
import lombok.With;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Domain value object representing physical addresses.
 * Immutable and validates address components.
 */
@Data
@With
@Builder
public final class Address {
    
    private final String street1;
    private final String street2;
    private final String city;
    private final String state;
    private final String postalCode;
    private final String country;
    private final String region;
    private final AddressType type;
    private final boolean isVerified;
    private final Double latitude;
    private final Double longitude;
    
    // Common postal code patterns
    private static final Pattern US_POSTAL_CODE = Pattern.compile("^\\d{5}(-\\d{4})?$");
    private static final Pattern UK_POSTAL_CODE = Pattern.compile("^[A-Z]{1,2}\\d[A-Z\\d]?\\s?\\d[A-Z]{2}$");
    private static final Pattern CANADA_POSTAL_CODE = Pattern.compile("^[A-Z]\\d[A-Z]\\s?\\d[A-Z]\\d$");
    private static final Pattern GERMANY_POSTAL_CODE = Pattern.compile("^\\d{5}$");
    private static final Pattern FRANCE_POSTAL_CODE = Pattern.compile("^\\d{5}$");
    
    /**
     * Gets the full street address.
     */
    public String getFullStreetAddress() {
        StringBuilder fullStreet = new StringBuilder();
        if (street1 != null && !street1.trim().isEmpty()) {
            fullStreet.append(street1.trim());
        }
        if (street2 != null && !street2.trim().isEmpty()) {
            if (fullStreet.length() > 0) {
                fullStreet.append(", ");
            }
            fullStreet.append(street2.trim());
        }
        return fullStreet.toString();
    }
    
    /**
     * Gets the full address as a single string.
     */
    public String getFullAddress() {
        StringBuilder full = new StringBuilder();
        
        String streetAddress = getFullStreetAddress();
        if (!streetAddress.isEmpty()) {
            full.append(streetAddress);
        }
        
        if (city != null && !city.trim().isEmpty()) {
            if (full.length() > 0) full.append(", ");
            full.append(city.trim());
        }
        
        if (state != null && !state.trim().isEmpty()) {
            if (full.length() > 0) full.append(", ");
            full.append(state.trim());
        }
        
        if (postalCode != null && !postalCode.trim().isEmpty()) {
            if (full.length() > 0) full.append(" ");
            full.append(postalCode.trim());
        }
        
        if (country != null && !country.trim().isEmpty()) {
            if (full.length() > 0) full.append(", ");
            full.append(country.trim());
        }
        
        return full.toString();
    }
    
    /**
     * Gets the city, state postal code portion.
     */
    public String getCityStatePostal() {
        StringBuilder csz = new StringBuilder();
        
        if (city != null && !city.trim().isEmpty()) {
            csz.append(city.trim());
        }
        
        if (state != null && !state.trim().isEmpty()) {
            if (csz.length() > 0) csz.append(", ");
            csz.append(state.trim());
        }
        
        if (postalCode != null && !postalCode.trim().isEmpty()) {
            if (csz.length() > 0) csz.append(" ");
            csz.append(postalCode.trim());
        }
        
        return csz.toString();
    }
    
    /**
     * Validates the address completeness.
     */
    public boolean isComplete() {
        return street1 != null && !street1.trim().isEmpty() &&
               city != null && !city.trim().isEmpty() &&
               country != null && !country.trim().isEmpty() &&
               (isInternationalAddress() || 
                (state != null && !state.trim().isEmpty() && 
                 postalCode != null && !postalCode.trim().isEmpty()));
    }
    
    /**
     * Checks if this is an international address (non-US).
     */
    public boolean isInternationalAddress() {
        return country != null && !"US".equalsIgnoreCase(country) && !"USA".equalsIgnoreCase(country);
    }
    
    /**
     * Validates the postal code format based on country.
     */
    public boolean isPostalCodeValid() {
        if (postalCode == null || postalCode.trim().isEmpty()) {
            return false;
        }
        
        String code = postalCode.trim().toUpperCase();
        String countryCode = country != null ? country.toUpperCase() : "US";
        
        return switch (countryCode) {
            case "US", "USA" -> US_POSTAL_CODE.matcher(code).matches();
            case "UK", "GB" -> UK_POSTAL_CODE.matcher(code).matches();
            case "CA", "CANADA" -> CANADA_POSTAL_CODE.matcher(code).matches();
            case "DE", "GERMANY" -> GERMANY_POSTAL_CODE.matcher(code).matches();
            case "FR", "FRANCE" -> FRANCE_POSTAL_CODE.matcher(code).matches();
            default -> code.length() >= 3 && code.length() <= 10; // Generic validation
        };
    }
    
    /**
     * Checks if the address is deliverable.
     */
    public boolean isDeliverable() {
        return isComplete() && isPostalCodeValid() && 
               (type == AddressType.RESIDENTIAL || type == AddressType.COMMERCIAL);
    }
    
    /**
     * Checks if addresses are equivalent (ignoring case and extra whitespace).
     */
    public boolean isEquivalentTo(Address other) {
        if (other == null) return false;
        
        return normalize(this.street1).equals(normalize(other.street1)) &&
               normalize(this.street2).equals(normalize(other.street2)) &&
               normalize(this.city).equals(normalize(other.city)) &&
               normalize(this.state).equals(normalize(other.state)) &&
               normalize(this.postalCode).equals(normalize(other.postalCode)) &&
               normalize(this.country).equals(normalize(other.country));
    }
    
    /**
     * Calculates approximate distance to another address (if coordinates available).
     */
    public Double getDistanceTo(Address other) {
        if (this.latitude == null || this.longitude == null ||
            other.latitude == null || other.longitude == null) {
            return null;
        }
        
        return calculateHaversineDistance(
            this.latitude, this.longitude,
            other.latitude, other.longitude
        );
    }
    
    /**
     * Checks if the address is within a certain distance of another address.
     */
    public boolean isWithinDistance(Address other, double maxDistanceKm) {
        Double distance = getDistanceTo(other);
        return distance != null && distance <= maxDistanceKm;
    }
    
    /**
     * Gets the geographic region for shipping calculations.
     */
    public ShippingRegion getShippingRegion() {
        if (country == null) return ShippingRegion.UNKNOWN;
        
        String countryCode = country.toUpperCase();
        return switch (countryCode) {
            case "US", "USA" -> ShippingRegion.NORTH_AMERICA;
            case "CA", "CANADA" -> ShippingRegion.NORTH_AMERICA;
            case "MX", "MEXICO" -> ShippingRegion.NORTH_AMERICA;
            case "UK", "GB", "DE", "FR", "IT", "ES", "NL", "BE" -> ShippingRegion.EUROPE;
            case "CN", "JP", "KR", "IN", "TH", "SG" -> ShippingRegion.ASIA;
            case "AU", "NZ" -> ShippingRegion.OCEANIA;
            case "BR", "AR", "CO", "PE", "CL" -> ShippingRegion.SOUTH_AMERICA;
            case "ZA", "KE", "NG", "EG" -> ShippingRegion.AFRICA;
            default -> ShippingRegion.OTHER;
        };
    }
    
    /**
     * Validates that the address is suitable for the given address type.
     */
    public boolean isValidForType(AddressType addressType) {
        return switch (addressType) {
            case RESIDENTIAL -> true; // Most flexible
            case COMMERCIAL -> isComplete(); // Requires complete address
            case PO_BOX -> street1 != null && street1.toUpperCase().contains("PO BOX");
            case MILITARY -> state != null && 
                           ("APO".equals(state.toUpperCase()) || 
                            "FPO".equals(state.toUpperCase()) || 
                            "DPO".equals(state.toUpperCase()));
        };
    }
    
    /**
     * Normalizes address for shipping label printing.
     */
    public Address normalizeForShipping() {
        return this.withStreet1(normalizeStreetName(street1))
                  .withStreet2(street2 != null ? normalizeStreetName(street2) : null)
                  .withCity(normalize(city))
                  .withState(normalizeState(state))
                  .withPostalCode(normalizePostalCode(postalCode))
                  .withCountry(normalizeCountry(country));
    }
    
    /**
     * Creates a builder with common US state abbreviation.
     */
    public static AddressBuilder usAddress() {
        return builder().country("US");
    }
    
    /**
     * Creates a builder for international addresses.
     */
    public static AddressBuilder internationalAddress(String country) {
        return builder().country(country);
    }
    
    private static String normalize(String text) {
        return text != null ? text.trim().toUpperCase() : "";
    }
    
    private String normalizeStreetName(String street) {
        if (street == null) return null;

        return street.trim()
                    .replaceAll("(?i)\\bSTREET\\b", "ST")
                    .replaceAll("(?i)\\bAVENUE\\b", "AVE")
                    .replaceAll("(?i)\\bBOULEVARD\\b", "BLVD")
                    .replaceAll("(?i)\\bROAD\\b", "RD")
                    .replaceAll("(?i)\\bDRIVE\\b", "DR")
                    .replaceAll("(?i)\\bLANE\\b", "LN")
                    .replaceAll("(?i)\\bCOURT\\b", "CT")
                    .replaceAll("(?i)\\bCIRCLE\\b", "CIR")
                    .replaceAll("\\s+", " ")
                    .toUpperCase();
    }
    
    private String normalizeState(String state) {
        if (state == null) return null;
        
        // This would include a full state name to abbreviation mapping
        String normalized = state.trim().toUpperCase();
        return switch (normalized) {
            case "CALIFORNIA" -> "CA";
            case "NEW YORK" -> "NY";
            case "TEXAS" -> "TX";
            case "FLORIDA" -> "FL";
            // ... more mappings would be here
            default -> normalized;
        };
    }
    
    private String normalizePostalCode(String postal) {
        if (postal == null) return null;
        
        String normalized = postal.trim().toUpperCase();
        
        // US ZIP code normalization
        if (country != null && ("US".equalsIgnoreCase(country) || "USA".equalsIgnoreCase(country))) {
            normalized = normalized.replaceAll("[^0-9-]", "");
            if (normalized.length() == 9 && !normalized.contains("-")) {
                normalized = normalized.substring(0, 5) + "-" + normalized.substring(5);
            }
        }
        
        return normalized;
    }
    
    private String normalizeCountry(String country) {
        if (country == null) return null;
        
        String normalized = country.trim().toUpperCase();
        return switch (normalized) {
            case "USA", "UNITED STATES", "UNITED STATES OF AMERICA" -> "US";
            case "UNITED KINGDOM", "GREAT BRITAIN" -> "UK";
            case "DEUTSCHLAND" -> "DE";
            default -> normalized;
        };
    }
    
    private static double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;
        
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                  Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                  Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return EARTH_RADIUS_KM * c;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Address)) return false;
        Address other = (Address) obj;
        return Objects.equals(street1, other.street1) &&
               Objects.equals(street2, other.street2) &&
               Objects.equals(city, other.city) &&
               Objects.equals(state, other.state) &&
               Objects.equals(postalCode, other.postalCode) &&
               Objects.equals(country, other.country);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(street1, street2, city, state, postalCode, country);
    }
    
    @Override
    public String toString() {
        return getFullAddress();
    }
    
    /**
     * Address types for validation and processing.
     */
    public enum AddressType {
        RESIDENTIAL("Residential"),
        COMMERCIAL("Commercial"), 
        PO_BOX("PO Box"),
        MILITARY("Military");
        
        private final String displayName;
        
        AddressType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Shipping regions for logistics calculations.
     */
    public enum ShippingRegion {
        NORTH_AMERICA, EUROPE, ASIA, OCEANIA, SOUTH_AMERICA, AFRICA, OTHER, UNKNOWN
    }
}