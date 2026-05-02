package com.byteentropy.tokenization_vault_core.crypto;

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * KeyManager handles the secure retrieval of encryption keys.
 * In production, this would interface with AWS KMS, Azure Key Vault, or an HSM.
 */
@Component
public class KeyManager {

    private static final Logger logger = Logger.getLogger(KeyManager.class.getName());
    private final byte[] masterKey;

    // A static 16-byte fallback key for local development
    private static final String DEFAULT_KEY = "3ntr0py_V4ult_21"; 

    public KeyManager() {
        this.masterKey = resolveKey();
        validateKey(this.masterKey);
    }

    /**
     * Resolves key from Environment Variable or uses the secure default.
     */
    private byte[] resolveKey() {
        return Optional.ofNullable(System.getenv("VAULT_MASTER_KEY"))
                .map(envKey -> {
                    try {
                        logger.info("Loading Master Key from environment variable...");
                        return Base64.getDecoder().decode(envKey);
                    } catch (IllegalArgumentException e) {
                        logger.warning("Environment key is not valid Base64! Falling back to default.");
                        return DEFAULT_KEY.getBytes(StandardCharsets.UTF_8);
                    }
                })
                .orElseGet(() -> {
                    logger.info("No environment key found. Using default development key.");
                    return DEFAULT_KEY.getBytes(StandardCharsets.UTF_8);
                });
    }

    /**
     * Ensures the key is exactly 16, 24, or 32 bytes (AES requirements).
     */
    private void validateKey(byte[] key) {
        if (key.length != 16 && key.length != 24 && key.length != 32) {
            throw new IllegalStateException("Invalid AES key length: " + key.length + " bytes. " +
                    "Key must be 16, 24, or 32 bytes.");
        }
    }

    public byte[] getMasterKey() {
        return masterKey;
    }

    /**
     * Derives a 'Tweak' for FF1 encryption. 
     * Using the BIN (first 6 digits) as a tweak ensures that cards from 
     * different banks result in different token patterns even with the same master key.
     */
    public byte[] deriveTweak(String pan) {
        if (pan == null || pan.length() < 6) {
            return "DEFAULT_TWEAK".getBytes(StandardCharsets.UTF_8);
        }
        return pan.substring(0, 6).getBytes(StandardCharsets.UTF_8);
    }
}