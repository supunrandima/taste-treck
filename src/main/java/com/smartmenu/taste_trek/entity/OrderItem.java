package com.smartmenu.taste_trek.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private String menuItemId;
    private String name;
    private String itemCode;
    private Double unitPrice;
    private Integer quantity;
}