package com.smartmenu.taste_trek.dto;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public record MenuItemDTO(
        ObjectId itemId,
        String itemCode,
        String name,
        String category,
        String description,
        Double price,
        String imageUrl,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
