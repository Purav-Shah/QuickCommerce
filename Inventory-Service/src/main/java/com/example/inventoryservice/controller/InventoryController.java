package com.example.inventoryservice.controller;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.model.InventoryItem;
import com.example.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
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
    public InventoryItem updateInventory(@PathVariable Long productId, @RequestParam Integer quantityChange) {
        return inventoryService.updateInventory(productId, quantityChange);
    }
}