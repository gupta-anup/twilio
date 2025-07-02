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
public class WhatsAppService implements MessageService {
    
    private static final Logger logger = LoggerFactory.getLogger(WhatsAppService.class);
    
    @Value("${messaging.twilio.whatsapp-number}")
    private String fromWhatsAppNumber;
    
    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        try {
            String toNumber = request.getRecipient().startsWith("whatsapp:") 
                    ? request.getRecipient() 
                    : "whatsapp:" + request.getRecipient();
            
            Message message = Message.creator(
                    new PhoneNumber(toNumber),
                    new PhoneNumber(fromWhatsAppNumber),
                    request.getMessage()
            ).create();
            
            logger.info("WhatsApp message sent successfully to {}. SID: {}", 
                       request.getRecipient(), message.getSid());
            
            return new MessageResponse(
                    true, 
                    "WhatsApp message sent successfully", 
                    message.getSid(),
                    MessageChannel.WHATSAPP,
                    request.getRecipient()
            );
            
        } catch (Exception e) {
            logger.error("Failed to send WhatsApp message to {}: {}", 
                        request.getRecipient(), e.getMessage());
            
            return new MessageResponse(
                    false, 
                    "Failed to send WhatsApp message: " + e.getMessage(), 
                    null,
                    MessageChannel.WHATSAPP,
                    request.getRecipient()
            );
        }
    }
    
    @Override
    public boolean supports(MessageChannel channel) {
        return MessageChannel.WHATSAPP.equals(channel);
    }
}
