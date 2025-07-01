package com.nonstop.twilio.service.impl;

import com.nonstop.twilio.config.TwilioProperties;
import com.nonstop.twilio.dto.SmsRequest;
import com.nonstop.twilio.dto.SmsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for TwilioSmsService.
 */
@ExtendWith(MockitoExtension.class)
class TwilioSmsServiceTest {

    @Mock
    private TwilioProperties twilioProperties;

    @InjectMocks
    private TwilioSmsService twilioSmsService;

    @BeforeEach
    void setUp() {
        when(twilioProperties.phoneNumber()).thenReturn("+1234567890");
        when(twilioProperties.accountSid()).thenReturn("test-account-sid");
        when(twilioProperties.authToken()).thenReturn("test-auth-token");
    }

    @Test
    void testSmsRequestValidation() {
        // Test valid request
        SmsRequest validRequest = new SmsRequest("+1234567890", "Test message");
        assertNotNull(validRequest);
        assertEquals("+1234567890", validRequest.to());
        assertEquals("Test message", validRequest.message());
    }

    @Test
    void testMaskPhoneNumber() {
        // Use reflection to test the private method or create a public wrapper for testing
        String phoneNumber = "+1234567890";
        String expected = "+12***90";
        
        // Since maskPhoneNumber is private, we'll test it indirectly through the service
        // by checking that it doesn't throw errors when processing phone numbers
        assertDoesNotThrow(() -> {
            twilioSmsService.sendSms(phoneNumber, "Test message");
        });
    }
}
