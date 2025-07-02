package com.nonstop.twilio.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.nonstop.twilio.dto.MessageRequest;
import com.nonstop.twilio.dto.MessageResponse;
import com.nonstop.twilio.enums.MessageChannel;
import com.nonstop.twilio.service.MessageService;

@Service
public class FacebookMessengerService implements MessageService {
    
    private static final Logger logger = LoggerFactory.getLogger(FacebookMessengerService.class);
    
    @Value("${facebook.page-access-token}")
    private String pageAccessToken;
    
    @Value("${facebook.api-version}")
    private String apiVersion;
    
    @Autowired
    private WebClient webClient;
    
    @Override
    public MessageResponse sendMessage(MessageRequest request) {
        try {
            String url = String.format("https://graph.facebook.com/%s/me/messages", apiVersion);
            
            Map<String, Object> payload = createMessagePayload(request.getRecipient(), request.getMessage());
            
            @SuppressWarnings("unchecked")
            Map<String, Object> response = webClient.post()
                    .uri(url)
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();
            
            String messageId = response != null ? (String) response.get("message_id") : null;
            
            logger.info("Facebook message sent successfully to {}. Message ID: {}", 
                       request.getRecipient(), messageId);
            
            return new MessageResponse(
                    true, 
                    "Facebook message sent successfully", 
                    messageId,
                    MessageChannel.FACEBOOK,
                    request.getRecipient()
            );
            
        } catch (Exception e) {
            logger.error("Failed to send Facebook message to {}: {}", 
                        request.getRecipient(), e.getMessage());
            
            return new MessageResponse(
                    false, 
                    "Failed to send Facebook message: " + e.getMessage(), 
                    null,
                    MessageChannel.FACEBOOK,
                    request.getRecipient()
            );
        }
    }
    
    private Map<String, Object> createMessagePayload(String recipientId, String messageText) {
        Map<String, Object> payload = new HashMap<>();
        
        Map<String, String> recipient = new HashMap<>();
        recipient.put("id", recipientId);
        
        Map<String, String> message = new HashMap<>();
        message.put("text", messageText);
        
        payload.put("recipient", recipient);
        payload.put("message", message);
        payload.put("access_token", pageAccessToken);
        
        return payload;
    }
    
    @Override
    public boolean supports(MessageChannel channel) {
        return MessageChannel.FACEBOOK.equals(channel);
    }
}
