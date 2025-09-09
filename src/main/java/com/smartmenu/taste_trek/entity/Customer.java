package com.smartmenu.taste_trek.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "customer")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    private ObjectId customerId;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private LocalDateTime birthday;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
