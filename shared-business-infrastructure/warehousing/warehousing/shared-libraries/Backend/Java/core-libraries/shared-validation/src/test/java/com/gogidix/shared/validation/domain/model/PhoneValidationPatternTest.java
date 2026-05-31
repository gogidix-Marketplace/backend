package com.gogidix.shared.validation.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for PhoneValidationPattern
 */
@DisplayName("PhoneValidationPattern Tests")
class PhoneValidationPatternTest {

    // ========== Basic Validation Tests ==========

    @Test
    @DisplayName("Test valid international phone numbers")
    void testValidInternationalPhoneNumbers() {
        assertTrue(PhoneValidationPattern.isValid("+14155552671", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL));
        assertTrue(PhoneValidationPattern.isValid("+442071234567", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL));
        assertTrue(PhoneValidationPattern.isValid("+49151123456", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL));
        assertTrue(PhoneValidationPattern.isValid("+33123456789", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL));
    }

    @ParameterizedTest
    @CsvSource({
        "+14155552671, true",
        "14155552671, true",
        "+44 20 7123 4567, true",
        "+1 (415) 555-2671, true",
        "invalid, false",
        "123, false",
        "+0123456789, false"
    })
    @DisplayName("Test basic phone validation")
    void testBasicPhoneValidation(String phone, boolean expected) {
        assertEquals(expected, PhoneValidationPattern.isValid(phone, PhoneValidationPattern.PhoneValidationLevel.BASIC));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Test null and empty phone numbers")
    void testNullAndEmptyPhones(String phone) {
        assertFalse(PhoneValidationPattern.isValid(phone));
    }

    // ========== Validation Level Tests ==========

    @Test
    @DisplayName("Test LENIENT validation level")
    void testLenientValidationLevel() {
        assertTrue(PhoneValidationPattern.isValid("(415) 555-2671", PhoneValidationPattern.PhoneValidationLevel.LENIENT));
        assertTrue(PhoneValidationPattern.isValid("415-555-2671", PhoneValidationPattern.PhoneValidationLevel.LENIENT));
        assertTrue(PhoneValidationPattern.isValid("+1 415 555 2671", PhoneValidationPattern.PhoneValidationLevel.LENIENT));
        assertFalse(PhoneValidationPattern.isValid("abc", PhoneValidationPattern.PhoneValidationLevel.LENIENT));
    }

    @Test
    @DisplayName("Test BASIC validation level")
    void testBasicValidationLevel() {
        assertTrue(PhoneValidationPattern.isValid("+14155552671", PhoneValidationPattern.PhoneValidationLevel.BASIC));
        assertTrue(PhoneValidationPattern.isValid("14155552671", PhoneValidationPattern.PhoneValidationLevel.BASIC));
        // Note: (415) 555-2671 is cleaned to 4155552671, which is valid for BASIC (digits only, 7-14 chars)
        assertTrue(PhoneValidationPattern.isValid("(415) 555-2671", PhoneValidationPattern.PhoneValidationLevel.BASIC));
    }

    @Test
    @DisplayName("Test INTERNATIONAL validation level")
    void testInternationalValidationLevel() {
        assertTrue(PhoneValidationPattern.isValid("+14155552671", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL));
        assertTrue(PhoneValidationPattern.isValid("+442071234567", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL));
        assertFalse(PhoneValidationPattern.isValid("14155552671", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL));
    }

    // ========== Country-Specific Validation Tests ==========

    @Test
    @DisplayName("Test US phone number validation")
    void testUSPhoneNumberValidation() {
        assertTrue(PhoneValidationPattern.isValidForCountry("+14155551234", "US"));
        assertTrue(PhoneValidationPattern.isValidForCountry("+14155551234", "us")); // Case insensitive

        // Invalid US numbers
        assertFalse(PhoneValidationPattern.isValidForCountry("+10123456789", "US")); // Invalid area code
        assertFalse(PhoneValidationPattern.isValidForCountry("+1415555123", "US")); // Too short
    }

    @Test
    @DisplayName("Test UK phone number validation")
    void testUKPhoneNumberValidation() {
        assertTrue(PhoneValidationPattern.isValidForCountry("+442071234567", "UK"));
        assertFalse(PhoneValidationPattern.isValidForCountry("+4412345678", "UK")); // Too short
    }

    @Test
    @DisplayName("Test Germany phone number validation")
    void testGermanyPhoneNumberValidation() {
        assertTrue(PhoneValidationPattern.isValidForCountry("+49151123456789", "DE"));
        assertFalse(PhoneValidationPattern.isValidForCountry("+49123456", "DE")); // Too short
    }

    @Test
    @DisplayName("Test France phone number validation")
    void testFrancePhoneNumberValidation() {
        assertTrue(PhoneValidationPattern.isValidForCountry("+33123456789", "FR"));
    }

    @Test
    @DisplayName("Test China phone number validation")
    void testChinaPhoneNumberValidation() {
        assertTrue(PhoneValidationPattern.isValidForCountry("+8612345678901", "CN"));
    }

    @Test
    @DisplayName("Test Japan phone number validation")
    void testJapanPhoneNumberValidation() {
        assertTrue(PhoneValidationPattern.isValidForCountry("+81123456789", "JP"));
    }

    @Test
    @DisplayName("Test India phone number validation")
    void testIndiaPhoneNumberValidation() {
        assertTrue(PhoneValidationPattern.isValidForCountry("+919876543210", "IN"));
    }

    @Test
    @DisplayName("Test Brazil phone number validation")
    void testBrazilPhoneNumberValidation() {
        assertTrue(PhoneValidationPattern.isValidForCountry("+5511987654321", "BR"));
    }

    @Test
    @DisplayName("Test Canada phone number validation")
    void testCanadaPhoneNumberValidation() {
        assertTrue(PhoneValidationPattern.isValidForCountry("+14165551234", "CA"));
    }

    @Test
    @DisplayName("Test unsupported country falls back to basic validation")
    void testUnsupportedCountryFallback() {
        // Should fallback to basic validation for unsupported country
        assertTrue(PhoneValidationPattern.isValidForCountry("+1234567890", "XX"));
    }

    @Test
    @DisplayName("Test null country code returns false")
    void testNullCountryCode() {
        assertFalse(PhoneValidationPattern.isValidForCountry("+14155551234", null));
    }

    // ========== Length Validation Tests ==========

    @Test
    @DisplayName("Test phone number too short")
    void testPhoneNumberTooShort() {
        assertFalse(PhoneValidationPattern.isValid("123456"));
        PhoneValidationPattern.PhoneValidationInfo info = PhoneValidationPattern.getValidationInfo("123456");
        assertFalse(info.isValid());
        assertTrue(info.getMessage().contains("too short"));
    }

    @Test
    @DisplayName("Test phone number too long")
    void testPhoneNumberTooLong() {
        // 16 digits - too long
        String longPhone = "+1234567890123456";
        assertFalse(PhoneValidationPattern.isValid(longPhone));
    }

    // ========== Formatting Tests ==========

    @Test
    @DisplayName("Test formatInternational")
    void testFormatInternational() {
        assertEquals("+14155552671", PhoneValidationPattern.formatInternational("14155552671"));
        assertEquals("+14155552671", PhoneValidationPattern.formatInternational("+14155552671"));
        // Note: formatInternational just adds "+" and cleans, doesn't infer country codes
        assertEquals("+4155552671", PhoneValidationPattern.formatInternational("(415) 555-2671"));
    }

    @Test
    @DisplayName("Test formatForDisplay US")
    void testFormatForDisplayUS() {
        String formatted = PhoneValidationPattern.formatForDisplay("4155552671", "US");
        assertEquals("(415) 555-2671", formatted);

        String formattedIntl = PhoneValidationPattern.formatForDisplay("14155552671", "US");
        assertEquals("+1 (415) 555-2671", formattedIntl);
    }

    @Test
    @DisplayName("Test formatForDisplay UK")
    void testFormatForDisplayUK() {
        String formatted = PhoneValidationPattern.formatForDisplay("+442071234567", "UK");
        assertTrue(formatted.contains("+44"));
        assertTrue(formatted.contains("20"));
    }

    @Test
    @DisplayName("Test formatForDisplay Germany")
    void testFormatForDisplayGermany() {
        String formatted = PhoneValidationPattern.formatForDisplay("+491511234567", "DE");
        assertTrue(formatted.contains("+49"));
    }

    @Test
    @DisplayName("Test formatForDisplay France")
    void testFormatForDisplayFrance() {
        String formatted = PhoneValidationPattern.formatForDisplay("+33123456789", "FR");
        assertTrue(formatted.contains("+33"));
    }

    @Test
    @DisplayName("Test formatForDisplay with unsupported country")
    void testFormatForDisplayUnsupportedCountry() {
        String formatted = PhoneValidationPattern.formatForDisplay("+1234567890", "XX");
        // Should return cleaned version
        assertEquals("+1234567890", formatted);
    }

    // ========== Validation Info Tests ==========

    @Test
    @DisplayName("Test getValidationInfo for null phone")
    void testGetValidationInfoForNull() {
        PhoneValidationPattern.PhoneValidationInfo info = PhoneValidationPattern.getValidationInfo(null);
        assertFalse(info.isValid());
        assertEquals("Phone number is null", info.getMessage());
    }

    @Test
    @DisplayName("Test getValidationInfo for empty phone")
    void testGetValidationInfoForEmpty() {
        PhoneValidationPattern.PhoneValidationInfo info = PhoneValidationPattern.getValidationInfo("");
        assertFalse(info.isValid());
        assertEquals("Phone number is empty", info.getMessage());
    }

    @Test
    @DisplayName("Test getValidationInfo for phone with no digits")
    void testGetValidationInfoWithNoDigits() {
        PhoneValidationPattern.PhoneValidationInfo info = PhoneValidationPattern.getValidationInfo("abc");
        assertFalse(info.isValid());
        assertEquals("Phone number contains no digits", info.getMessage());
    }

    @Test
    @DisplayName("Test getValidationInfo for valid international phone")
    void testGetValidationInfoForValidInternational() {
        PhoneValidationPattern.PhoneValidationInfo info = PhoneValidationPattern.getValidationInfo("+14155552671");
        assertTrue(info.isValid());
        assertEquals("Valid international phone number", info.getMessage());
        assertEquals(PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL, info.getLevel());
        assertEquals("US/CA", info.getCountryCode());
    }

    @Test
    @DisplayName("Test getValidationInfo for valid basic phone")
    void testGetValidationInfoForValidBasic() {
        PhoneValidationPattern.PhoneValidationInfo info = PhoneValidationPattern.getValidationInfo("14155552671");
        assertTrue(info.isValid());
        assertEquals("Valid phone number", info.getMessage());
        assertEquals(PhoneValidationPattern.PhoneValidationLevel.BASIC, info.getLevel());
    }

    // ========== Country Code Detection Tests ==========

    @Test
    @DisplayName("Test country code detection")
    void testCountryCodeDetection() {
        PhoneValidationPattern.PhoneValidationInfo info1 = PhoneValidationPattern.getValidationInfo("+14155552671");
        assertEquals("US/CA", info1.getCountryCode());

        PhoneValidationPattern.PhoneValidationInfo info2 = PhoneValidationPattern.getValidationInfo("+442071234567");
        assertEquals("UK", info2.getCountryCode());

        PhoneValidationPattern.PhoneValidationInfo info3 = PhoneValidationPattern.getValidationInfo("+491511234567");
        assertEquals("DE", info3.getCountryCode());

        PhoneValidationPattern.PhoneValidationInfo info4 = PhoneValidationPattern.getValidationInfo("+33123456789");
        assertEquals("FR", info4.getCountryCode());

        PhoneValidationPattern.PhoneValidationInfo info5 = PhoneValidationPattern.getValidationInfo("+8612345678901");
        assertEquals("CN", info5.getCountryCode());

        PhoneValidationPattern.PhoneValidationInfo info6 = PhoneValidationPattern.getValidationInfo("+81123456789");
        assertEquals("JP", info6.getCountryCode());

        PhoneValidationPattern.PhoneValidationInfo info7 = PhoneValidationPattern.getValidationInfo("+919876543210");
        assertEquals("IN", info7.getCountryCode());

        PhoneValidationPattern.PhoneValidationInfo info8 = PhoneValidationPattern.getValidationInfo("+5511987654321");
        assertEquals("BR", info8.getCountryCode());
    }

    // ========== PhoneValidationLevel Enum Tests ==========

    @Test
    @DisplayName("Test PhoneValidationLevel enum properties")
    void testPhoneValidationLevelEnum() {
        assertEquals("Lenient", PhoneValidationPattern.PhoneValidationLevel.LENIENT.getDisplayName());
        assertEquals("Basic format check with flexible characters", PhoneValidationPattern.PhoneValidationLevel.LENIENT.getDescription());

        assertEquals("Basic", PhoneValidationPattern.PhoneValidationLevel.BASIC.getDisplayName());
        assertEquals("Standard phone number validation", PhoneValidationPattern.PhoneValidationLevel.BASIC.getDescription());

        assertEquals("International", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL.getDisplayName());
        assertEquals("Strict E.164 international format", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL.getDescription());
    }

    // ========== PhoneValidationInfo Tests ==========

    @Test
    @DisplayName("Test PhoneValidationInfo getters")
    void testPhoneValidationInfoGetters() {
        PhoneValidationPattern.PhoneValidationInfo info = PhoneValidationPattern.getValidationInfo("+14155552671");

        assertTrue(info.isValid());
        assertEquals("Valid international phone number", info.getMessage());
        assertEquals(PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL, info.getLevel());
        assertEquals("US/CA", info.getCountryCode());
    }

    @Test
    @DisplayName("Test PhoneValidationInfo constructors")
    void testPhoneValidationInfoConstructors() {
        // Two-argument constructor
        PhoneValidationPattern.PhoneValidationInfo info1 = new PhoneValidationPattern.PhoneValidationInfo(true, "Valid");
        assertTrue(info1.isValid());
        assertEquals("Valid", info1.getMessage());
        assertNull(info1.getLevel());
        assertNull(info1.getCountryCode());

        // Three-argument constructor
        PhoneValidationPattern.PhoneValidationInfo info2 = new PhoneValidationPattern.PhoneValidationInfo(
            true, "Valid", PhoneValidationPattern.PhoneValidationLevel.BASIC);
        assertEquals(PhoneValidationPattern.PhoneValidationLevel.BASIC, info2.getLevel());
        assertNull(info2.getCountryCode());

        // Four-argument constructor
        PhoneValidationPattern.PhoneValidationInfo info3 = new PhoneValidationPattern.PhoneValidationInfo(
            true, "Valid", PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL, "US");
        assertEquals(PhoneValidationPattern.PhoneValidationLevel.INTERNATIONAL, info3.getLevel());
        assertEquals("US", info3.getCountryCode());
    }

    // ========== Edge Cases ==========

    @Test
    @DisplayName("Test phone with leading + in middle")
    void testPhoneWithPlusInMiddle() {
        // + in middle is removed during cleaning, leaving valid digits: 4155552671
        assertTrue(PhoneValidationPattern.isValid("415+555+2671", PhoneValidationPattern.PhoneValidationLevel.BASIC));
    }

    @Test
    @DisplayName("Test phone starting with 0 after +")
    void testPhoneStartingWithZeroAfterPlus() {
        assertFalse(PhoneValidationPattern.isValid("+0123456789"));
    }

    @Test
    @DisplayName("Test phone with only + symbol")
    void testPhoneWithOnlyPlus() {
        PhoneValidationPattern.PhoneValidationInfo info = PhoneValidationPattern.getValidationInfo("+");
        assertFalse(info.isValid());
    }
}
