package com.gogidix.infrastructure.config.infrastructure.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for EncryptionService.
 */
@DisplayName("EncryptionService Tests")
class EncryptionServiceTest {

    private EncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        encryptionService = new EncryptionService();
        // Set a test encryption key (must be at least 16 chars for AES-128)
        ReflectionTestUtils.setField(encryptionService, "encryptionKey",
                "test-encryption-key-32-chars-long!!");
    }

    @Test
    @DisplayName("Should encrypt and decrypt string correctly")
    void shouldEncryptAndDecryptStringCorrectly() {
        String plaintext = "sensitive-data-value";

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertNotEquals(plaintext, encrypted);
        assertEquals(plaintext, decrypted);
    }

    @Test
    @DisplayName("Should produce different encrypted values for same input")
    void shouldProduceDifferentEncryptedValuesForSameInput() {
        String plaintext = "same-input";

        String encrypted1 = encryptionService.encrypt(plaintext);
        String encrypted2 = encryptionService.encrypt(plaintext);

        assertNotEquals(encrypted1, encrypted2,
                "Each encryption should use a random IV");
    }

    @Test
    @DisplayName("Should decrypt both values correctly")
    void shouldDecryptBothValuesCorrectly() {
        String plaintext = "same-input";

        String encrypted1 = encryptionService.encrypt(plaintext);
        String encrypted2 = encryptionService.encrypt(plaintext);

        assertEquals(plaintext, encryptionService.decrypt(encrypted1));
        assertEquals(plaintext, encryptionService.decrypt(encrypted2));
    }

    @Test
    @DisplayName("Should handle empty string")
    void shouldHandleEmptyString() {
        String plaintext = "";

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(plaintext, decrypted);
    }

    @Test
    @DisplayName("Should handle null value")
    void shouldHandleNullValue() {
        String encrypted = encryptionService.encrypt(null);
        String decrypted = encryptionService.decrypt(null);

        assertNull(encrypted);
        assertNull(decrypted);
    }

    @Test
    @DisplayName("Should handle special characters")
    void shouldHandleSpecialCharacters() {
        String plaintext = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(plaintext, decrypted);
    }

    @Test
    @DisplayName("Should handle unicode characters")
    void shouldHandleUnicodeCharacters() {
        String plaintext = "Hello 世界 🌍 Привет";

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(plaintext, decrypted);
    }

    @Test
    @DisplayName("Should handle long strings")
    void shouldHandleLongStrings() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append("a");
        }
        String plaintext = sb.toString();

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(plaintext, decrypted);
    }

    @Test
    @DisplayName("Should generate IV")
    void shouldGenerateIv() {
        String iv = encryptionService.generateIv();

        assertNotNull(iv);
        assertFalse(iv.isEmpty());
        assertTrue(iv.length() > 0);
    }

    @Test
    @DisplayName("Should generate different IVs")
    void shouldGenerateDifferentIVs() {
        String iv1 = encryptionService.generateIv();
        String iv2 = encryptionService.generateIv();

        assertNotEquals(iv1, iv2);
    }

    @Test
    @DisplayName("Should generate valid encryption key")
    void shouldGenerateValidEncryptionKey() {
        String key = encryptionService.generateNewKey();

        assertNotNull(key);
        assertFalse(key.isEmpty());
        // Base64 encoded 256-bit key should be 44 chars
        assertTrue(key.length() >= 32);
    }

    @Test
    @DisplayName("Should validate key is valid")
    void shouldValidateKeyIsValid() {
        assertTrue(encryptionService.isKeyValid());
    }

    @Test
    @DisplayName("Should detect invalid key")
    void shouldDetectInvalidKey() {
        EncryptionService invalidService = new EncryptionService();
        ReflectionTestUtils.setField(invalidService, "encryptionKey", "short");

        assertFalse(invalidService.isKeyValid());
    }

    @Test
    @DisplayName("Should throw exception for invalid encrypted data")
    void shouldThrowExceptionForInvalidEncryptedData() {
        assertThrows(RuntimeException.class, () -> {
            encryptionService.decrypt("invalid-encrypted-data");
        });
    }

    @Test
    @DisplayName("Should handle JSON strings")
    void shouldHandleJsonStrings() {
        String plaintext = "{\"key\":\"value\",\"number\":123,\"nested\":{\"field\":\"data\"}}";

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(plaintext, decrypted);
    }

    @Test
    @DisplayName("Should encrypt numeric string values")
    void shouldEncryptNumericStringValues() {
        String plaintext = "12345.6789";

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(plaintext, decrypted);
    }

    @Test
    @DisplayName("Should handle newlines in text")
    void shouldHandleNewlinesInText() {
        String plaintext = "line1\nline2\rline3\r\nline4";

        String encrypted = encryptionService.encrypt(plaintext);
        String decrypted = encryptionService.decrypt(encrypted);

        assertEquals(plaintext, decrypted);
    }

    @Test
    @DisplayName("Should produce Base64-encoded output")
    void shouldProduceBase64EncodedOutput() {
        String plaintext = "test-value";

        String encrypted = encryptionService.encrypt(plaintext);

        // Base64 only contains alphanumeric, +, /, and = characters
        assertTrue(encrypted.matches("^[A-Za-z0-9+/=]+$"));
    }

    @Test
    @DisplayName("Should maintain data integrity across multiple encryptions")
    void shouldMaintainDataIntegrityAcrossMultipleEncryptions() {
        String plaintext = "original-value";

        String encrypted1 = encryptionService.encrypt(plaintext);
        String decrypted1 = encryptionService.decrypt(encrypted1);

        String encrypted2 = encryptionService.encrypt(decrypted1);
        String decrypted2 = encryptionService.decrypt(encrypted2);

        assertEquals(plaintext, decrypted1);
        assertEquals(plaintext, decrypted2);
    }
}
