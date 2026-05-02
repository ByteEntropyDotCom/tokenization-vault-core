package com.byteentropy.tokenization_vault_core.controller;

import com.byteentropy.tokenization_vault_core.service.VaultService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.byteentropy.tokenization_vault_core.api.TokenRequest;
import com.byteentropy.tokenization_vault_core.api.TokenResponse;


@RestController
@RequestMapping("/api/v1/vault")
public class VaultController {

    private final VaultService vaultService;

    public VaultController(VaultService vaultService) {
        this.vaultService = vaultService;
    }

    @PostMapping("/tokenize")
    public TokenResponse tokenize(@RequestBody TokenRequest request) {
        // This execution now happens on a Virtual Thread!
        return vaultService.process(request);
    }
}