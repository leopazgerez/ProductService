package com.example.productservice.controllers;

import com.example.productservice.dtos.ItemOrder;
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
    ResponseEntity<?> existStock(@RequestBody Set<ItemOrder> itemsOrder) {
        return ResponseEntity.ok(productService.existStockOfProducts(itemsOrder));
    }

    @Operation(summary = "Update product by Id", description = "Update product.")
    @PutMapping("/update")
//    agregar @Valid luego de testear
    ResponseEntity<?> updateProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id) throws BadRequestException {
        return ResponseEntity.ok(productService.updateProduct(productDTO, id));
    }

    @Operation(summary = "Update stock", description = "Update stocks of products sent by body")
    @PutMapping("/updateStock")
    ResponseEntity<?> updateStock(@RequestBody Set<ItemOrder> itemsOrder) throws BadRequestException {
        productService.updateStock(itemsOrder);
        return ResponseEntity.ok("Stock updated successfully");
    }

    @Operation(summary = "Create product", description = "Create product.")
    @PostMapping("/create")
        //    agregar @Valid luego de testear
    ResponseEntity<?> createProduct(@RequestBody @Valid ProductDTO productDTO) throws BadRequestException {
        return ResponseEntity.ok(productService.createProduct(productDTO));
    }


}
