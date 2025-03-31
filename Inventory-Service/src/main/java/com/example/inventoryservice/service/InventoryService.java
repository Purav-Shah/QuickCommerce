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
        log.info("Updating inventory for productId: {}, quantityChange: {}", productId, quantityChange);
        
        InventoryItem inventoryItem = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> {
                    log.error("Inventory not found for product: {}", productId);
                    return new EntityNotFoundException("Inventory not found for product: " + productId);
                });
                
        log.info("Current inventory for product {}: {}", productId, inventoryItem.getQuantity());
        
        int newQuantity = inventoryItem.getQuantity() + quantityChange;
        log.info("Calculated new quantity for product {}: {}", productId, newQuantity);
        
        if (newQuantity < 0) {
            String errorMessage = String.format("Insufficient inventory for product: %d. Available: %d, Requested: %d",
                    productId, inventoryItem.getQuantity(), Math.abs(quantityChange));
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        inventoryItem.setQuantity(newQuantity);
        InventoryItem savedItem = inventoryRepository.save(inventoryItem);
        log.info("Successfully updated inventory for product {}. New quantity: {}", productId, savedItem.getQuantity());
        return savedItem;
    }

    public InventoryItem addInventoryItem(InventoryItem inventoryItem) {
        return inventoryRepository.save(inventoryItem);
    }

    public InventoryItem getInventoryByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new EntityNotFoundException("Inventory not found for product: " + productId));
    }
}