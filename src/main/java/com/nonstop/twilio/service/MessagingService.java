package com.nonstop.twilio.service;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nonstop.twilio.dto.MessageRequest;
import com.nonstop.twilio.dto.MessageResponse;
import com.nonstop.twilio.enums.MessageChannel;

// coordinator service that routes message to appropriate channel-specific implementation
@Service
public class MessagingService {
    
    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);
    
    @Autowired
    private List<MessageService> messageServices;
    
    // send a message using the appropriate service
    public MessageResponse sendMessage(MessageRequest request) {
        logger.info("Sending {} message to {}", request.getChannel(), request.getRecipient());
        
        MessageService service = getServiceForChannel(request.getChannel());
        if (service == null) {
            logger.error("No service found for channel: {}", request.getChannel());
            return new MessageResponse(
                false, 
                "Unsupported channel: " + request.getChannel(), 
                null, 
                request.getChannel(), 
                request.getRecipient()
            );
        }
        
        return service.sendMessage(request);
    }
    
    // send bulk messages using appropriate services based on each request's channel
    public List<MessageResponse> sendBulkMessage(List<MessageRequest> requests) {
        logger.info("Sending bulk messages: {} requests", requests.size());
        
        return requests.stream()
                .map(this::sendMessage)
                .collect(Collectors.toList());
    }
    
    // find the appropriate service for the given channel
    private MessageService getServiceForChannel(MessageChannel channel) {
        return messageServices.stream()
                .filter(service -> service.supports(channel))
                .findFirst()
                .orElse(null);
    }
    
    // get all supported channels
    public List<MessageChannel> getSupportedChannels() {
        return messageServices.stream()
                .flatMap(service -> 
                    List.of(MessageChannel.values()).stream()
                        .filter(service::supports)
                )
                .distinct()
                .collect(Collectors.toList());
    }
}
