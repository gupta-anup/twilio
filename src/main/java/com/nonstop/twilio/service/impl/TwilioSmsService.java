package com.nonstop.twilio.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nonstop.twilio.dto.SmsRequest;
import com.nonstop.twilio.dto.SmsResponse;
import com.nonstop.twilio.service.SmsService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class TwilioSmsService implements SmsService {
    
    private static final Logger logger = LoggerFactory.getLogger(TwilioSmsService.class);
    
    @Value("${twilio.phone-number}")
    private String fromPhoneNumber;
    
    @Override
    public SmsResponse sendSms(SmsRequest smsRequest) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(smsRequest.getPhoneNumber()),
                    new PhoneNumber(fromPhoneNumber),
                    smsRequest.getMessage()
            ).create();
            
            logger.info("SMS sent successfully. SID: {}", message.getSid());
            
            return new SmsResponse(
                    true, 
                    "SMS sent successfully", 
                    message.getSid()
            );
            
        } catch (Exception e) {
            logger.error("Failed to send SMS: {}", e.getMessage());
            
            return new SmsResponse(
                    false, 
                    "Failed to send SMS: " + e.getMessage(), 
                    null
            );
        }
    }
    
    @Override
    public SmsResponse sendBulkSms(String[] phoneNumbers, String message) {
        int successCount = 0;
        int failureCount = 0;
        StringBuilder errors = new StringBuilder();
        
        for (String phoneNumber : phoneNumbers) {
            try {
                Message msg = Message.creator(
                        new PhoneNumber(phoneNumber),
                        new PhoneNumber(fromPhoneNumber),
                        message
                ).create();
                
                successCount++;
                logger.info("SMS sent to {}, SID: {}", phoneNumber, msg.getSid());
                
            } catch (Exception e) {
                failureCount++;
                errors.append("Failed to send to ").append(phoneNumber)
                      .append(": ").append(e.getMessage()).append("; ");
                logger.error("Failed to send SMS to {}: {}", phoneNumber, e.getMessage());
            }
        }
        
        String responseMessage = String.format(
                "Bulk SMS completed. Success: %d, Failed: %d", 
                successCount, failureCount
        );
        
        if (failureCount > 0) {
            responseMessage += ". Errors: " + errors.toString();
        }
        
        return new SmsResponse(
                failureCount == 0, 
                responseMessage, 
                null
        );
    }
}