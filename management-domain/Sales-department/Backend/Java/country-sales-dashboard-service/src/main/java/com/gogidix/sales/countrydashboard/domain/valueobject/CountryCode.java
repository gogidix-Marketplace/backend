package com.gogidix.sales.countrydashboard.domain.valueobject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Country Code Value Object
 * Validates and holds ISO 3166-1 alpha-2 country codes
 */
@Data
@Builder
@NoArgsConstructor
public class CountryCode {

    private static final Set<String> VALID_COUNTRIES = Set.of(
            "US", "UK", "GB", "DE", "FR", "ES", "IT", "NL", "BE", "AT", "CH",
            "SE", "NO", "DK", "FI", "PL", "CZ", "HU", "RO", "BG", "GR",
            "PT", "IE", "LU", "CA", "AU", "IN", "JP", "SG", "MY", "TH",
            "VN", "ID", "PH", "NZ", "ZA", "AE", "SA", "IL", "TR", "BR",
            "AR", "MX", "CL", "CO", "PE"
    );

    private String code;

    public CountryCode(String code) {
        if (code == null || code.length() != 2) {
            throw new IllegalArgumentException("Country code must be 2 characters");
        }
        String upperCode = code.toUpperCase();
        if (!VALID_COUNTRIES.contains(upperCode)) {
            throw new IllegalArgumentException("Invalid country code: " + code);
        }
        this.code = upperCode;
    }

    public static void validate(String code) {
        if (code == null || !VALID_COUNTRIES.contains(code.toUpperCase())) {
            throw new IllegalArgumentException("Invalid country code: " + code);
        }
    }

    public static CountryCode of(String code) {
        return new CountryCode(code);
    }

    public static boolean isValid(String code) {
        return code != null && VALID_COUNTRIES.contains(code.toUpperCase());
    }

    public String toISO3166() {
        return code;
    }

    public String getCurrency() {
        return CountryCurrencyMapping.getCurrencyForCountry(code);
    }

    public String getRegion() {
        return getRegionForCountry(code);
    }

    public static String getRegionForCountry(String countryCode) {
        return CountryRegionMapping.getRegionForCountry(countryCode);
    }

    @Override
    public String toString() {
        return code;
    }
}

/**
 * Country to Currency Mapping
 */
class CountryCurrencyMapping {
    private static final java.util.Map<String, String> MAPPING = java.util.Map.ofEntries(
            java.util.Map.entry("US", "USD"),
            java.util.Map.entry("UK", "GBP"),
            java.util.Map.entry("GB", "GBP"),
            java.util.Map.entry("DE", "EUR"),
            java.util.Map.entry("FR", "EUR"),
            java.util.Map.entry("ES", "EUR"),
            java.util.Map.entry("IT", "EUR"),
            java.util.Map.entry("NL", "EUR"),
            java.util.Map.entry("BE", "EUR"),
            java.util.Map.entry("AT", "EUR"),
            java.util.Map.entry("CH", "CHF"),
            java.util.Map.entry("SE", "SEK"),
            java.util.Map.entry("NO", "NOK"),
            java.util.Map.entry("DK", "DKK"),
            java.util.Map.entry("FI", "EUR"),
            java.util.Map.entry("PL", "PLN"),
            java.util.Map.entry("CZ", "CZK"),
            java.util.Map.entry("HU", "HUF"),
            java.util.Map.entry("RO", "RON"),
            java.util.Map.entry("BG", "BGN"),
            java.util.Map.entry("GR", "EUR"),
            java.util.Map.entry("PT", "EUR"),
            java.util.Map.entry("IE", "EUR"),
            java.util.Map.entry("LU", "EUR"),
            java.util.Map.entry("CA", "CAD"),
            java.util.Map.entry("AU", "AUD"),
            java.util.Map.entry("IN", "INR"),
            java.util.Map.entry("JP", "JPY"),
            java.util.Map.entry("SG", "SGD"),
            java.util.Map.entry("MY", "MYR"),
            java.util.Map.entry("TH", "THB"),
            java.util.Map.entry("VN", "VND"),
            java.util.Map.entry("ID", "IDR"),
            java.util.Map.entry("PH", "PHP"),
            java.util.Map.entry("NZ", "NZD"),
            java.util.Map.entry("ZA", "ZAR"),
            java.util.Map.entry("AE", "AED"),
            java.util.Map.entry("SA", "SAR"),
            java.util.Map.entry("IL", "ILS"),
            java.util.Map.entry("TR", "TRY"),
            java.util.Map.entry("BR", "BRL"),
            java.util.Map.entry("AR", "ARS"),
            java.util.Map.entry("MX", "MXN"),
            java.util.Map.entry("CL", "CLP"),
            java.util.Map.entry("CO", "COP"),
            java.util.Map.entry("PE", "PEN")
    );

    public static String getCurrencyForCountry(String countryCode) {
        return MAPPING.getOrDefault(countryCode.toUpperCase(), "USD");
    }
}

/**
 * Country to Region Mapping
 */
class CountryRegionMapping {
    private static final java.util.Map<String, String> MAPPING = java.util.Map.ofEntries(
            java.util.Map.entry("US", "NORTH_AMERICA"),
            java.util.Map.entry("CA", "NORTH_AMERICA"),
            java.util.Map.entry("MX", "NORTH_AMERICA"),
            java.util.Map.entry("UK", "EUROPE"),
            java.util.Map.entry("GB", "EUROPE"),
            java.util.Map.entry("DE", "EUROPE"),
            java.util.Map.entry("FR", "EUROPE"),
            java.util.Map.entry("ES", "EUROPE"),
            java.util.Map.entry("IT", "EUROPE"),
            java.util.Map.entry("NL", "EUROPE"),
            java.util.Map.entry("BE", "EUROPE"),
            java.util.Map.entry("AT", "EUROPE"),
            java.util.Map.entry("CH", "EUROPE"),
            java.util.Map.entry("SE", "EUROPE"),
            java.util.Map.entry("NO", "EUROPE"),
            java.util.Map.entry("DK", "EUROPE"),
            java.util.Map.entry("FI", "EUROPE"),
            java.util.Map.entry("PL", "EUROPE"),
            java.util.Map.entry("CZ", "EUROPE"),
            java.util.Map.entry("HU", "EUROPE"),
            java.util.Map.entry("RO", "EUROPE"),
            java.util.Map.entry("BG", "EUROPE"),
            java.util.Map.entry("GR", "EUROPE"),
            java.util.Map.entry("PT", "EUROPE"),
            java.util.Map.entry("IE", "EUROPE"),
            java.util.Map.entry("LU", "EUROPE"),
            java.util.Map.entry("AU", "ASIA_PACIFIC"),
            java.util.Map.entry("NZ", "ASIA_PACIFIC"),
            java.util.Map.entry("IN", "ASIA_PACIFIC"),
            java.util.Map.entry("JP", "ASIA_PACIFIC"),
            java.util.Map.entry("SG", "ASIA_PACIFIC"),
            java.util.Map.entry("MY", "ASIA_PACIFIC"),
            java.util.Map.entry("TH", "ASIA_PACIFIC"),
            java.util.Map.entry("VN", "ASIA_PACIFIC"),
            java.util.Map.entry("ID", "ASIA_PACIFIC"),
            java.util.Map.entry("PH", "ASIA_PACIFIC"),
            java.util.Map.entry("CN", "ASIA_PACIFIC"),
            java.util.Map.entry("KR", "ASIA_PACIFIC"),
            java.util.Map.entry("ZA", "MIDDLE_EAST_AFRICA"),
            java.util.Map.entry("AE", "MIDDLE_EAST_AFRICA"),
            java.util.Map.entry("SA", "MIDDLE_EAST_AFRICA"),
            java.util.Map.entry("IL", "MIDDLE_EAST_AFRICA"),
            java.util.Map.entry("TR", "MIDDLE_EAST_AFRICA"),
            java.util.Map.entry("BR", "LATIN_AMERICA"),
            java.util.Map.entry("AR", "LATIN_AMERICA"),
            java.util.Map.entry("CL", "LATIN_AMERICA"),
            java.util.Map.entry("CO", "LATIN_AMERICA"),
            java.util.Map.entry("PE", "LATIN_AMERICA")
    );

    public static String getRegionForCountry(String countryCode) {
        return MAPPING.getOrDefault(countryCode.toUpperCase(), "OTHER");
    }
}
