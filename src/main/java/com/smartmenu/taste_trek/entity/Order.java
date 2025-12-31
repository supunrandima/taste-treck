package com.smartmenu.taste_trek.entity;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "order")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    private String id;

    @Indexed(unique = true)
    @Size(min = 5, max = 5)
    private String orderId; // Custom Business ID (e.g., "A1B2C")

    private List<OrderItem> items; // Embedded Class

    private Double totalAmount;

    private String status = "PENDING"; // PENDING, PREPARING, READY, SERVED
    private String orderType;          // TABLE, TAKEOUT

    private String tableNumber;

    // --- ADDED THESE TO LINK WITH FRONTEND ---
    private String customerId;    // Link to the logged-in user
    private String customerPhone; // For notifications/tracking
    // -----------------------------------------

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setTimestamps() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
}