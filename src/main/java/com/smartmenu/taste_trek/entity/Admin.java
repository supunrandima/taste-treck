package com.smartmenu.taste_trek.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    private ObjectId adminId;
    private String firstName;
    private String lastName;
    private String userId;
    private String password;
    private String email;
    private String phone;
    private String address;
    private String role;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
