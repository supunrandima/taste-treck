package com.smartmenu.taste_trek.service;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Value("${twilio.service_sid}")
    private String serviceSid;

    public String sendOtp(String phoneNumber) {
        try {
            Verification verification = Verification.creator(
                            serviceSid,
                            phoneNumber,
                            "sms")
                    .create();
            return verification.getStatus();
        } catch (Exception e) {
            throw new RuntimeException("Twilio Send Error: " + e.getMessage());
        }
    }

    public boolean verifyOtp(String phoneNumber, String code) {
        try {

            VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid)
                    .setCode(code)
                    .setTo(phoneNumber)
                    .create();

            return "approved".equals(verificationCheck.getStatus());
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Twilio Verify Error: " + e.getMessage());
            return false;
        }
    }
}