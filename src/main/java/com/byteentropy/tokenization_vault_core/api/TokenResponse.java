package com.byteentropy.tokenization_vault_core.api;

import java.time.Instant;

/**
 * Immutable response containing the FPE Token and a unique Trace ID.
 */
public record TokenResponse(
    String token, 
    String traceId, 
    long processingTimeMs,
    Instant createdAt
) {}