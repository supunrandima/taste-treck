package com.smartmenu.taste_trek.dto;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;


public record StaffDTO(
        ObjectId id,
        String firstName,
        String lastName,
        String staffId,
        String password,
        String phone,
        String address,
        String role,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
