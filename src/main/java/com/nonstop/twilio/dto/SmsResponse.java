package com.nonstop.twilio.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for SMS send responses.
 * Contains the result of SMS sending operation.
 */
public record SmsResponse(
        String sid,
        String to,
        String from,
        String body,
        String status,
        String errorCode,
        String errorMessage,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime sentAt,
        boolean success,
        String message
) {
    
    /**
     * Creates a successful SMS response.
     */
    public static SmsResponse success(String sid, String to, String from, String body, String status, LocalDateTime sentAt) {
        return new SmsResponse(sid, to, from, body, status, null, null, sentAt, true, "SMS sent successfully");
    }
    
    /**
     * Creates a failed SMS response.
     */
    public static SmsResponse failure(String to, String errorCode, String errorMessage) {
        return new SmsResponse(null, to, null, null, "failed", errorCode, errorMessage, LocalDateTime.now(), false, "Failed to send SMS");
    }
}
