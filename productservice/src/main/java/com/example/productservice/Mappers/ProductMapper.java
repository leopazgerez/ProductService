package com.example.productservice.Mappers;

import com.example.productservice.dtos.ProductDTO;
import com.example.productservice.models.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductMapper() {
    }

    public ProductDTO entityToDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setDescription(product.getDescription());
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setStock(product.getStock());
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }

    public Product dtoToEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setDescription(productDTO.getDescription());
        product.setName(productDTO.getName());
        product.setStock(productDTO.getStock());
        product.setPrice(productDTO.getPrice());
        return product;
    }
}
