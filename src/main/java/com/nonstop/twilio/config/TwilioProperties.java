package com.nonstop.twilio.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

/**
 * Configuration properties for Twilio SMS service.
 * This class binds Twilio-related configuration from application.yaml
 */
@ConfigurationProperties(prefix = "twilio")
@Validated
public record TwilioProperties(
        @NotBlank(message = "Account SID is required")
        String accountSid,
        
        @NotBlank(message = "Auth token is required")
        String authToken,
        
        @NotBlank(message = "Phone number is required")
        String phoneNumber
) {
    @ConstructorBinding
    public TwilioProperties {
        // Constructor validation is handled by annotations
    }
}
