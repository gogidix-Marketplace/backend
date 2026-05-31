package com.gogidix.aiservices.aisecurityservice.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.aiservices.aisecurityservice.application.dto.request.DecryptionRequest;
import com.gogidix.aiservices.aisecurityservice.application.dto.request.EncryptionRequest;
import com.gogidix.aiservices.aisecurityservice.application.dto.response.DecryptionResponse;
import com.gogidix.aiservices.aisecurityservice.application.dto.response.EncryptionResponse;
import com.gogidix.aiservices.aisecurityservice.application.service.SecurityService;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionAlgorithm;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionKey;
import com.gogidix.aiservices.aisecurityservice.domain.model.EncryptionResult;
import com.gogidix.aiservices.aisecurityservice.domain.model.KeyStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("SecurityController REST API Tests")
class SecurityControllerTest {

    @Mock
    private SecurityService securityService;

    private MockMvc mockMvc;
    private SecurityController controller;
    private ObjectMapper objectMapper;

    private static final String KEY_ID = "key-123";
    private static final String ENCRYPTED_DATA = "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=";
    private static final String PLAIN_DATA = "Sensitive data to encrypt";
    private static final String DECRYPTED_DATA = "Decrypted sensitive data";

    @BeforeEach
    void setUp() {
        controller = new SecurityController(securityService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @AfterEach
    void tearDown() {
        reset(securityService);
    }

    @Nested
    @DisplayName("POST /api/v1/security/encrypt")
    class EncryptEndpointTests {

        @Test
        @DisplayName("Should encrypt data successfully")
        void shouldEncryptDataSuccessfully() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData(PLAIN_DATA);
            request.setAlgorithm(EncryptionAlgorithm.AES256);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.encryptedData").value(ENCRYPTED_DATA))
                    .andExpect(jsonPath("$.keyId").value(KEY_ID))
                    .andExpect(jsonPath("$.algorithm").value("AES256"));

            verify(securityService).encrypt(eq(PLAIN_DATA), eq(EncryptionAlgorithm.AES256));
        }

        @Test
        @DisplayName("Should use AES256 as default algorithm")
        void shouldUseAES256AsDefaultAlgorithm() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData(PLAIN_DATA);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());

            verify(securityService).encrypt(eq(PLAIN_DATA), eq(EncryptionAlgorithm.AES256));
        }

        @ParameterizedTest
        @ValueSource(strings = {"AES256", "RSA2048", "RSA4096"})
        @DisplayName("Should accept all encryption algorithms")
        void shouldAcceptAllEncryptionAlgorithms(String algorithm) throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData(PLAIN_DATA);
            request.setAlgorithm(EncryptionAlgorithm.valueOf(algorithm));

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should reject empty data")
        void shouldRejectEmptyData() throws Exception {
            EncryptionRequest request = new EncryptionRequest();
            request.setData("");

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should reject null data")
        void shouldRejectNullData() throws Exception {
            EncryptionRequest request = new EncryptionRequest();
            request.setData(null);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return encrypted data with checksum")
        void shouldReturnEncryptedDataWithChecksum() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData(PLAIN_DATA);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.checksum").exists());
        }

        @Test
        @DisplayName("Should return encryption timestamp")
        void shouldReturnEncryptionTimestamp() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData(PLAIN_DATA);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.encryptedAt").exists());
        }

        @Test
        @DisplayName("Should encrypt with RSA2048 algorithm")
        void shouldEncryptWithRSA2048Algorithm() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData(PLAIN_DATA);
            request.setAlgorithm(EncryptionAlgorithm.RSA2048);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.algorithm").value("RSA2048"));

            verify(securityService).encrypt(eq(PLAIN_DATA), eq(EncryptionAlgorithm.RSA2048));
        }

        @Test
        @DisplayName("Should encrypt with RSA4096 algorithm")
        void shouldEncryptWithRSA4096Algorithm() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData(PLAIN_DATA);
            request.setAlgorithm(EncryptionAlgorithm.RSA4096);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.algorithm").value("RSA4096"));

            verify(securityService).encrypt(eq(PLAIN_DATA), eq(EncryptionAlgorithm.RSA4096));
        }
    }

    @Nested
    @DisplayName("POST /api/v1/security/decrypt")
    class DecryptEndpointTests {

        @Test
        @DisplayName("Should decrypt data successfully")
        void shouldDecryptDataSuccessfully() throws Exception {
            when(securityService.decrypt(anyString(), anyString())).thenReturn(DECRYPTED_DATA);

            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(ENCRYPTED_DATA);
            request.setKeyId(KEY_ID);

            mockMvc.perform(post("/api/v1/security/decrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.decryptedData").value(DECRYPTED_DATA));

            verify(securityService).decrypt(eq(ENCRYPTED_DATA), eq(KEY_ID));
        }

        @Test
        @DisplayName("Should reject empty encrypted data")
        void shouldRejectEmptyEncryptedData() throws Exception {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData("");
            request.setKeyId(KEY_ID);

            mockMvc.perform(post("/api/v1/security/decrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should reject null encrypted data")
        void shouldRejectNullEncryptedData() throws Exception {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(null);
            request.setKeyId(KEY_ID);

            mockMvc.perform(post("/api/v1/security/decrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should reject empty key ID")
        void shouldRejectEmptyKeyId() throws Exception {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(ENCRYPTED_DATA);
            request.setKeyId("");

            mockMvc.perform(post("/api/v1/security/decrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should reject null key ID")
        void shouldRejectNullKeyId() throws Exception {
            DecryptionRequest request = new DecryptionRequest();
            request.setEncryptedData(ENCRYPTED_DATA);
            request.setKeyId(null);

            mockMvc.perform(post("/api/v1/security/decrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should accept various encrypted data formats")
        void shouldAcceptVariousEncryptedDataFormats() throws Exception {
            when(securityService.decrypt(anyString(), anyString())).thenReturn(DECRYPTED_DATA);

            String[] testData = {
                    "U2FsdGVkX1+vupppZksvRf5pq5g5XjFRlipRkwB0K1Y=",
                    "SGVsbG8gV29ybGQ=",
                    "YW55IGNhcm5hbCBwbGVhc3VyZQ=="
            };

            for (String encryptedData : testData) {
                DecryptionRequest request = new DecryptionRequest();
                request.setEncryptedData(encryptedData);
                request.setKeyId(KEY_ID);

                mockMvc.perform(post("/api/v1/security/decrypt")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk());
            }
        }

        @Test
        @DisplayName("Should accept various key IDs")
        void shouldAcceptVariousKeyIds() throws Exception {
            when(securityService.decrypt(anyString(), anyString())).thenReturn(DECRYPTED_DATA);

            String[] keyIds = {"key-001", "key-abc", "key-550e8400-e29b-41d4-a716-446655440000"};

            for (String keyId : keyIds) {
                DecryptionRequest request = new DecryptionRequest();
                request.setEncryptedData(ENCRYPTED_DATA);
                request.setKeyId(keyId);

                mockMvc.perform(post("/api/v1/security/decrypt")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk());
            }
        }
    }

    @Nested
    @DisplayName("POST /api/v1/security/keys/rotate")
    class RotateKeysEndpointTests {

        @Test
        @DisplayName("Should rotate keys successfully")
        void shouldRotateKeysSuccessfully() throws Exception {
            doNothing().when(securityService).rotateKeys();

            mockMvc.perform(post("/api/v1/security/keys/rotate"))
                    .andExpect(status().isAccepted());

            verify(securityService).rotateKeys();
        }

        @Test
        @DisplayName("Should return 202 Accepted")
        void shouldReturn202Accepted() throws Exception {
            doNothing().when(securityService).rotateKeys();

            mockMvc.perform(post("/api/v1/security/keys/rotate"))
                    .andExpect(status().isAccepted());
        }

        @Test
        @DisplayName("Should not require request body")
        void shouldNotRequireRequestBody() throws Exception {
            doNothing().when(securityService).rotateKeys();

            mockMvc.perform(post("/api/v1/security/keys/rotate"))
                    .andExpect(status().isAccepted());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/security/keys/{keyId}")
    class GetKeyEndpointTests {

        @Test
        @DisplayName("Should get key by ID")
        void shouldGetKeyById() throws Exception {
            EncryptionKey key = createMockEncryptionKey();
            when(securityService.getKey(eq(KEY_ID))).thenReturn(key);

            mockMvc.perform(get("/api/v1/security/keys/" + KEY_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keyId").value(KEY_ID))
                    .andExpect(jsonPath("$.algorithm").value("AES256"))
                    .andExpect(jsonPath("$.status").value("ACTIVE"));

            verify(securityService).getKey(eq(KEY_ID));
        }

        @Test
        @DisplayName("Should return 200 OK")
        void shouldReturn200Ok() throws Exception {
            EncryptionKey key = createMockEncryptionKey();
            when(securityService.getKey(anyString())).thenReturn(key);

            mockMvc.perform(get("/api/v1/security/keys/key-001"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should accept various key IDs")
        void shouldAcceptVariousKeyIds() throws Exception {
            EncryptionKey key = createMockEncryptionKey();
            when(securityService.getKey(anyString())).thenReturn(key);

            String[] keyIds = {"key-001", "key-abc", "key-x9y8z7"};

            for (String keyId : keyIds) {
                mockMvc.perform(get("/api/v1/security/keys/" + keyId))
                        .andExpect(status().isOk());
            }
        }

        @Test
        @DisplayName("Should return key with all properties")
        void shouldReturnKeyWithAllProperties() throws Exception {
            EncryptionKey key = createMockEncryptionKey();
            when(securityService.getKey(eq(KEY_ID))).thenReturn(key);

            mockMvc.perform(get("/api/v1/security/keys/" + KEY_ID))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.keyId").exists())
                    .andExpect(jsonPath("$.algorithm").exists())
                    .andExpect(jsonPath("$.status").exists())
                    .andExpect(jsonPath("$.createdAt").exists())
                    .andExpect(jsonPath("$.expiresAt").exists())
                    .andExpect(jsonPath("$.version").exists());
        }
    }

    @Nested
    @DisplayName("GET /api/v1/security/keys")
    class GetAllKeysEndpointTests {

        @Test
        @DisplayName("Should get all keys")
        void shouldGetAllKeys() throws Exception {
            Set<EncryptionKey> keys = Set.of(
                    createMockEncryptionKey(),
                    createMockEncryptionKey("key-456")
            );
            when(securityService.getAllKeys()).thenReturn(keys);

            mockMvc.perform(get("/api/v1/security/keys"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());

            verify(securityService).getAllKeys();
        }

        @Test
        @DisplayName("Should return 200 OK")
        void shouldReturn200OkForAllKeys() throws Exception {
            when(securityService.getAllKeys()).thenReturn(Set.of());

            mockMvc.perform(get("/api/v1/security/keys"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return empty list when no keys exist")
        void shouldReturnEmptyListWhenNoKeysExist() throws Exception {
            when(securityService.getAllKeys()).thenReturn(Set.of());

            mockMvc.perform(get("/api/v1/security/keys"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }

        @Test
        @DisplayName("Should return multiple keys")
        void shouldReturnMultipleKeys() throws Exception {
            Set<EncryptionKey> keys = Set.of(
                    createMockEncryptionKey("key-001"),
                    createMockEncryptionKey("key-002"),
                    createMockEncryptionKey("key-003")
            );
            when(securityService.getAllKeys()).thenReturn(keys);

            mockMvc.perform(get("/api/v1/security/keys"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle encrypt then decrypt workflow")
        void shouldHandleEncryptThenDecryptWorkflow() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);
            when(securityService.decrypt(anyString(), anyString())).thenReturn(DECRYPTED_DATA);

            // Encrypt
            EncryptionRequest encryptRequest = new EncryptionRequest();
            encryptRequest.setData(PLAIN_DATA);
            encryptRequest.setAlgorithm(EncryptionAlgorithm.AES256);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(encryptRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.encryptedData").value(ENCRYPTED_DATA))
                    .andExpect(jsonPath("$.keyId").value(KEY_ID));

            // Decrypt
            DecryptionRequest decryptRequest = new DecryptionRequest();
            decryptRequest.setEncryptedData(ENCRYPTED_DATA);
            decryptRequest.setKeyId(KEY_ID);

            mockMvc.perform(post("/api/v1/security/decrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(decryptRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.decryptedData").value(DECRYPTED_DATA));

            verify(securityService).encrypt(eq(PLAIN_DATA), eq(EncryptionAlgorithm.AES256));
            verify(securityService).decrypt(eq(ENCRYPTED_DATA), eq(KEY_ID));
        }

        @Test
        @DisplayName("Should handle key rotation workflow")
        void shouldHandleKeyRotationWorkflow() throws Exception {
            doNothing().when(securityService).rotateKeys();

            EncryptionKey key = createMockEncryptionKey();
            when(securityService.getKey(eq(KEY_ID))).thenReturn(key);

            // Rotate keys
            mockMvc.perform(post("/api/v1/security/keys/rotate"))
                    .andExpect(status().isAccepted());

            // Get key status
            mockMvc.perform(get("/api/v1/security/keys/" + KEY_ID))
                    .andExpect(status().isOk());

            verify(securityService).rotateKeys();
            verify(securityService).getKey(eq(KEY_ID));
        }
    }

    @Nested
    @DisplayName("Content Type Tests")
    class ContentTypeTests {

        @Test
        @DisplayName("Should accept application/json")
        void shouldAcceptApplicationJson() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData(PLAIN_DATA);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should return application/json")
        void shouldReturnApplicationJson() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData(PLAIN_DATA);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long data to encrypt")
        void shouldHandleVeryLongDataToEncrypt() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            String longData = "Sensitive data ".repeat(1000);
            EncryptionRequest request = new EncryptionRequest();
            request.setData(longData);

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle very long key ID")
        void shouldHandleVeryLongKeyId() throws Exception {
            EncryptionKey key = createMockEncryptionKey();
            when(securityService.getKey(anyString())).thenReturn(key);

            String longKeyId = "key-" + "x".repeat(200);

            mockMvc.perform(get("/api/v1/security/keys/" + longKeyId))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle special characters in data")
        void shouldHandleSpecialCharactersInData() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData("Data with special chars: @#$%^&*()");

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Should handle unicode in data")
        void shouldHandleUnicodeInData() throws Exception {
            EncryptionResult result = createMockEncryptionResult();
            when(securityService.encrypt(anyString(), any(EncryptionAlgorithm.class))).thenReturn(result);

            EncryptionRequest request = new EncryptionRequest();
            request.setData("Data with unicode: 🔒🔑 中文 日本語");

            mockMvc.perform(post("/api/v1/security/encrypt")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk());
        }
    }

    // Helper methods
    private EncryptionResult createMockEncryptionResult() {
        return EncryptionResult.builder()
                .encryptedData(ENCRYPTED_DATA)
                .keyId(KEY_ID)
                .algorithm(EncryptionAlgorithm.AES256)
                .encryptedAt(Instant.now())
                .checksum("a1b2c3d4")
                .build();
    }

    private EncryptionKey createMockEncryptionKey() {
        return createMockEncryptionKey(KEY_ID);
    }

    private EncryptionKey createMockEncryptionKey(String keyId) {
        return EncryptionKey.builder()
                .keyId(keyId)
                .algorithm(EncryptionAlgorithm.AES256)
                .status(KeyStatus.ACTIVE)
                .createdAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .version("v1.0")
                .build();
    }
}
