package com.nonstop.twilio.controller;

import com.nonstop.twilio.dto.SmsRequest;
import com.nonstop.twilio.dto.SmsResponse;
import com.nonstop.twilio.service.SmsService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for SMS operations.
 * Provides endpoints for sending SMS messages through Twilio.
 */
@RestController
@RequestMapping("/api/v1/sms")
public class SmsController {
    
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);
    
    private final SmsService smsService;
    
    public SmsController(SmsService smsService) {
        this.smsService = smsService;
    }
    
    /**
     * Sends an SMS message.
     *
     * @param smsRequest The SMS request containing recipient and message details
     * @return ResponseEntity with SMS response
     */
    @PostMapping("/send")
    public ResponseEntity<SmsResponse> sendSms(@Valid @RequestBody SmsRequest smsRequest) {
        logger.info("Received SMS send request for recipient: {}", maskPhoneNumber(smsRequest.to()));
        
        SmsResponse response = smsService.sendSms(smsRequest);
        
        if (response.success()) {
            logger.info("SMS sent successfully to: {}", maskPhoneNumber(smsRequest.to()));
            return ResponseEntity.ok(response);
        } else {
            logger.error("Failed to send SMS to: {} - Error: {}", 
                maskPhoneNumber(smsRequest.to()), response.errorMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    /**
     * Health check endpoint for SMS service.
     *
     * @return Service status
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("SMS Service is running");
    }
    
    /**
     * Masks phone number for logging purposes to protect privacy.
     */
    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 6) {
            return "***";
        }
        return phoneNumber.substring(0, 3) + "***" + phoneNumber.substring(phoneNumber.length() - 2);
    }
}
