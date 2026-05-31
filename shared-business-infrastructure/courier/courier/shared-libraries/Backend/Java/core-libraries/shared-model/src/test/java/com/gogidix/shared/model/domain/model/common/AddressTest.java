package com.gogidix.shared.model.domain.model.common;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for Address value object
 */
@DisplayName("Address Tests")
class AddressTest {

    @Test
    @DisplayName("Test builder creates valid address")
    void testBuilderCreatesValidAddress() {
        Address address = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .type(Address.AddressType.RESIDENTIAL)
                .build();

        assertEquals("123 Main St", address.getStreet1());
        assertEquals("New York", address.getCity());
        assertEquals("NY", address.getState());
        assertEquals("10001", address.getPostalCode());
        assertEquals("US", address.getCountry());
        assertEquals(Address.AddressType.RESIDENTIAL, address.getType());
    }

    @Test
    @DisplayName("Test usAddress builder helper")
    void testUsAddressBuilder() {
        Address address = Address.usAddress()
                .street1("456 Oak Ave")
                .city("Los Angeles")
                .state("CA")
                .postalCode("90001")
                .build();

        assertEquals("US", address.getCountry());
    }

    @Test
    @DisplayName("Test internationalAddress builder helper")
    void testInternationalAddressBuilder() {
        Address address = Address.internationalAddress("UK")
                .street1("Baker Street")
                .city("London")
                .postalCode("NW1 6XE")
                .build();

        assertEquals("UK", address.getCountry());
    }

    @Test
    @DisplayName("Test getFullStreetAddress")
    void testGetFullStreetAddress() {
        Address address1 = Address.builder()
                .street1("123 Main St")
                .build();

        assertEquals("123 Main St", address1.getFullStreetAddress());

        Address address2 = Address.builder()
                .street1("123 Main St")
                .street2("Apt 4B")
                .build();

        assertEquals("123 Main St, Apt 4B", address2.getFullStreetAddress());
    }

    @Test
    @DisplayName("Test getFullAddress")
    void testGetFullAddress() {
        Address address = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        String fullAddress = address.getFullAddress();

        assertTrue(fullAddress.contains("123 Main St"));
        assertTrue(fullAddress.contains("New York"));
        assertTrue(fullAddress.contains("NY"));
        assertTrue(fullAddress.contains("10001"));
        assertTrue(fullAddress.contains("US"));
    }

    @Test
    @DisplayName("Test getCityStatePostal")
    void testGetCityStatePostal() {
        Address address = Address.builder()
                .city("Austin")
                .state("TX")
                .postalCode("78701")
                .build();

        String csz = address.getCityStatePostal();

        assertEquals("Austin, TX 78701", csz);
    }

    @Test
    @DisplayName("Test isComplete with valid US address")
    void testIsCompleteWithValidUSAddress() {
        Address address = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        assertTrue(address.isComplete());
    }

    @Test
    @DisplayName("Test isComplete with international address (no state)")
    void testIsCompleteWithInternationalAddress() {
        Address address = Address.builder()
                .street1("Baker Street")
                .city("London")
                .country("UK")
                .build();

        assertTrue(address.isComplete());
    }

    @Test
    @DisplayName("Test isComplete returns false for incomplete address")
    void testIsCompleteReturnsFalseForIncomplete() {
        Address address1 = Address.builder()
                .city("New York")
                .country("US")
                .build();

        assertFalse(address1.isComplete());

        Address address2 = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .build();

        assertFalse(address2.isComplete());
    }

    @Test
    @DisplayName("Test isInternationalAddress")
    void testIsInternationalAddress() {
        Address usAddress = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .country("US")
                .build();

        Address ukAddress = Address.builder()
                .street1("Baker Street")
                .city("London")
                .country("UK")
                .build();

        assertFalse(usAddress.isInternationalAddress());
        assertTrue(ukAddress.isInternationalAddress());
    }

    @ParameterizedTest
    @CsvSource({
        "12345, US, true",
        "12345-6789, US, true",
        "1234, US, false",
        "ABCDE, US, false",
        "NW1 6XE, UK, true",
        "SW1A 1AA, UK, true",
        "K1A 0B1, CA, true",
        "10115, DE, true",
        "75001, FR, true"
    })
    @DisplayName("Test isPostalCodeValid")
    void testIsPostalCodeValid(String postalCode, String country, boolean expected) {
        Address address = Address.builder()
                .postalCode(postalCode)
                .country(country)
                .build();

        assertEquals(expected, address.isPostalCodeValid());
    }

    @Test
    @DisplayName("Test isDeliverable")
    void testIsDeliverable() {
        Address residential = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .type(Address.AddressType.RESIDENTIAL)
                .build();

        Address commercial = Address.builder()
                .street1("456 Business Ave")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .type(Address.AddressType.COMMERCIAL)
                .build();

        assertTrue(residential.isDeliverable());
        assertTrue(commercial.isDeliverable());
    }

    @Test
    @DisplayName("Test isEquivalentTo")
    void testIsEquivalentTo() {
        Address address1 = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        Address address2 = Address.builder()
                .street1("123 main st")  // Different case
                .city("new york")
                .state("ny")
                .postalCode("10001")
                .country("us")
                .build();

        Address address3 = Address.builder()
                .street1("456 Oak Ave")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        assertTrue(address1.isEquivalentTo(address2));
        assertFalse(address1.isEquivalentTo(address3));
        assertFalse(address1.isEquivalentTo(null));
    }

    @Test
    @DisplayName("Test getDistanceTo with coordinates")
    void testGetDistanceToWithCoordinates() {
        Address address1 = Address.builder()
                .latitude(40.7128)
                .longitude(-74.0060)
                .build();

        Address address2 = Address.builder()
                .latitude(34.0522)
                .longitude(-118.2437)
                .build();

        Double distance = address1.getDistanceTo(address2);

        assertNotNull(distance);
        // Distance between NYC and LA is approximately 3944 km
        assertTrue(distance > 3900 && distance < 4000);
    }

    @Test
    @DisplayName("Test getDistanceTo without coordinates returns null")
    void testGetDistanceToWithoutCoordinates() {
        Address address1 = Address.builder()
                .street1("123 Main St")
                .build();

        Address address2 = Address.builder()
                .street1("456 Oak Ave")
                .build();

        assertNull(address1.getDistanceTo(address2));
    }

    @Test
    @DisplayName("Test isWithinDistance")
    void testIsWithinDistance() {
        Address address1 = Address.builder()
                .latitude(40.7128)
                .longitude(-74.0060)
                .build();

        Address address2 = Address.builder()
                .latitude(40.7148)
                .longitude(-74.0068)
                .build();

        assertTrue(address1.isWithinDistance(address2, 1.0)); // Within 1km
        assertFalse(address1.isWithinDistance(address2, 0.1)); // Not within 100m
    }

    @ParameterizedTest
    @CsvSource({
        "US, NORTH_AMERICA",
        "CA, NORTH_AMERICA",
        "MX, NORTH_AMERICA",
        "UK, EUROPE",
        "DE, EUROPE",
        "FR, EUROPE",
        "JP, ASIA",
        "CN, ASIA",
        "AU, OCEANIA",
        "BR, SOUTH_AMERICA",
        "ZA, AFRICA",
        "XX, OTHER"
    })
    @DisplayName("Test getShippingRegion")
    void testGetShippingRegion(String country, Address.ShippingRegion expected) {
        Address address = Address.builder()
                .country(country)
                .build();

        assertEquals(expected, address.getShippingRegion());
    }

    @Test
    @DisplayName("Test getShippingRegion with null country")
    void testGetShippingRegionWithNullCountry() {
        Address address = Address.builder().build();
        assertEquals(Address.ShippingRegion.UNKNOWN, address.getShippingRegion());
    }

    @Test
    @DisplayName("Test isValidForType")
    void testIsValidForType() {
        Address address = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        assertTrue(address.isValidForType(Address.AddressType.RESIDENTIAL));
        assertTrue(address.isValidForType(Address.AddressType.COMMERCIAL));
        assertFalse(address.isValidForType(Address.AddressType.PO_BOX));
        assertFalse(address.isValidForType(Address.AddressType.MILITARY));
    }

    @Test
    @DisplayName("Test isValidForType with PO Box")
    void testIsValidForTypePOBox() {
        Address poBox = Address.builder()
                .street1("PO BOX 123")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        assertTrue(poBox.isValidForType(Address.AddressType.PO_BOX));
    }

    @Test
    @DisplayName("Test isValidForType with military address")
    void testIsValidForTypeMilitary() {
        Address apo = Address.builder()
                .street1("Unit 123")
                .state("APO")
                .country("US")
                .build();

        Address fpo = Address.builder()
                .street1("Unit 456")
                .state("FPO")
                .country("US")
                .build();

        assertTrue(apo.isValidForType(Address.AddressType.MILITARY));
        assertTrue(fpo.isValidForType(Address.AddressType.MILITARY));
    }

    @Test
    @DisplayName("Test normalizeForShipping")
    void testNormalizeForShipping() {
        Address address = Address.builder()
                .street1("123 main street")
                .city("new york")
                .state("new york")
                .postalCode("10001")
                .country("usa")
                .build();

        Address normalized = address.normalizeForShipping();

        assertEquals("123 MAIN ST", normalized.getStreet1());
        assertEquals("NEW YORK", normalized.getCity());
        assertEquals("NY", normalized.getState());
        assertEquals("10001", normalized.getPostalCode());
        assertEquals("US", normalized.getCountry());
    }

    @Test
    @DisplayName("Test with methods create new instances")
    void testWithMethodsCreateNewInstances() {
        Address original = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .build();

        Address modified = original.withStreet1("456 Oak Ave");

        assertEquals("123 Main St", original.getStreet1());
        assertEquals("456 Oak Ave", modified.getStreet1());
        assertEquals("New York", modified.getCity());
    }

    @Test
    @DisplayName("Test equals and hashCode")
    void testEqualsAndHashCode() {
        Address address1 = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        Address address2 = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        Address address3 = Address.builder()
                .street1("456 Oak Ave")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
        assertNotEquals(address1, address3);
    }

    @Test
    @DisplayName("Test toString returns full address")
    void testToString() {
        Address address = Address.builder()
                .street1("123 Main St")
                .city("New York")
                .state("NY")
                .postalCode("10001")
                .country("US")
                .build();

        String result = address.toString();

        assertTrue(result.contains("123 Main St"));
    }

    @Test
    @DisplayName("Test AddressType enum")
    void testAddressTypeEnum() {
        assertEquals("Residential", Address.AddressType.RESIDENTIAL.getDisplayName());
        assertEquals("Commercial", Address.AddressType.COMMERCIAL.getDisplayName());
        assertEquals("PO Box", Address.AddressType.PO_BOX.getDisplayName());
        assertEquals("Military", Address.AddressType.MILITARY.getDisplayName());
    }

    @Test
    @DisplayName("Test verified flag")
    void testVerifiedFlag() {
        Address verified = Address.builder()
                .street1("123 Main St")
                .isVerified(true)
                .build();

        Address unverified = Address.builder()
                .street1("123 Main St")
                .isVerified(false)
                .build();

        assertTrue(verified.isVerified());
        assertFalse(unverified.isVerified());
    }

    @Test
    @DisplayName("Test region field")
    void testRegionField() {
        Address address = Address.builder()
                .region("Northeast")
                .build();

        assertEquals("Northeast", address.getRegion());
    }

    @Test
    @DisplayName("Test street2 optional field")
    void testStreet2OptionalField() {
        Address withStreet2 = Address.builder()
                .street1("123 Main St")
                .street2("Apt 4B")
                .build();

        Address withoutStreet2 = Address.builder()
                .street1("123 Main St")
                .build();

        assertEquals("Apt 4B", withStreet2.getStreet2());
        assertNull(withoutStreet2.getStreet2());
    }

    @Test
    @DisplayName("Test normalizeStreetName abbreviations")
    void testNormalizeStreetName() {
        Address address = Address.builder()
                .street1("123 Main Street")
                .city("Anytown")
                .country("US")
                .build();

        Address normalized = address.normalizeForShipping();

        assertEquals("123 MAIN ST", normalized.getStreet1());
    }
}
