package com.byteentropy.tokenization_vault_core.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.byteentropy.tokenization_vault_core.crypto.KeyManager;
import com.byteentropy.tokenization_vault_core.crypto.FpeEngine;
import com.byteentropy.tokenization_vault_core.service.VaultService;
import com.byteentropy.tokenization_vault_core.repository.TokenRepository;;

@Configuration
public class AppConfig {

    @Bean
    public KeyManager keyManager() {
        return new KeyManager();
    }

    @Bean
    public FpeEngine fpeEngine(KeyManager keyManager) {
        return new FpeEngine(keyManager.getMasterKey());
    }

    @Bean
    public TokenRepository tokenRepository() {
        return new TokenRepository();
    }

    @Bean
    public VaultService vaultService(FpeEngine fpeEngine, TokenRepository repository) {
        return new VaultService(fpeEngine, repository);
    }
}
