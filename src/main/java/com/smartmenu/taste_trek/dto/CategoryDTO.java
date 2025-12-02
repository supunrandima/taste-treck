package com.smartmenu.taste_trek.dto;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public record CategoryDTO(
        ObjectId categoryId,
        String name,
        String categoryCode,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
