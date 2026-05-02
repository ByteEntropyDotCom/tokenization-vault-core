package com.byteentropy.tokenization_vault_core.service;

import com.byteentropy.tokenization_vault_core.api.TokenRequest;
import com.byteentropy.tokenization_vault_core.crypto.FpeEngine;
import com.byteentropy.tokenization_vault_core.repository.TokenRepository;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VaultConcurrencyTest {

    @Test
    void testHighConcurrencyTokenization() throws InterruptedException {
        FpeEngine engine = new FpeEngine("1234567890123456".getBytes());
        TokenRepository repo = new TokenRepository();
        VaultService service = new VaultService(engine, repo);

        int numberOfRequests = 1000;
        // Use Virtual Threads to simulate high load
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            AtomicInteger successCount = new AtomicInteger(0);

            for (int i = 0; i < numberOfRequests; i++) {
                final String pan = "41110000" + String.format("%08d", i);
                executor.submit(() -> {
                    service.process(new TokenRequest(pan, "test-suite"));
                    successCount.incrementAndGet();
                });
            }
            
            executor.shutdown();
            executor.awaitTermination(10, TimeUnit.SECONDS);
            
            assertEquals(numberOfRequests, successCount.get(), "All requests should process without blocking");
        }
    }
}