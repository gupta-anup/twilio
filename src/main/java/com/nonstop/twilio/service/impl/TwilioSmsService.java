package com.nonstop.twilio.service.impl;

import com.nonstop.twilio.config.TwilioProperties;
import com.nonstop.twilio.dto.SmsRequest;
import com.nonstop.twilio.dto.SmsResponse;
import com.nonstop.twilio.service.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Implementation of SMS service using Twilio API.
 * Handles SMS sending operations with proper error handling and logging.
 */
@Service
public class TwilioSmsService implements SmsService {
    
    private static final Logger logger = LoggerFactory.getLogger(TwilioSmsService.class);
    
    private final TwilioProperties twilioProperties;
    
    public TwilioSmsService(TwilioProperties twilioProperties) {
        this.twilioProperties = twilioProperties;
    }
    
    @Override
    public SmsResponse sendSms(SmsRequest smsRequest) {
        return sendSms(smsRequest.to(), smsRequest.message());
    }
    
    @Override
    public SmsResponse sendSms(String to, String messageBody) {
        try {
            logger.info("Attempting to send SMS to: {}", maskPhoneNumber(to));
            
            Message message = Message.creator(
                    new PhoneNumber(to),
                    new PhoneNumber(twilioProperties.phoneNumber()),
                    messageBody
            ).create();
            
            LocalDateTime sentAt = message.getDateCreated() != null 
                ? message.getDateCreated().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
                : LocalDateTime.now();
                
            logger.info("SMS sent successfully. SID: {}, Status: {}", message.getSid(), message.getStatus());
            
            return SmsResponse.success(
                message.getSid(),
                to,
                twilioProperties.phoneNumber(),
                messageBody,
                message.getStatus().toString(),
                sentAt
            );
            
        } catch (Exception e) {
            logger.error("Failed to send SMS to {}: {}", maskPhoneNumber(to), e.getMessage(), e);
            return SmsResponse.failure(to, "SEND_ERROR", e.getMessage());
        }
    }
    
    /**
     * Masks phone number for logging purposes to protect privacy.
     * Shows only first 3 and last 2 digits.
     */
    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 6) {
            return "***";
        }
        return phoneNumber.substring(0, 3) + "***" + phoneNumber.substring(phoneNumber.length() - 2);
    }
}
