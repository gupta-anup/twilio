package com.nonstop.twilio.integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Integration tests for SMS service.
 */
@SpringBootTest
@ActiveProfiles("test")
class SmsServiceIntegrationTest {

    @Test
    void contextLoads() {
        // Test that the Spring context loads successfully
    }
}
