package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.model.InventoryItem;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<Long> productIds) {
        return inventoryService.isInStock(productIds);
    }

    @GetMapping("/{productId}")
    public InventoryItem getInventoryByProductId(@PathVariable Long productId) {
        return inventoryService.getInventoryByProductId(productId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItem addInventoryItem(@RequestBody InventoryItem inventoryItem) {
        return inventoryService.addInventoryItem(inventoryItem);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateInventory(@PathVariable Long productId, @RequestParam Integer quantityChange) {
        try {
            log.info("Received request to update inventory for product {} with quantity change {}", 
                productId, quantityChange);
                
            InventoryItem updatedItem = inventoryService.updateInventory(productId, quantityChange);
            return ResponseEntity.ok(updatedItem);
            
        } catch (EntityNotFoundException e) {
            log.error("Product not found error: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(e.getMessage()));
                
        } catch (IllegalStateException e) {
            log.error("Insufficient inventory error: {}", e.getMessage());
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(e.getMessage()));
                
        } catch (Exception e) {
            log.error("Unexpected error updating inventory: {}", e.getMessage(), e);
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("An unexpected error occurred while updating inventory"));
        }
    }
}

@Data
@AllArgsConstructor
class ErrorResponse {
    private String message;
}
