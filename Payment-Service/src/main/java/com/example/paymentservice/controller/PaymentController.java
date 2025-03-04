package com.example.paymentservice.controller;

import com.example.paymentservice.dto.PaymentRequest;
import com.example.paymentservice.dto.PaymentResponse;
import com.example.paymentservice.model.Payment;
import com.example.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/process")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponse processPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.processPayment(paymentRequest);
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable String paymentId) {
        return ResponseEntity.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<Payment>> getPaymentsByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentsByOrderId(orderId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Payment>> getPaymentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getPaymentsByUserId(userId));
    }
}
