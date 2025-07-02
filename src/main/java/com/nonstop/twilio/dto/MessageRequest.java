package com.nonstop.twilio.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.nonstop.twilio.enums.MessageChannel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    
    @NotNull(message = "Channel is required")
    private MessageChannel channel;
    
    @NotBlank(message = "Recipient is required")
    private String recipient; // phone number for SMS/WhatsApp, user ID for facebook
    
    @NotBlank(message = "Message is required")
    private String message;
    
    // optional fields for email
    private String subject;
    private String htmlContent;
}
