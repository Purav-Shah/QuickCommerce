package com.example.paymentservice.service;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentResponse processPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment for order: {}", paymentRequest.getOrderId());

        try {
            // In a real application, you would integrate with a payment gateway here
            // For simulation, we'll randomly succeed or fail (90% success rate)
            boolean paymentSuccessful = Math.random() < 0.9;

            Payment payment = new Payment();
            payment.setPaymentId(UUID.randomUUID().toString());
            payment.setOrderId(paymentRequest.getOrderId());
            payment.setUserId(paymentRequest.getUserId());
            payment.setAmount(paymentRequest.getAmount());
            payment.setPaymentMethod(paymentRequest.getPaymentMethod());
            payment.setPaymentDate(LocalDateTime.now());

            if (paymentSuccessful) {
                payment.setStatus("COMPLETED");
                payment.setTransactionDetails("Payment processed successfully");

                paymentRepository.save(payment);

                return PaymentResponse.builder()
                        .paymentId(payment.getPaymentId())
                        .status("COMPLETED")
                        .message("Payment processed successfully")
                        .build();
            } else {
                payment.setStatus("FAILED");
                payment.setTransactionDetails("Payment declined by the payment processor");

                paymentRepository.save(payment);

                return PaymentResponse.builder()
                        .paymentId(payment.getPaymentId())
                        .status("FAILED")
                        .message("Payment declined by the payment processor")
                        .build();
            }
        } catch (Exception e) {
            log.error("Error processing payment: {}", e.getMessage());

            return PaymentResponse.builder()
                    .paymentId(null)
                    .status("ERROR")
                    .message("An error occurred while processing payment: " + e.getMessage())
                    .build();
        }
    }

    public Payment getPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with id: " + paymentId));
    }

    public List<Payment> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    public List<Payment> getPaymentsByUserId(Long userId) {
        return paymentRepository.findByUserId(userId);
    }
}
