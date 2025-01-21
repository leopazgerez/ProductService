package com.example.productservice.controllers;

import com.example.productservice.dtos.ProductDTO;
import com.example.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    ResponseEntity<?> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PutMapping("/update")
    ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id) {
        return ResponseEntity.ok(productService.updateProduct(productDTO, id));
    }

    @GetMapping("/create")
    ResponseEntity<?> createProduct(@RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }


}
