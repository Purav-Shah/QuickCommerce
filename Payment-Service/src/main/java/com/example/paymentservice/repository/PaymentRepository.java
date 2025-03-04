package com.example.paymentservice.repository;

import com.example.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {
    List<Payment> findByOrderId(Long orderId);
    List<Payment> findByUserId(Long userId);
}