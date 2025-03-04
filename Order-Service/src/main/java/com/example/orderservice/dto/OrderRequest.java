package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Long userId;
    private String shippingAddress;
    private List<OrderItemRequest> orderItems;
}