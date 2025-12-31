package com.smartmenu.taste_trek.dto;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;


public record AdminDTO(
        String adminId,
        String firstName,
        String lastName,
        String userId,
        String password,
        String email,
        String phone,
        String address,
        String role,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
