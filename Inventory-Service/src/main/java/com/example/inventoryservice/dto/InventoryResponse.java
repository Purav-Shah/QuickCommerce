package com.example.inventoryservice.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {
    private Long productId;
    private String productName;
    private boolean isInStock;
    private Integer availableQuantity;
}