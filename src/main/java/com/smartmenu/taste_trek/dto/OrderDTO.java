package com.smartmenu.taste_trek.dto;

import com.smartmenu.taste_trek.entity.OrderItem;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDTO(

        String id,

        String orderId,

        @NotNull(message = "Order items list cannot be null")
        List<OrderItem> items,

        Double totalAmount,

        @NotBlank(message = "Order type is required (TABLE, TAKEOUT, DELIVERY)")
        String orderType,

        String tableNumber,

        String status,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}