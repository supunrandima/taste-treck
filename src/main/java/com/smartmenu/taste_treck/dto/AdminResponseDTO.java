package com.smartmenu.taste_treck.dto;

import org.bson.types.ObjectId;
import java.time.LocalDateTime;

public record AdminResponseDTO(
        ObjectId adminId,
        String username,
        String email,
        String phone,
        String address,
        String role,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
