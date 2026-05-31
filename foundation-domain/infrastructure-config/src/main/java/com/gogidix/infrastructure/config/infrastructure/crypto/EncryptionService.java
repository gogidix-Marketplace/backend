package com.gogidix.infrastructure.config.infrastructure.crypto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Service for encrypting and decrypting sensitive configuration values.
 *
 * <p>Uses AES-GCM encryption for symmetric encryption of secrets.</p>
 */
@Slf4j
@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256;
    private static final int GCM_TAG_LENGTH = 128;
    private static final int GCM_IV_LENGTH = 12;

    @Value("${infrastructure-config.encryption.key}")
    private String encryptionKey;

    private final SecureRandom secureRandom = new SecureRandom();

    /**
     * Encrypts a plaintext value.
     *
     * @param plaintext The plaintext to encrypt
     * @return Base64-encoded encrypted value
     */
    public String encrypt(String plaintext) {
        if (plaintext == null || plaintext.isEmpty()) {
            return plaintext;
        }

        try {
            SecretKey key = deriveKey();
            byte[] iv = Base64.getDecoder().decode(generateIv());

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);

            byte[] encrypted = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // Combine IV and encrypted data
            ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + encrypted.length);
            byteBuffer.put(iv);
            byteBuffer.put(encrypted);

            return Base64.getEncoder().encodeToString(byteBuffer.array());
        } catch (Exception e) {
            log.error("Encryption failed", e);
            throw new RuntimeException("Failed to encrypt value", e);
        }
    }

    /**
     * Decrypts an encrypted value.
     *
     * @param ciphertext Base64-encoded encrypted value
     * @return Decrypted plaintext
     */
    public String decrypt(String ciphertext) {
        if (ciphertext == null || ciphertext.isEmpty()) {
            return ciphertext;
        }

        try {
            SecretKey key = deriveKey();
            byte[] decoded = Base64.getDecoder().decode(ciphertext);

            ByteBuffer byteBuffer = ByteBuffer.wrap(decoded);
            byte[] iv = new byte[GCM_IV_LENGTH];
            byteBuffer.get(iv);
            byte[] encrypted = new byte[byteBuffer.remaining()];
            byteBuffer.get(encrypted);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("Decryption failed", e);
            throw new RuntimeException("Failed to decrypt value", e);
        }
    }

    /**
     * Generates a new IV (Initialization Vector).
     *
     * @return New IV as Base64 string
     */
    public String generateIv() {
        byte[] iv = new byte[GCM_IV_LENGTH];
        secureRandom.nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }

    /**
     * Derives a cryptographic key from the configured encryption key.
     *
     * @return Derived SecretKey
     */
    private SecretKey deriveKey() throws Exception {
        // Use SHA-256 to derive a key of the correct length
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(encryptionKey.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Generates a new random encryption key.
     *
     * @return Base64-encoded key
     */
    public String generateNewKey() {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(KEY_SIZE);
            SecretKey key = keyGenerator.generateKey();
            return Base64.getEncoder().encodeToString(key.getEncoded());
        } catch (Exception e) {
            log.error("Key generation failed", e);
            throw new RuntimeException("Failed to generate key", e);
        }
    }

    /**
     * Validates that the encryption key is properly configured.
     *
     * @return true if the key is valid
     */
    public boolean isKeyValid() {
        try {
            deriveKey();
            return encryptionKey != null && encryptionKey.length() >= 16;
        } catch (Exception e) {
            return false;
        }
    }
}
