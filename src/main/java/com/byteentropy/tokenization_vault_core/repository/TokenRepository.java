package com.byteentropy.tokenization_vault_core.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * High-performance repository using thread-safe non-blocking operations.
 */
public class TokenRepository {
    
    // In production: replace with Jedis/Lettuce for Redis integration
    private final Map<String, String> panToTokenMap = new ConcurrentHashMap<>(100_000);
    private final Map<String, String> tokenToPanMap = new ConcurrentHashMap<>(100_000);

    public void save(String pan, String token) {
        panToTokenMap.put(pan, token);
        tokenToPanMap.put(token, pan);
    }

    public String findTokenByPan(String pan) {
        return panToTokenMap.get(pan);
    }

    public String findPanByToken(String token) {
        return tokenToPanMap.get(token);
    }
}