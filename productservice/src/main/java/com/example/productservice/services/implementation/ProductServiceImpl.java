package com.example.productservice.services.implementation;

import com.example.productservice.Mappers.ProductMapper;
import com.example.productservice.dtos.ItemOrder;
import com.example.productservice.dtos.ProductDTO;
import com.example.productservice.exceptions.ProductException;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.ProductRepository;
import com.example.productservice.services.ProductService;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
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
            productFound = productRepository.findById(id).orElseThrow(() -> new ProductException("Product does not exist"));
            productFound.setStock(productFound.getStock());
            try {
                productSaved = productRepository.save(productFound);
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }
        }
        return productMapper.entityToDTO(productSaved);
    }

    @Override
    public void updateStock(Set<ItemOrder> itemsOrder) throws BadRequestException {
        Product productFound;
        Product productSaved = new Product();
        validateStockFromRequest(itemsOrder);
        for (ItemOrder item : itemsOrder) {
            productFound = productRepository.findById(item.id()).orElseThrow(() -> new ProductException("Product does not exist"));
            if (productFound.getStock() >= item.quantity()) {
                productFound.setStock(productFound.getStock() - item.quantity());
                updateProduct(productMapper.entityToDTO(productFound), productFound.getId());
            } else {
                throw new ProductException("Stock is not enough");
            }
        }
    }

    private void validateStockFromRequest(Set<ItemOrder> itemsOrder) {
        itemsOrder.forEach(item -> {
            if (item.quantity() == null) {
                throw new ProductException("Item must have quantity greater than 0");
            }
        });
    }


    @Override
    public boolean existStockOfProducts(Set<ItemOrder> itemsOrder) {
        boolean result = false;
        for (ItemOrder order : itemsOrder) {
            if (productRepository.existsById(order.id())) {
                Product product = productRepository.findById(order.id()).orElseThrow(() -> new ProductException("Product does not exist"));
                if (product.getStock() >= order.quantity()) {
                    result = true;
                } else {
                    throw new ProductException("Stock is not enough");
                }
            } else {
                throw new ProductException("Product does not exist");
            }
        }
        return result;
    }


}
