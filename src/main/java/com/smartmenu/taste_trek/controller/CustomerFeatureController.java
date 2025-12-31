package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.entity.MenuItem;
import com.smartmenu.taste_trek.entity.Order;
import com.smartmenu.taste_trek.repository.OrderRepo;
import com.smartmenu.taste_trek.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer-features")
@CrossOrigin(origins = "*")
public class CustomerFeatureController {

    @Autowired
    private RecommendationService recommendationService;

    @Autowired
    private OrderRepo orderRepo;

    // 1. Get Recommendations (Smart: Works for Guest & User)
    @GetMapping("/recommendations/{customerId}")
    public ResponseEntity<List<MenuItem>> getRecommendations(@PathVariable String customerId) {
        return ResponseEntity.ok(recommendationService.getRecommendations(customerId));
    }

    // 2. Get Order History (Secured: Users Only)
    @GetMapping("/history/{customerId}")
    public ResponseEntity<?> getOrderHistory(@PathVariable String customerId) {
        if (customerId.startsWith("GUEST-")) {
            return ResponseEntity.status(403).body("Guests cannot view history.");
        }
        // Reverse list to show newest first
        List<Order> history = orderRepo.findByCustomerId(customerId);
        return ResponseEntity.ok(history);
    }
}