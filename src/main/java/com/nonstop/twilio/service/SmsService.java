package com.nonstop.twilio.service;

import com.nonstop.twilio.dto.SmsRequest;
import com.nonstop.twilio.dto.SmsResponse;

/**
 * Service interface for SMS operations.
 * Defines contract for SMS sending functionality.
 */
public interface SmsService {
    
    /**
     * Sends an SMS message to the specified recipient.
     *
     * @param smsRequest The SMS request containing recipient and message details
     * @return SmsResponse containing the result of the send operation
     */
    SmsResponse sendSms(SmsRequest smsRequest);
    
    /**
     * Sends a simple SMS message.
     *
     * @param to      The recipient phone number in E.164 format
     * @param message The message content
     * @return SmsResponse containing the result of the send operation
     */
    SmsResponse sendSms(String to, String message);
}
