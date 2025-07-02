package com.nonstop.twilio.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nonstop.twilio.dto.MessageRequest;
import com.nonstop.twilio.dto.MessageResponse;
import com.nonstop.twilio.service.MessagingService;

@RestController
@RequestMapping("/api/messaging")
@CrossOrigin(origins = "*")
public class MessagingController {

    @Value("${facebook.verify-token}")
    private String webhookVerifyToken;
    
    @Autowired
    private MessagingService messagingService;
    
    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(@Valid @RequestBody MessageRequest request) {
        MessageResponse response = messagingService.sendMessage(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/send-bulk")
    public ResponseEntity<List<MessageResponse>> sendBulkMessage(
            @RequestBody List<MessageRequest> requests) {
        
        if (requests == null || requests.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        List<MessageResponse> responses = messagingService.sendBulkMessage(requests);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        List<com.nonstop.twilio.enums.MessageChannel> supportedChannels = messagingService.getSupportedChannels();
        
        return ResponseEntity.ok(Map.of(
            "status", "Messaging service is running",
            "supportedChannels", supportedChannels,
            "totalChannels", supportedChannels.size()
        ));
    }
    
    @GetMapping("/channels")
    public ResponseEntity<List<com.nonstop.twilio.enums.MessageChannel>> getSupportedChannels() {
        return ResponseEntity.ok(messagingService.getSupportedChannels());
    }
    
    // facebook webhook endpoint
    @GetMapping("/webhook/facebook")
    public ResponseEntity<String> verifyFacebookWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {
        
        if ("subscribe".equals(mode) && webhookVerifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        }
        
        return ResponseEntity.status(403).body("Forbidden");
    }
    
    @PostMapping("/webhook/facebook")
    public ResponseEntity<String> handleFacebookWebhook(@RequestBody Map<String, Object> payload) {
        // to handle incoming facebook messages
        return ResponseEntity.ok("OK");
    }
}