package com.example.orderservice.client;

import com.example.orderservice.dto.ProductResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductServiceClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(@PathVariable Long id);
}