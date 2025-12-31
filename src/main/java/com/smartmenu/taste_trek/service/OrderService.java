package com.smartmenu.taste_trek.service;

import com.smartmenu.taste_trek.entity.Order;
import com.smartmenu.taste_trek.entity.OrderItem;
import com.smartmenu.taste_trek.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    public Order placeOrder(Order orderRequest) {
        // 1. Generate unique 5-character Order ID
        String customId = generateShortId();
        orderRequest.setOrderId(customId);

        // 2. Calculate Total Amount based on items
        double total = 0.0;
        if (orderRequest.getItems() != null) {
            for (OrderItem item : orderRequest.getItems()) {
                total += item.getUnitPrice() * item.getQuantity();
            }
        }
        orderRequest.setTotalAmount(total);

        // 3. Set Defaults
        orderRequest.setStatus("PENDING");
        orderRequest.setTimestamps();

        return orderRepo.save(orderRequest);
    }

    public List<Order> getKitchenOrders() {
        return orderRepo.findByStatusIn(Arrays.asList("PENDING", "PREPARING", "READY"));
    }

    public Order updateStatus(String orderId, String newStatus) {
        Order order = getOrderByCustomId(orderId);
        order.setStatus(newStatus);
        order.setTimestamps();
        return orderRepo.save(order);
    }

    public Order getOrderByCustomId(String orderId) {
        return orderRepo.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // Helper to generate 5-char ID (e.g., "7XK92")
    private String generateShortId() {
        return UUID.randomUUID().toString().substring(0, 5).toUpperCase();
    }
}