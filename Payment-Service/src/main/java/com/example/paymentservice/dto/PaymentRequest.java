package com.example.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {
    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
}