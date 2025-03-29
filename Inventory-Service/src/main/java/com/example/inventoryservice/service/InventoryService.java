package com.example.inventoryservice.service;

import com.example.inventoryservice.client.ProductServiceClient;
import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.dto.ProductResponse;
import com.example.inventoryservice.model.InventoryItem;
import com.example.inventoryservice.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductServiceClient productServiceClient;

    public List<InventoryResponse> isInStock(List<Long> productIds) {
        log.info("Checking inventory status for products: {}", productIds);

        return inventoryRepository.findByProductIdIn(productIds).stream()
                .map(inventoryItem -> {
                    try {
                        ProductResponse product = productServiceClient.getProductById(inventoryItem.getProductId());
                        return InventoryResponse.builder()
                                .productId(inventoryItem.getProductId())
                                .productName(product.getName())
                                .isInStock(inventoryItem.getQuantity() > 0)
                                .availableQuantity(inventoryItem.getQuantity())
                                .build();
                    } catch (Exception e) {
                        log.error("Error fetching product details: {}", e.getMessage());
                        return InventoryResponse.builder()
                                .productId(inventoryItem.getProductId())
                                .productName("Unknown")
                                .isInStock(inventoryItem.getQuantity() > 0)
                                .availableQuantity(inventoryItem.getQuantity())
                                .build();
                    }
                })
                .collect(Collectors.toList());
    }

    public InventoryItem updateInventory(Long productId, Integer quantityChange) {
        InventoryItem inventoryItem = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product: " + productId));

        int newQuantity = inventoryItem.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalStateException("Insufficient inventory for product: " + productId + ". Available: " + inventoryItem.getQuantity() + ", Requested: " + Math.abs(quantityChange));
        }

        inventoryItem.setQuantity(newQuantity);
        return inventoryRepository.save(inventoryItem);
    }

    public InventoryItem addInventoryItem(InventoryItem inventoryItem) {
        return inventoryRepository.save(inventoryItem);
    }

    public InventoryItem getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product: " + productId));
    }
}