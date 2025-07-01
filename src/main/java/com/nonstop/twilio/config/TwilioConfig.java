package com.nonstop.twilio.config;

import com.twilio.Twilio;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Twilio SMS service.
 * Initializes Twilio client with provided credentials.
 */
@Configuration
@EnableConfigurationProperties(TwilioProperties.class)
public class TwilioConfig {

    /**
     * Initializes Twilio client with account credentials.
     *
     * @param twilioProperties Configuration properties containing Twilio credentials
     * @return String indicating successful initialization
     */
    @Bean
    public String twilioInit(TwilioProperties twilioProperties) {
        Twilio.init(twilioProperties.accountSid(), twilioProperties.authToken());
        return "Twilio Initialized";
    }
}
