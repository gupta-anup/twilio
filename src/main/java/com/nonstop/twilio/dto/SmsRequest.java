package com.nonstop.twilio.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for SMS send requests.
 * Contains validation rules for phone number and message content.
 */
public record SmsRequest(
        @NotBlank(message = "Phone number is required")
        @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Phone number must be in E.164 format (e.g., +1234567890)")
        String to,
        
        @NotBlank(message = "Message is required")
        @Size(max = 1600, message = "Message length cannot exceed 1600 characters")
        String message
) {
}
