package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/otp")
@CrossOrigin(origins = "http://localhost:3000") // Adjust to your frontend URL
public class OtpController {

    @Autowired
    private OtpService otpService;

    // POST: /api/v1/otp/send
    @PostMapping("/send")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");

        if (phoneNumber == null || phoneNumber.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone number is required"));
        }

        try {
            String status = otpService.sendOtp(phoneNumber);
            return ResponseEntity.ok(Map.of("message", "OTP sent successfully", "status", status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST: /api/v1/otp/verify
    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String phoneNumber = request.get("phoneNumber");
        String code = request.get("code");

        if (phoneNumber == null || code == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Phone and Code are required"));
        }

        try {
            boolean isVerified = otpService.verifyOtp(phoneNumber, code);
            if (isVerified) {
                return ResponseEntity.ok(Map.of("message", "Verified", "status", "approved"));
            } else {
                return ResponseEntity.status(400).body(Map.of("error", "Invalid Code"));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}