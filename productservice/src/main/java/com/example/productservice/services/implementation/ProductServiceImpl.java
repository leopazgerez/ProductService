package com.example.productservice.services.implementation;

import com.example.productservice.Mappers.ProductMapper;
import com.example.productservice.dtos.ProductDTO;
import com.example.productservice.exceptions.ProductDoesNotExistException;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.ProductRepository;
import com.example.productservice.services.ProductService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ProductMapper productMapper;

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository
                .findAll()
                .stream()
                .map(productMapper::entityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) throws BadRequestException {
        Product productSaved;
        if (productDTO.getName().isEmpty() | productDTO.getDescription().isEmpty() | productDTO.getPrice() == null | productDTO.getStock() == null) {
            throw new BadRequestException("One or more attribute does not match with the request body");
        }
        try {
            productSaved = productRepository.save(productMapper.dtoToEntity(productDTO));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        return productMapper.entityToDTO(productSaved);
    }

    @Override
    public ProductDTO updateProduct(ProductDTO productDTO, Long id) throws BadRequestException {
        Product productFound;
        Product productSaved = new Product();
        if (productRepository.existsById(id)) {
            productFound = productRepository.findById(id).orElseThrow(() -> new ProductDoesNotExistException("Product does not exist"));
            productFound.setStock(productFound.getStock());
            try {
                productSaved = productRepository.save(productFound);
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return productMapper.entityToDTO(productSaved);
    }
}
