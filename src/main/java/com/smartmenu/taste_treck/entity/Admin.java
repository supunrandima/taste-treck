package com.smartmenu.taste_treck.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
    @Id
    private Integer adminId;
    private String username;
    private String password;
    private String email;
    private Integer phone;
    private String address;
    private String role;
    private String status;
    private String createdAt;
    private String updatedAt;

}
