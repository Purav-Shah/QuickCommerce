package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long productId;
    private String productName;
    private boolean isInStock;
    private Integer availableQuantity;
}