package com.smartmenu.taste_trek.controller;

import com.smartmenu.taste_trek.entity.Order;
import com.smartmenu.taste_trek.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/order")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // CUSTOMER: Place Order
    @PostMapping("/place")
    public ResponseEntity<?> placeOrder(@RequestBody Order order) {
        try {
            Order saved = orderService.placeOrder(order);
            return ResponseEntity.ok(Map.of("orderId", saved.getOrderId(), "message", "Success"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // CUSTOMER: Track Order
    @GetMapping("/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable String orderId) {
        return ResponseEntity.ok(orderService.getOrderByCustomId(orderId));
    }

    // KITCHEN: Get All Active Orders
    @GetMapping("/kitchen")
    public ResponseEntity<List<Order>> getKitchenOrders() {
        return ResponseEntity.ok(orderService.getKitchenOrders());
    }

    // KITCHEN: Update Status (e.g., Start Preparing)
    @PutMapping("/{orderId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable String orderId, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, status));
    }
}