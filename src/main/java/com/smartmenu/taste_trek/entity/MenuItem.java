package com.smartmenu.taste_trek.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "menu_item")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItem {
    @Id
    private ObjectId itemId;

    @NotBlank(message = "Meal name is required")
    @Indexed(unique = true) // Ensures no duplicate meal names
    private String name;

    @NotBlank(message = "Item Code is required")
    @Indexed(unique = true)
    @Size(min = 4,max = 4)
    private String itemCode;

    @NotBlank(message = "Category is required")
    private String category; // e.g., "Main Course", "Appetizer"

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be a positive value")
    private Double price;

    // Stores the URL or path to the image uploaded by the admin
    private String imageUrl;

    // Status can be used for visibility (e.g., 'Available', 'Out of Stock')
    private String status = "Available";

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Optional: Setter for timestamps, similar to the Customer entity structure
    public void setTimestamps() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
}
