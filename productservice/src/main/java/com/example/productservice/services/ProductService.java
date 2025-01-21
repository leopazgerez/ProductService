package com.example.productservice.services;

import com.example.productservice.dtos.ProductDTO;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    ProductDTO createProduct(ProductDTO productDTO) throws BadRequestException;
    ProductDTO updateProduct(ProductDTO productDTO, Long id) throws BadRequestException;
}
