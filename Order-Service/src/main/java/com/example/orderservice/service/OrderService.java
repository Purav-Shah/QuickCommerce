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
import java.util.ArrayList;
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
        log.info("Creating order for user: {}", orderRequest.getUserId());
        
        // Check if products are in stock and get their details
        List<OrderItemRequest> orderItemRequests = orderRequest.getOrderItems();
        List<Long> productIds = orderItemRequests.stream()
                .map(OrderItemRequest::getProductId)
                .collect(Collectors.toList());

        log.info("Checking inventory for products: {}", productIds);
        List<InventoryResponse> inventoryResponses = inventoryServiceClient.checkInventory(productIds);

        // Validate inventory for all products at once
        for (OrderItemRequest itemRequest : orderItemRequests) {
            InventoryResponse inventoryResponse = inventoryResponses.stream()
                    .filter(inv -> inv.getProductId().equals(itemRequest.getProductId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Product not found in inventory: " + itemRequest.getProductId()));

            if (!inventoryResponse.isInStock() || inventoryResponse.getAvailableQuantity() < itemRequest.getQuantity()) {
                throw new IllegalStateException(
                    String.format("Insufficient inventory for product %d. Available: %d, Requested: %d",
                        itemRequest.getProductId(), 
                        inventoryResponse.getAvailableQuantity(), 
                        itemRequest.getQuantity())
                );
            }
        }

        // Create order
        Order order = new Order();
        order.setUserId(orderRequest.getUserId());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PENDING");
        order.setShippingAddress(orderRequest.getShippingAddress());

        try {
            // Create order items and update inventory atomically
            List<OrderItem> orderItems = new ArrayList<>();
            for (OrderItemRequest itemRequest : orderItemRequests) {
                ProductResponse product = productServiceClient.getProductById(itemRequest.getProductId());
                
                // Update inventory first
                log.info("Updating inventory for product: {} with quantity: {}", 
                    itemRequest.getProductId(), -itemRequest.getQuantity());
                    
                inventoryServiceClient.updateInventory(
                    itemRequest.getProductId(), 
                    -itemRequest.getQuantity()
                );

                // Create order item
                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(itemRequest.getProductId());
                orderItem.setProductName(product.getName());
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setPrice(product.getPrice());
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            }

            order.setOrderItems(orderItems);

            // Calculate total amount
            BigDecimal totalAmount = orderItems.stream()
                    .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            order.setTotalAmount(totalAmount);

            // Save order
            Order savedOrder = orderRepository.save(order);
            log.info("Order created successfully with ID: {}", savedOrder.getId());

            // Process payment
            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .orderId(savedOrder.getId())
                    .userId(savedOrder.getUserId())
                    .amount(savedOrder.getTotalAmount())
                    .paymentMethod("CREDIT_CARD")
                    .build();

            PaymentResponse paymentResponse = paymentServiceClient.processPayment(paymentRequest);
            log.info("Payment processed with status: {}", paymentResponse.getStatus());

            // Update order with payment details
            savedOrder.setPaymentId(paymentResponse.getPaymentId());

            if ("COMPLETED".equals(paymentResponse.getStatus())) {
                savedOrder.setStatus("PAID");
            } else {
                savedOrder.setStatus("PAYMENT_FAILED");
                // If payment fails, we should consider rolling back inventory changes
                // This would require additional implementation
            }

            return orderRepository.save(savedOrder);
            
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
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

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
