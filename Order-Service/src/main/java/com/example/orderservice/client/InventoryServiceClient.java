package com.example.orderservice.client;

import com.example.orderservice.dto.InventoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryServiceClient {

    @GetMapping("/check")
    List<InventoryResponse> checkInventory(@RequestParam List<Long> productIds);

    @PutMapping("/{productId}")
    void updateInventory(@PathVariable Long productId, @RequestParam Integer quantityChange);
}