package com.nonstop.twilio.dto;

import com.nonstop.twilio.enums.MessageChannel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {
    private boolean success;
    private String message;
    private String messageId;
    private MessageChannel channel;
    private String recipient;
}
