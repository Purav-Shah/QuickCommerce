package com.example.paymentservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    private String paymentId;

    private Long orderId;
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status; // PENDING, COMPLETED, FAILED
    private LocalDateTime paymentDate;
    private String transactionDetails;
}