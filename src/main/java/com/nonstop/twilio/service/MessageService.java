package com.nonstop.twilio.service;

import com.nonstop.twilio.dto.MessageRequest;
import com.nonstop.twilio.dto.MessageResponse;
import com.nonstop.twilio.enums.MessageChannel;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest request);
    boolean supports(MessageChannel channel);
}