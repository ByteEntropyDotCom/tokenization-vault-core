package com.byteentropy.tokenization_vault_core.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * High-performance FF1 implementation for PAN Tokenization.
 * Maintains format (16 digits in -> 16 digits out).
 */
public class FpeEngine {

    private final SecretKeySpec secretKey;

    public FpeEngine(byte[] keyBytes) {
        this.secretKey = new SecretKeySpec(keyBytes, "AES");
    }

    public String tokenize(String pan) {
        // Simplified FF1-style logic for demonstration.
        // In production, use BouncyCastle's NIST-compliant FF1.
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            byte[] panBytes = pan.getBytes(StandardCharsets.UTF_8);
            byte[] padded = Arrays.copyOf(panBytes, 16); // AES Block size
            byte[] encrypted = cipher.doFinal(padded);
            
            return bytesToNumericToken(encrypted, pan.length());
        } catch (Exception e) {
            throw new RuntimeException("Tokenization failed", e);
        }
    }

    private String bytesToNumericToken(byte[] encrypted, int originalLength) {
        StringBuilder sb = new StringBuilder();
        for (byte b : encrypted) {
            sb.append(Math.abs(b % 10)); // Maintain numeric format
        }
        return sb.substring(0, originalLength);
    }
}
