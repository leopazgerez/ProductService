package com.example.productservice.controllers;

import com.example.productservice.dtos.OrderDTO;
import com.example.productservice.dtos.OrderItemDTO;
import com.example.productservice.dtos.ProductDTO;
import com.example.productservice.services.ProductService;
import jakarta.validation.Valid;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @Operation(summary = "Retrieve all product", description = "Fetches all products.")
    @GetMapping("/all")
    ResponseEntity<?> getAllProduct() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @PostMapping("/existStock")
    ResponseEntity<?> existStock(@RequestBody Set<OrderItemDTO> itemsOrder) {
        return ResponseEntity.ok(productService.existStockOfProducts(itemsOrder));
    }

    @Operation(summary = "Update product by Id", description = "Update product.")
    @PutMapping("/update")
    ResponseEntity<?> updateProduct(@Valid @RequestBody ProductDTO productDTO, @PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(productService.updateProduct(productDTO, id));
    }

    @Operation(summary = "Update stock", description = "Update stocks of products sent by body")
    @PutMapping("/updateStock")
    ResponseEntity<?> updateStock(@Valid @RequestBody OrderDTO orderDTO) throws BadRequestException {
        productService.updateStock(orderDTO);
        return ResponseEntity.ok("Stock updated successfully");
    }

    @Operation(summary = "Create product", description = "Create product.")
    @PostMapping("/create")
    ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO) throws BadRequestException {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }


}
