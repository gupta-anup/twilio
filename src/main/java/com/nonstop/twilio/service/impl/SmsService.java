package com.nonstop.twilio.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.nonstop.twilio.dto.MessageRequest;
import com.nonstop.twilio.dto.MessageResponse;
import com.nonstop.twilio.enums.MessageChannel;
import com.nonstop.twilio.service.MessageService;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsService implements MessageService {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);
    
    @Value("${messaging.twilio.phone-number}")
    private String fromPhoneNumber;
    
    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(request.getRecipient()),
                    new PhoneNumber(fromPhoneNumber),
                    request.getMessage()
            ).create();
            
            logger.info("SMS sent successfully to {}. SID: {}", request.getRecipient(), message.getSid());
            
            return new MessageResponse(
                    true, 
                    "SMS sent successfully", 
                    message.getSid(),
                    MessageChannel.SMS,
                    request.getRecipient()
            );
            
        } catch (Exception e) {
            logger.error("Failed to send SMS to {}: {}", request.getRecipient(), e.getMessage());
            
            return new MessageResponse(
                    false, 
                    "Failed to send SMS: " + e.getMessage(), 
                    null,
                    MessageChannel.SMS,
                    request.getRecipient()
            );
        }
    }

    @Override
    public boolean supports(MessageChannel channel) {
        return MessageChannel.SMS.equals(channel);
    }
}