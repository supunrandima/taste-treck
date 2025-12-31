package com.smartmenu.taste_trek.dto;

import java.time.LocalDateTime;

public record StaffResponseDTO(
        String id,
        String firstName,
        String lastName,
        String staffId,
        String phone,
        String address,
        String role,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
