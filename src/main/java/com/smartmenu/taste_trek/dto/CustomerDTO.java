package com.smartmenu.taste_trek.dto;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public record CustomerDTO(
         ObjectId customerId,
         String firstName,
         String lastName,
         String phone,
         String email,
         LocalDateTime birthday,
         String status,
         LocalDateTime createdAt,
         LocalDateTime updatedAt
) {
}
