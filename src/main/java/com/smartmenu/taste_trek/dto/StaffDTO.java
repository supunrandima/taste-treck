package com.smartmenu.taste_trek.dto;


import java.time.LocalDateTime;


public record StaffDTO(
        String id,
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
