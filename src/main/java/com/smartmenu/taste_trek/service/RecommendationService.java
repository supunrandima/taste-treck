package com.smartmenu.taste_trek.service;

import com.smartmenu.taste_trek.entity.MenuItem;
import com.smartmenu.taste_trek.entity.Order;
import com.smartmenu.taste_trek.entity.OrderItem;
import com.smartmenu.taste_trek.repository.MenuItemRepo;
import com.smartmenu.taste_trek.repository.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private MenuItemRepo menuItemRepo;

    public List<MenuItem> getRecommendations(String customerId) {
        // 1. GUEST LOGIC: Return Generic "Trending" (Top 4 items)
        if (customerId == null || customerId.startsWith("GUEST-")) {
            return menuItemRepo.findAll().stream().limit(4).collect(Collectors.toList());
        }

        // 2. REGISTERED LOGIC: Personalized
        List<Order> pastOrders = orderRepo.findByCustomerId(customerId);

        if (pastOrders.isEmpty()) {
            return menuItemRepo.findAll().stream().limit(4).collect(Collectors.toList());
        }

        // Analyze frequency of categories ordered
        Map<String, Integer> categoryCount = new HashMap<>();

        for (Order order : pastOrders) {
            if (order.getItems() != null) {
                for (OrderItem item : order.getItems()) {
                    // We need to look up the item to know its category
                    Optional<MenuItem> menuVal = menuItemRepo.findById(item.getMenuItemId());
                    menuVal.ifPresent(m ->
                            categoryCount.put(m.getCategory(), categoryCount.getOrDefault(m.getCategory(), 0) + 1)
                    );
                }
            }
        }

        if (categoryCount.isEmpty()) {
            return menuItemRepo.findAll().stream().limit(4).collect(Collectors.toList());
        }

        // Find the favorite category
        String favoriteCategory = categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("All");

        // Return items from that category
        return menuItemRepo.findByCategory(favoriteCategory).stream()
                .limit(4)
                .collect(Collectors.toList());
    }
}