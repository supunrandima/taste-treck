package com.smartmenu.taste_trek.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*")
public class GuestController {

    @PostMapping("/guest-login")
    public ResponseEntity<?> createGuestSession() {
        // 1. Generate a random ID for this guest session
        String guestId = "GUEST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 2. Return it (In a real app, you would wrap this in a JWT)
        // For now, we return a simple JSON object that matches your Login response structure
        return ResponseEntity.ok(Map.of(
                "token", "guest-token-" + UUID.randomUUID().toString(), // Dummy token for consistency
                "customerId", guestId, // This is the temporary ID
                "name", "Guest",
                "role", "GUEST"
        ));
    }
}