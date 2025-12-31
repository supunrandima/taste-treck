package com.smartmenu.taste_trek.config;

import com.twilio.Twilio;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfig {

    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;

    @PostConstruct
    public void init() {
        // Initialize Twilio once on startup
        try {
            Twilio.init(accountSid, authToken);
            System.out.println("Twilio Initialized Successfully with Account SID: " + accountSid);
        } catch (Exception e) {
            System.err.println("Failed to initialize Twilio: " + e.getMessage());
        }
    }
}