package com.byteentropy.tokenization_vault_core.api;

/**
 * Immutable request for tokenization.
 * PAN is sensitive and handled only within the Virtual Thread context.
 */
public record TokenRequest(String pan, String requestContext) {
    public TokenRequest {
        if (pan == null || pan.length() < 13) {
            throw new IllegalArgumentException("Invalid PAN length");
        }
    }
}