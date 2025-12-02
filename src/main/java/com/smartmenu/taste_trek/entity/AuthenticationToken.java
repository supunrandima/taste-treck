package com.smartmenu.taste_trek.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "authentication_tokens")
public class AuthenticationToken {

    @Id

    private Long tokenId;
    private String tokenValue;
    private LocalDateTime tokenCreationDateTime;


    @DBRef
    private Customer customer;

    @DBRef
    private Admin admin;

    @DBRef
    private  Staff staff;


    public AuthenticationToken(Customer customer){
        this.customer = customer;
        this.tokenValue = UUID.randomUUID().toString();
        this.tokenCreationDateTime = LocalDateTime.now();
    }

    public AuthenticationToken(Admin admin){
        this.admin = admin;
        this.tokenValue = UUID.randomUUID().toString();
        this.tokenCreationDateTime = LocalDateTime.now();
    }

    public AuthenticationToken(Staff staff) {
        this.staff = staff;
        this.tokenValue = UUID.randomUUID().toString();
        this.tokenCreationDateTime = LocalDateTime.now();
    }
}
