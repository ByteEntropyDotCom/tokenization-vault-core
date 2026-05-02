package com.byteentropy.tokenization_vault_core.service;

import com.byteentropy.tokenization_vault_core.crypto.FpeEngine;
import com.byteentropy.tokenization_vault_core.api.TokenRequest;
import com.byteentropy.tokenization_vault_core.api.TokenResponse;
import com.byteentropy.tokenization_vault_core.repository.TokenRepository;
import java.time.Instant;
import java.util.UUID;

public class VaultService {

    private final FpeEngine fpeEngine;
    private final TokenRepository repository;

    public VaultService(FpeEngine fpeEngine, TokenRepository repository) {
        this.fpeEngine = fpeEngine;
        this.repository = repository;
    }

    /**
     * The primary entry point for tokenization.
     * Uses Virtual Threads for non-blocking repository checks.
     */
    public TokenResponse process(TokenRequest request) {
        long start = System.currentTimeMillis();
        
        // 1. Check if we already have a token for this PAN (Idempotency)
        String existingToken = repository.findTokenByPan(request.pan());
        if (existingToken != null) {
            return buildResponse(existingToken, start);
        }

        // 2. Perform Format-Preserving Encryption
        String newToken = fpeEngine.tokenize(request.pan());

        // 3. Persist mapping
        repository.save(request.pan(), newToken);

        return buildResponse(newToken, start);
    }

    private TokenResponse buildResponse(String token, long startTime) {
        return new TokenResponse(
            token,
            UUID.randomUUID().toString(),
            System.currentTimeMillis() - startTime,
            Instant.now()
        );
    }
}