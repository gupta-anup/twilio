package com.nonstop.twilio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageChannel {
    SMS("sms"),
    WHATSAPP("whatsapp"),
    FACEBOOK("facebook"),
    EMAIL("email");
    
    private final String value;
    
    public static MessageChannel fromString(String channel) {
        for (MessageChannel c : MessageChannel.values()) {
            if (c.value.equalsIgnoreCase(channel)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown channel: " + channel);
    }
}
