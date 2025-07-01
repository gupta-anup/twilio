package com.nonstop.twilio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nonstop.twilio.dto.SmsResponse;
import com.nonstop.twilio.dto.SmsRequest;
import com.nonstop.twilio.service.SmsService;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/sms")
@CrossOrigin(origins = "*")
public class SmsController {
    
    @Autowired
    private SmsService smsService;
    
    @PostMapping("/send")
    public ResponseEntity<SmsResponse> sendSms(@Valid @RequestBody SmsRequest smsRequest) {
        SmsResponse response = smsService.sendSms(smsRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/send-bulk")
    public ResponseEntity<SmsResponse> sendBulkSms(@RequestBody Map<String, Object> request) {
        String[] phoneNumbers = (String[]) request.get("phoneNumbers");
        String message = (String) request.get("message");
        
        if (phoneNumbers == null || phoneNumbers.length == 0) {
            return ResponseEntity.badRequest().body(
                new SmsResponse(false, "Phone numbers are required", null)
            );
        }
        
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(
                new SmsResponse(false, "Message is required", null)
            );
        }
        
        SmsResponse response = smsService.sendBulkSms(phoneNumbers, message);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("SMS service is running");
    }
}