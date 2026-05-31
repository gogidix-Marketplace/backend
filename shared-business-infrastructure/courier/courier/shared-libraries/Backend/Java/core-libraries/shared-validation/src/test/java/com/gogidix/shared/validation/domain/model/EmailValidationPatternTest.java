package com.gogidix.shared.validation.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test suite for EmailValidationPattern
 */
@DisplayName("EmailValidationPattern Tests")
class EmailValidationPatternTest {

    // ========== Basic Validation Tests ==========

    @Test
    @DisplayName("Test valid email addresses")
    void testValidEmailAddresses() {
        assertTrue(EmailValidationPattern.isValid("test@example.com"));
        assertTrue(EmailValidationPattern.isValid("user.name@example.com"));
        assertTrue(EmailValidationPattern.isValid("user+tag@example.com"));
        assertTrue(EmailValidationPattern.isValid("first.last@domain.co.uk"));
        assertTrue(EmailValidationPattern.isValid("123456@example.com"));
        assertTrue(EmailValidationPattern.isValid("test@subdomain.example.com"));
    }

    @ParameterizedTest
    @CsvSource({
        "test@example.com, true",
        "user.name@example.com, true",
        "user+tag@example.com, true",
        "invalid, false",
        "test@, false",
        "@example.com, false",
        "test@.com, false",
        "test@com, false"
    })
    @DisplayName("Test basic email validation")
    void testBasicEmailValidation(String email, boolean expected) {
        assertEquals(expected, EmailValidationPattern.isValid(email));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Test null and empty email addresses")
    void testNullAndEmptyEmails(String email) {
        assertFalse(EmailValidationPattern.isValid(email));
    }

    // ========== Validation Level Tests ==========

    @Test
    @DisplayName("Test BASIC validation level")
    void testBasicValidationLevel() {
        assertTrue(EmailValidationPattern.isValid("test@example.com", EmailValidationPattern.EmailValidationLevel.BASIC));
        assertTrue(EmailValidationPattern.isValid("user.name@example.com", EmailValidationPattern.EmailValidationLevel.BASIC));
        assertFalse(EmailValidationPattern.isValid("invalid", EmailValidationPattern.EmailValidationLevel.BASIC));
    }

    @Test
    @DisplayName("Test STRICT validation level")
    void testStrictValidationLevel() {
        assertTrue(EmailValidationPattern.isValid("test@example.com", EmailValidationPattern.EmailValidationLevel.STRICT));
        assertFalse(EmailValidationPattern.isValid(".test@example.com", EmailValidationPattern.EmailValidationLevel.STRICT));
        assertFalse(EmailValidationPattern.isValid("test.@example.com", EmailValidationPattern.EmailValidationLevel.STRICT));
    }

    @Test
    @DisplayName("Test LENIENT validation level")
    void testLenientValidationLevel() {
        assertTrue(EmailValidationPattern.isValid("a@b.c", EmailValidationPattern.EmailValidationLevel.LENIENT));
        assertTrue(EmailValidationPattern.isValid("test@example.com", EmailValidationPattern.EmailValidationLevel.LENIENT));
        assertFalse(EmailValidationPattern.isValid("invalid", EmailValidationPattern.EmailValidationLevel.LENIENT));
    }

    // ========== Invalid Email Tests ==========

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid",
        "@example.com",
        "test@",
        "test@.com",
        "test@com",
        "test..email@example.com",
        ".test@example.com",
        "test.@example.com",
        "test@example..com"
    })
    @DisplayName("Test invalid email formats")
    void testInvalidEmailFormats(String email) {
        assertFalse(EmailValidationPattern.isValid(email));
    }

    // ========== Length Validation Tests ==========

    @Test
    @DisplayName("Test email length validation")
    void testEmailLengthValidation() {
        // Valid length email
        String validEmail = "test@example.com";
        assertTrue(EmailValidationPattern.isValid(validEmail));

        // Create email > 254 characters (RFC 5321 limit)
        StringBuilder longEmail = new StringBuilder("a@");
        for (int i = 0; i < 254; i++) {
            longEmail.append("a");
        }
        longEmail.append(".com");
        assertFalse(EmailValidationPattern.isValid(longEmail.toString()));
    }

    @Test
    @DisplayName("Test local part length validation")
    void testLocalPartLengthValidation() {
        // Local part > 64 characters should be invalid
        StringBuilder longLocal = new StringBuilder();
        for (int i = 0; i < 65; i++) {
            longLocal.append("a");
        }
        String email = longLocal + "@example.com";
        assertFalse(EmailValidationPattern.isValid(email));
    }

    @Test
    @DisplayName("Test domain part length validation")
    void testDomainPartLengthValidation() {
        // Domain part > 253 characters should be invalid
        StringBuilder longDomain = new StringBuilder("test@");
        for (int i = 0; i < 254; i++) {
            longDomain.append("a");
        }
        assertFalse(EmailValidationPattern.isValid(longDomain.toString()));
    }

    // ========== @ Symbol Tests ==========

    @Test
    @DisplayName("Test email with no @ symbol")
    void testEmailWithNoAtSymbol() {
        assertFalse(EmailValidationPattern.isValid("testexample.com"));
    }

    @Test
    @DisplayName("Test email with multiple @ symbols")
    void testEmailWithMultipleAtSymbols() {
        assertFalse(EmailValidationPattern.isValid("test@@example.com"));
        assertFalse(EmailValidationPattern.isValid("test@exam@ple.com"));
    }

    // ========== Whitespace Tests ==========

    @Test
    @DisplayName("Test email with leading/trailing whitespace")
    void testEmailWithWhitespace() {
        // Should trim and validate
        assertTrue(EmailValidationPattern.isValid(" test@example.com "));
        assertTrue(EmailValidationPattern.isValid("test@example.com"));
    }

    // ========== Validation Info Tests ==========

    @Test
    @DisplayName("Test getValidationInfo for null email")
    void testGetValidationInfoForNull() {
        EmailValidationPattern.EmailValidationInfo info = EmailValidationPattern.getValidationInfo(null);
        assertFalse(info.isValid());
        assertEquals("Email is null", info.getMessage());
        assertNull(info.getLevel());
    }

    @Test
    @DisplayName("Test getValidationInfo for empty email")
    void testGetValidationInfoForEmpty() {
        EmailValidationPattern.EmailValidationInfo info = EmailValidationPattern.getValidationInfo("");
        assertFalse(info.isValid());
        assertEquals("Email is empty", info.getMessage());
    }

    @Test
    @DisplayName("Test getValidationInfo for email without @ symbol")
    void testGetValidationInfoWithoutAtSymbol() {
        EmailValidationPattern.EmailValidationInfo info = EmailValidationPattern.getValidationInfo("testexample.com");
        assertFalse(info.isValid());
        assertEquals("Missing @ symbol", info.getMessage());
    }

    @Test
    @DisplayName("Test getValidationInfo for email with multiple @ symbols")
    void testGetValidationInfoWithMultipleAtSymbols() {
        EmailValidationPattern.EmailValidationInfo info = EmailValidationPattern.getValidationInfo("test@@example.com");
        assertFalse(info.isValid());
        assertEquals("Multiple @ symbols found", info.getMessage());
    }

    @Test
    @DisplayName("Test getValidationInfo for email without local part")
    void testGetValidationInfoWithoutLocalPart() {
        EmailValidationPattern.EmailValidationInfo info = EmailValidationPattern.getValidationInfo("@example.com");
        assertFalse(info.isValid());
        assertEquals("Missing local part (before @)", info.getMessage());
    }

    @Test
    @DisplayName("Test getValidationInfo for email without domain part")
    void testGetValidationInfoWithoutDomainPart() {
        EmailValidationPattern.EmailValidationInfo info = EmailValidationPattern.getValidationInfo("test@");
        assertFalse(info.isValid());
        assertEquals("Missing domain part (after @)", info.getMessage());
    }

    @Test
    @DisplayName("Test getValidationInfo for valid email")
    void testGetValidationInfoForValidEmail() {
        EmailValidationPattern.EmailValidationInfo info = EmailValidationPattern.getValidationInfo("test@example.com");
        assertTrue(info.isValid());
        assertEquals("Valid email address", info.getMessage());
        assertNotNull(info.getLevel());
    }

    @Test
    @DisplayName("Test getValidationInfo for email without domain dot")
    void testGetValidationInfoWithoutDomainDot() {
        EmailValidationPattern.EmailValidationInfo info = EmailValidationPattern.getValidationInfo("test@domain");
        assertFalse(info.isValid());
        assertEquals("Domain must contain at least one dot", info.getMessage());
    }

    // ========== Domain Label Tests ==========

    @Test
    @DisplayName("Test domain label validation")
    void testDomainLabelValidation() {
        // Valid domain labels
        assertTrue(EmailValidationPattern.isValid("test@sub.domain.com"));

        // Invalid domain labels
        assertFalse(EmailValidationPattern.isValid("test@domain-.com"));
        assertFalse(EmailValidationPattern.isValid("test@-domain.com"));
        assertFalse(EmailValidationPattern.isValid("test@domain..com"));
    }

    // ========== TLD Tests ==========

    @ParameterizedTest
    @CsvSource({
        "test@example.com, true",
        "test@example.co.uk, true",
        "test@example.io, true",
        "test@example.tech, true",
        "test@example.a, false"     // Single character TLD - invalid (requires min 2 chars)
    })
    @DisplayName("Test various TLDs")
    void testVariousTLDs(String email, boolean expected) {
        assertEquals(expected, EmailValidationPattern.isValid(email));
    }

    // ========== Special Characters Tests ==========

    @Test
    @DisplayName("Test allowed special characters in local part")
    void testAllowedSpecialCharacters() {
        assertTrue(EmailValidationPattern.isValid("user.name@example.com"));
        assertTrue(EmailValidationPattern.isValid("user+tag@example.com"));
        assertTrue(EmailValidationPattern.isValid("user_tag@example.com"));
        assertTrue(EmailValidationPattern.isValid("user-tag@example.com"));
        assertTrue(EmailValidationPattern.isValid("user%tag@example.com"));
    }

    // ========== Edge Cases ==========

    @Test
    @DisplayName("Test consecutive dots in email")
    void testConsecutiveDots() {
        assertFalse(EmailValidationPattern.isValid("test..email@example.com"));
        assertFalse(EmailValidationPattern.isValid("test@email..com"));
        assertFalse(EmailValidationPattern.isValid("test@sub..domain.com"));
    }

    @Test
    @DisplayName("Test starting/ending with dot")
    void testStartingOrEndingWithDot() {
        assertFalse(EmailValidationPattern.isValid(".test@example.com"));
        assertFalse(EmailValidationPattern.isValid("test.@example.com"));
        assertFalse(EmailValidationPattern.isValid("test@.example.com"));
        assertFalse(EmailValidationPattern.isValid("test@example.com."));
    }

    // ========== EmailValidationLevel Enum Tests ==========

    @Test
    @DisplayName("Test EmailValidationLevel enum properties")
    void testEmailValidationLevelEnum() {
        assertEquals("Lenient", EmailValidationPattern.EmailValidationLevel.LENIENT.getDisplayName());
        assertEquals("Basic format check", EmailValidationPattern.EmailValidationLevel.LENIENT.getDescription());

        assertEquals("Basic", EmailValidationPattern.EmailValidationLevel.BASIC.getDisplayName());
        assertEquals("Standard email format validation", EmailValidationPattern.EmailValidationLevel.BASIC.getDescription());

        assertEquals("Strict", EmailValidationPattern.EmailValidationLevel.STRICT.getDisplayName());
        assertEquals("Strict RFC-compliant validation", EmailValidationPattern.EmailValidationLevel.STRICT.getDescription());
    }

    // ========== EmailValidationInfo Tests ==========

    @Test
    @DisplayName("Test EmailValidationInfo getters")
    void testEmailValidationInfoGetters() {
        EmailValidationPattern.EmailValidationInfo info = EmailValidationPattern.getValidationInfo("test@example.com");

        assertTrue(info.isValid());
        assertEquals("Valid email address", info.getMessage());
        assertNotNull(info.getLevel());
    }

    @Test
    @DisplayName("Test EmailValidationInfo constructors")
    void testEmailValidationInfoConstructors() {
        // Two-argument constructor
        EmailValidationPattern.EmailValidationInfo info1 = new EmailValidationPattern.EmailValidationInfo(true, "Valid");
        assertTrue(info1.isValid());
        assertEquals("Valid", info1.getMessage());
        assertNull(info1.getLevel());

        // Three-argument constructor
        EmailValidationPattern.EmailValidationInfo info2 = new EmailValidationPattern.EmailValidationInfo(
            true, "Valid", EmailValidationPattern.EmailValidationLevel.BASIC);
        assertEquals(EmailValidationPattern.EmailValidationLevel.BASIC, info2.getLevel());
    }
}
