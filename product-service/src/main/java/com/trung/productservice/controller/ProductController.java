package com.trung.productservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @GetMapping
    public List<String> getMockProducts() {
        return Arrays.asList("MacBook Pro M3", "iPhone 15 Pro Max", "iPad Air 6");
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestHeader(name = "X-User-Username") String userId,
                                           @RequestBody Map<String, String> productData) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Product created successfully");
        response.put("product", productData);
        response.put("createdBy", userId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
