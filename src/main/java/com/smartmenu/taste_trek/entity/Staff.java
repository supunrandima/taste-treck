package com.smartmenu.taste_trek.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "staff")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    private ObjectId id;

    @NotBlank(message = "First Name is required")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    private String lastName;

    @NotBlank(message = "Staff ID is required")
    @Indexed(unique = true)
    private String staffId;

    @NotBlank(message = "Password is required")
    private String password;


    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be valid")
    @Indexed(unique = true)
    private String phone;

    @NotBlank(message = "Address is required")
    private String address;


    private String role;

    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void setTimestamps() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

}
