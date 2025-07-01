package com.nonstop.twilio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import com.twilio.Twilio;

@Configuration
public class TwilioConfig {
    
    @Value("${twilio.account-sid}")
    private String accountSid;
    
    @Value("${twilio.auth-token}")
    private String authToken;
    
    @PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }
}
