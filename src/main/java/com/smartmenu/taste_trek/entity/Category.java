package com.smartmenu.taste_trek.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "menu_category")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    private String categoryId;

    @NotBlank(message = "Category Name is required")
    @Indexed(unique = true)
    private String name;

    // 2-digit numeric code for item identification (e.g., 01, 15)
    @NotBlank(message = "Category Code is required")
    @Pattern(regexp = "^[0-9]{2}$", message = "Category Code must be exactly 2 numeric digits")
    @Indexed(unique = true)
    private String categoryCode;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setTimestamps() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.updatedAt = LocalDateTime.now();
    }
}