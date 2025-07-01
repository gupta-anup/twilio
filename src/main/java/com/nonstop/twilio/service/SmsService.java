package com.nonstop.twilio.service;

import com.nonstop.twilio.dto.SmsRequest;
import com.nonstop.twilio.dto.SmsResponse;

public interface SmsService {
    
    SmsResponse sendSms(SmsRequest smsRequest);
    
    SmsResponse sendBulkSms(String[] phoneNumbers, String message);
}
