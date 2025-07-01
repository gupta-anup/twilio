package com.nonstop.twilio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsResponse {
    private boolean success;
    private String message;
    private String messageSid;
}
