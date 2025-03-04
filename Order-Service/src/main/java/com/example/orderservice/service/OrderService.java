package com.example.orderservice.service;

import com.example.orderservice.client.InventoryServiceClient;
import com.example.orderservice.client.PaymentServiceClient;
import com.example.orderservice.client.ProductServiceClient;
import com.example.orderservice.dto.*;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductServiceClient productServiceClient;
    private final InventoryServiceClient inventoryServiceClient;
    private final PaymentServiceClient paymentServiceClient;

    @Transactional
    public Order createOrder(OrderRequest orderRequest) {
        // Check if products are in stock
        List<Long> productIds = orderRequest.getOrderItems().stream()
                .map(OrderItemRequest::getProductId)
                .collect(Collectors.toList());

        List<InventoryResponse> inventoryResponses = inventoryServiceClient.checkInventory(productIds);

        // Check if all products are in stock
        boolean allProductsInStock = inventoryResponses.stream()
                .allMatch(InventoryResponse::isInStock);

        if (!allProductsInStock) {
            throw new IllegalArgumentException("Some products are out of stock");
        }

        // Create order
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setShippingAddress(orderRequest.getShippingAddress());

        // Get product details and create order items
        List<OrderItem> orderItems = orderRequest.getOrderItems().stream()
                .map(itemRequest -> {
                    ProductResponse product = productServiceClient.getProductById(itemRequest.getProductId());

                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(itemRequest.getProductId());
                    orderItem.setProductName(product.getName());
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setPrice(product.getPrice());
                    orderItem.setOrder(order);

                    // Update inventory
                    inventoryServiceClient.updateInventory(itemRequest.getProductId(), -itemRequest.getQuantity());

                    return orderItem;
                })
                .collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // Calculate total amount
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Process payment
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUserId())
                .amount(savedOrder.getTotalAmount())
                .paymentMethod("CREDIT_CARD") // Default payment method
                .build();

        PaymentResponse paymentResponse = paymentServiceClient.processPayment(paymentRequest);

        // Update order with payment details
        savedOrder.setPaymentId(paymentResponse.getPaymentId());

        if ("COMPLETED".equals(paymentResponse.getStatus())) {
            savedOrder.setStatus("PAID");
        } else {
            savedOrder.setStatus("PAYMENT_FAILED");
        }

        return orderRepository.save(savedOrder);
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
    }

    public Order updateOrderStatus(Long id, String status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
