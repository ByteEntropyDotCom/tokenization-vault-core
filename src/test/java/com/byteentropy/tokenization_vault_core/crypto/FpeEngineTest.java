package com.byteentropy.tokenization_vault_core.crypto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FpeEngineTest {

    private FpeEngine fpeEngine;

    @BeforeEach
    void setUp() {
        byte[] testKey = "1234567890123456".getBytes();
        fpeEngine = new FpeEngine(testKey);
    }

    @Test
    void testTokenizationPreservesLength() {
        String pan = "4111111111111111";
        String token = fpeEngine.tokenize(pan);

        assertNotNull(token);
        assertEquals(pan.length(), token.length(), "Token must be same length as PAN");
        assertNotEquals(pan, token, "Token must not be the same as the raw PAN");
    }

    @Test
    void testTokenizationIsNumeric() {
        String pan = "4111111111111111";
        String token = fpeEngine.tokenize(pan);

        assertTrue(token.matches("\\d+"), "Token must contain only digits");
    }
}