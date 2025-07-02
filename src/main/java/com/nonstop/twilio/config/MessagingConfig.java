package com.nonstop.twilio.config;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import com.twilio.Twilio;

@Configuration
public class MessagingConfig {
    
    @Value("${messaging.twilio.account-sid}")
    private String twilioAccountSid;
    
    @Value("${messaging.twilio.auth-token}")
    private String twilioAuthToken;
    
    @PostConstruct
    public void initTwilio() {
        Twilio.init(twilioAccountSid, twilioAuthToken);
    }
    
    // for making http requests (we need it for facebook messenger)
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();
    }
}
