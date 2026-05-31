package com.gogidix.aiservices.aisecurityservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EncryptionResult Domain Model Tests")
class EncryptionResultTest {

    private static final String ENCRYPTED_DATA = "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=";
    private static final String KEY_ID = "key-123";
    private static final String CHECKSUM = "a1b2c3d4e5f6";

    @Nested
    @DisplayName("Builder Tests")
    class BuilderTests {

        @Test
        @DisplayName("Should build with all fields")
        void shouldBuildWithAllFields() {
            Instant now = Instant.now();
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .encryptedAt(now)
                    .checksum(CHECKSUM)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(ENCRYPTED_DATA);
            assertThat(result.getKeyId()).isEqualTo(KEY_ID);
            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.AES256);
            assertThat(result.getEncryptedAt()).isEqualTo(now);
            assertThat(result.getChecksum()).isEqualTo(CHECKSUM);
        }

        @Test
        @DisplayName("Should build with required fields only")
        void shouldBuildWithRequiredFieldsOnly() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(ENCRYPTED_DATA);
            assertThat(result.getKeyId()).isEqualTo(KEY_ID);
            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.AES256);
        }

        @Test
        @DisplayName("Should build with null optional fields")
        void shouldBuildWithNullOptionalFields() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .encryptedAt(null)
                    .checksum(null)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(ENCRYPTED_DATA);
            assertThat(result.getEncryptedAt()).isNull();
            assertThat(result.getChecksum()).isNull();
        }

        @Test
        @DisplayName("Should support method chaining")
        void shouldSupportMethodChaining() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .checksum(CHECKSUM)
                    .build();

            assertThat(result.getChecksum()).isEqualTo(CHECKSUM);
        }
    }

    @Nested
    @DisplayName("EncryptedData Tests")
    class EncryptedDataTests {

        @Test
        @DisplayName("Should set and get encrypted data")
        void shouldSetAndGetEncryptedData() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(ENCRYPTED_DATA);
        }

        @Test
        @DisplayName("Should accept null encrypted data")
        void shouldAcceptNullEncryptedData() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(null)
                    .build();

            assertThat(result.getEncryptedData()).isNull();
        }

        @Test
        @DisplayName("Should accept empty encrypted data")
        void shouldAcceptEmptyEncryptedData() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData("")
                    .build();

            assertThat(result.getEncryptedData()).isEmpty();
        }

        @Test
        @DisplayName("Should accept Base64 encoded data")
        void shouldAcceptBase64EncodedData() {
            String base64Data = "SGVsbG8gV29ybGQ=";
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(base64Data)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(base64Data);
        }

        @Test
        @DisplayName("Should accept long encrypted data")
        void shouldAcceptLongEncryptedData() {
            String longData = "U2FsdGVkX1".repeat(100);
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(longData)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(longData);
        }
    }

    @Nested
    @DisplayName("KeyId Tests")
    class KeyIdTests {

        @Test
        @DisplayName("Should set and get key ID")
        void shouldSetAndGetKeyId() {
            EncryptionResult result = EncryptionResult.builder()
                    .keyId(KEY_ID)
                    .build();

            assertThat(result.getKeyId()).isEqualTo(KEY_ID);
        }

        @Test
        @DisplayName("Should accept null key ID")
        void shouldAcceptNullKeyId() {
            EncryptionResult result = EncryptionResult.builder()
                    .keyId(null)
                    .build();

            assertThat(result.getKeyId()).isNull();
        }

        @Test
        @DisplayName("Should accept empty key ID")
        void shouldAcceptEmptyKeyId() {
            EncryptionResult result = EncryptionResult.builder()
                    .keyId("")
                    .build();

            assertThat(result.getKeyId()).isEmpty();
        }

        @Test
        @DisplayName("Should accept UUID format key ID")
        void shouldAcceptUuidFormatKeyId() {
            String uuidKeyId = "key-550e8400-e29b-41d4-a716-446655440000";
            EncryptionResult result = EncryptionResult.builder()
                    .keyId(uuidKeyId)
                    .build();

            assertThat(result.getKeyId()).isEqualTo(uuidKeyId);
        }
    }

    @Nested
    @DisplayName("Algorithm Tests")
    class AlgorithmTests {

        @ParameterizedTest
        @EnumSource(EncryptionAlgorithm.class)
        @DisplayName("Should accept all encryption algorithms")
        void shouldAcceptAllEncryptionAlgorithms(EncryptionAlgorithm algorithm) {
            EncryptionResult result = EncryptionResult.builder()
                    .algorithm(algorithm)
                    .build();

            assertThat(result.getAlgorithm()).isEqualTo(algorithm);
        }

        @Test
        @DisplayName("Should accept null algorithm")
        void shouldAcceptNullAlgorithm() {
            EncryptionResult result = EncryptionResult.builder()
                    .algorithm(null)
                    .build();

            assertThat(result.getAlgorithm()).isNull();
        }

        @Test
        @DisplayName("Should support AES256 algorithm")
        void shouldSupportAES256Algorithm() {
            EncryptionResult result = EncryptionResult.builder()
                    .algorithm(EncryptionAlgorithm.AES256)
                    .build();

            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.AES256);
        }

        @Test
        @DisplayName("Should support RSA2048 algorithm")
        void shouldSupportRSA2048Algorithm() {
            EncryptionResult result = EncryptionResult.builder()
                    .algorithm(EncryptionAlgorithm.RSA2048)
                    .build();

            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.RSA2048);
        }

        @Test
        @DisplayName("Should support RSA4096 algorithm")
        void shouldSupportRSA4096Algorithm() {
            EncryptionResult result = EncryptionResult.builder()
                    .algorithm(EncryptionAlgorithm.RSA4096)
                    .build();

            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.RSA4096);
        }
    }

    @Nested
    @DisplayName("EncryptedAt Tests")
    class EncryptedAtTests {

        @Test
        @DisplayName("Should set and get encrypted at timestamp")
        void shouldSetAndGetEncryptedAt() {
            Instant now = Instant.now();
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedAt(now)
                    .build();

            assertThat(result.getEncryptedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should accept null encrypted at")
        void shouldAcceptNullEncryptedAt() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedAt(null)
                    .build();

            assertThat(result.getEncryptedAt()).isNull();
        }

        @Test
        @DisplayName("Should represent encryption time")
        void shouldRepresentEncryptionTime() {
            Instant before = Instant.now();
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedAt(Instant.now())
                    .build();
            Instant after = Instant.now();

            assertThat(result.getEncryptedAt()).isBetween(before, after);
        }

        @Test
        @DisplayName("Should accept past timestamp")
        void shouldAcceptPastTimestamp() {
            Instant past = Instant.now().minusSeconds(3600);
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedAt(past)
                    .build();

            assertThat(result.getEncryptedAt()).isEqualTo(past);
        }

        @Test
        @DisplayName("Should accept future timestamp")
        void shouldAcceptFutureTimestamp() {
            Instant future = Instant.now().plusSeconds(3600);
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedAt(future)
                    .build();

            assertThat(result.getEncryptedAt()).isEqualTo(future);
        }
    }

    @Nested
    @DisplayName("Checksum Tests")
    class ChecksumTests {

        @Test
        @DisplayName("Should set and get checksum")
        void shouldSetAndGetChecksum() {
            EncryptionResult result = EncryptionResult.builder()
                    .checksum(CHECKSUM)
                    .build();

            assertThat(result.getChecksum()).isEqualTo(CHECKSUM);
        }

        @Test
        @DisplayName("Should accept null checksum")
        void shouldAcceptNullChecksum() {
            EncryptionResult result = EncryptionResult.builder()
                    .checksum(null)
                    .build();

            assertThat(result.getChecksum()).isNull();
        }

        @Test
        @DisplayName("Should accept empty checksum")
        void shouldAcceptEmptyChecksum() {
            EncryptionResult result = EncryptionResult.builder()
                    .checksum("")
                    .build();

            assertThat(result.getChecksum()).isEmpty();
        }

        @Test
        @DisplayName("Should accept MD5 format checksum")
        void shouldAcceptMd5FormatChecksum() {
            String md5Checksum = "5d41402abc4b2a76b9719d911017c592";
            EncryptionResult result = EncryptionResult.builder()
                    .checksum(md5Checksum)
                    .build();

            assertThat(result.getChecksum()).isEqualTo(md5Checksum);
        }

        @Test
        @DisplayName("Should accept SHA256 format checksum")
        void shouldAcceptSha256FormatChecksum() {
            String sha256Checksum = "a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e";
            EncryptionResult result = EncryptionResult.builder()
                    .checksum(sha256Checksum)
                    .build();

            assertThat(result.getChecksum()).isEqualTo(sha256Checksum);
        }

        @Test
        @DisplayName("Should accept hexadecimal checksum")
        void shouldAcceptHexadecimalChecksum() {
            String hexChecksum = "0a1b2c3d4e5f";
            EncryptionResult result = EncryptionResult.builder()
                    .checksum(hexChecksum)
                    .build();

            assertThat(result.getChecksum()).isEqualTo(hexChecksum);
        }
    }

    @Nested
    @DisplayName("Getter Tests")
    class GetterTests {

        @Test
        @DisplayName("Should get encrypted data")
        void shouldGetEncryptedData() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(ENCRYPTED_DATA);
        }

        @Test
        @DisplayName("Should get key ID")
        void shouldGetKeyId() {
            EncryptionResult result = EncryptionResult.builder()
                    .keyId(KEY_ID)
                    .build();

            assertThat(result.getKeyId()).isEqualTo(KEY_ID);
        }

        @Test
        @DisplayName("Should get algorithm")
        void shouldGetAlgorithm() {
            EncryptionResult result = EncryptionResult.builder()
                    .algorithm(EncryptionAlgorithm.AES256)
                    .build();

            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.AES256);
        }

        @Test
        @DisplayName("Should get encrypted at")
        void shouldGetEncryptedAt() {
            Instant now = Instant.now();
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedAt(now)
                    .build();

            assertThat(result.getEncryptedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should get checksum")
        void shouldGetChecksum() {
            EncryptionResult result = EncryptionResult.builder()
                    .checksum(CHECKSUM)
                    .build();

            assertThat(result.getChecksum()).isEqualTo(CHECKSUM);
        }
    }

    @Nested
    @DisplayName("Lombok Data Tests")
    class LombokDataTests {

        @Test
        @DisplayName("Should generate equals")
        void shouldGenerateEquals() {
            EncryptionResult result1 = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .build();

            EncryptionResult result2 = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .build();

            assertThat(result1).isEqualTo(result2);
        }

        @Test
        @DisplayName("Should generate hashCode")
        void shouldGenerateHashCode() {
            EncryptionResult result1 = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .build();

            EncryptionResult result2 = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .build();

            assertThat(result1.hashCode()).isEqualTo(result2.hashCode());
        }

        @Test
        @DisplayName("Should generate toString")
        void shouldGenerateToString() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .build();

            String toString = result.toString();

            assertThat(toString).contains(ENCRYPTED_DATA);
            assertThat(toString).contains(KEY_ID);
        }

        @Test
        @DisplayName("Should generate setters")
        void shouldGenerateSetters() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .build();

            result.setKeyId("new-key-id");
            result.setAlgorithm(EncryptionAlgorithm.RSA2048);

            assertThat(result.getKeyId()).isEqualTo("new-key-id");
            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.RSA2048);
        }
    }

    @Nested
    @DisplayName("Use Case Tests")
    class UseCaseTests {

        @Test
        @DisplayName("Should represent successful encryption")
        void shouldRepresentSuccessfulEncryption() {
            Instant now = Instant.now();
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .encryptedAt(now)
                    .checksum(CHECKSUM)
                    .build();

            assertThat(result.getEncryptedData()).isNotNull();
            assertThat(result.getKeyId()).isNotNull();
            assertThat(result.getAlgorithm()).isNotNull();
            assertThat(result.getEncryptedAt()).isNotNull();
        }

        @Test
        @DisplayName("Should support AES256 encryption result")
        void shouldSupportAES256EncryptionResult() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.AES256)
                    .build();

            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.AES256);
        }

        @Test
        @DisplayName("Should support RSA2048 encryption result")
        void shouldSupportRSA2048EncryptionResult() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.RSA2048)
                    .build();

            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.RSA2048);
        }

        @Test
        @DisplayName("Should support RSA4096 encryption result")
        void shouldSupportRSA4096EncryptionResult() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .keyId(KEY_ID)
                    .algorithm(EncryptionAlgorithm.RSA4096)
                    .build();

            assertThat(result.getAlgorithm()).isEqualTo(EncryptionAlgorithm.RSA4096);
        }
    }

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should provide checksum for verification")
        void shouldProvideChecksumForVerification() {
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .checksum(CHECKSUM)
                    .build();

            assertThat(result.getChecksum()).isEqualTo(CHECKSUM);
        }

        @Test
        @DisplayName("Should include encryption timestamp")
        void shouldIncludeEncryptionTimestamp() {
            Instant now = Instant.now();
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(ENCRYPTED_DATA)
                    .encryptedAt(now)
                    .build();

            assertThat(result.getEncryptedAt()).isEqualTo(now);
        }

        @Test
        @DisplayName("Should reference the encryption key")
        void shouldReferenceTheEncryptionKey() {
            EncryptionResult result = EncryptionResult.builder()
                    .keyId(KEY_ID)
                    .build();

            assertThat(result.getKeyId()).isEqualTo(KEY_ID);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long encrypted data")
        void shouldHandleVeryLongEncryptedData() {
            String longData = "ENCRYPTED".repeat(1000);
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(longData)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(longData);
        }

        @Test
        @DisplayName("Should handle very long checksum")
        void shouldHandleVeryLongChecksum() {
            String longChecksum = "a".repeat(500);
            EncryptionResult result = EncryptionResult.builder()
                    .checksum(longChecksum)
                    .build();

            assertThat(result.getChecksum()).isEqualTo(longChecksum);
        }

        @Test
        @DisplayName("Should handle special characters in encrypted data")
        void shouldHandleSpecialCharactersInEncryptedData() {
            String specialData = "U2FsdGVkX1+/@#$%^&*()";
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(specialData)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(specialData);
        }

        @Test
        @DisplayName("Should handle unicode in encrypted data")
        void shouldHandleUnicodeInEncryptedData() {
            String unicodeData = "U2FsdGVkX1+🔒🔑";
            EncryptionResult result = EncryptionResult.builder()
                    .encryptedData(unicodeData)
                    .build();

            assertThat(result.getEncryptedData()).isEqualTo(unicodeData);
        }
    }
}
