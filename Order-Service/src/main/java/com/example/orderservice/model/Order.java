package com.example.orderservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String paymentId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();
}