package com.example.productservice.services.implementation;

import com.example.productservice.Mappers.ProductMapper;
import com.example.productservice.Utils.RabbitValues;
import com.example.productservice.dtos.OrderDTO;
import com.example.productservice.dtos.OrderItemDTO;
import com.example.productservice.dtos.ProductDTO;
import com.example.productservice.exceptions.InsufficientStockException;
import com.example.productservice.exceptions.ProductException;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.ProductRepository;
import com.example.productservice.services.ProductService;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitValues rabbitValues;

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
            log.error(e.getMessage(), e);
            throw new BadRequestException(e.getMessage());
        }
        return productMapper.entityToDTO(productSaved);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(ProductDTO productDTO, Long id) throws BadRequestException {
        Product productFound;
        Product productSaved = new Product();
        if (productRepository.existsById(id)) {
            productFound = productRepository.findById(id).orElseThrow(() -> new ProductException("Product does not exist"));
            productFound.setStock(productFound.getStock());
            try {
                productSaved = productRepository.save(productFound);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage(), e);
                throw new BadRequestException(e.getMessage());
            }
        }
        return productMapper.entityToDTO(productSaved);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Set<ProductDTO> getOrderProducts(Set<OrderItemDTO> orderItems) {
        final Set<Long> productsIds = orderItems.stream().map(OrderItemDTO::productId).collect(Collectors.toSet());
        return productRepository
                .findAllById(productsIds)
                .stream()
                .map(productMapper::entityToDTO)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(rollbackOn = InsufficientStockException.class)
//    Para poder acceder a una propiedad de un @Component se antepone "#"
    @RabbitListener(queues = "#{@rabbitValues.updateStockQueue}")
    public void updateStock(OrderDTO orderDTO) throws BadRequestException {
        try {
            for (OrderItemDTO item : orderDTO.products()) {
                Product productFound = productRepository.findById(item.productId())
                        .orElseThrow(() -> new ProductException("Product not found"));
//            Actualizo en caso de poder a nivel entidad sino lanzo excepción que desencadena el rolleback
                productFound.updateStock(item.quantity());
                productRepository.save(productFound);
            }
            rabbitTemplate.convertAndSend(rabbitValues.getExchange(), rabbitValues.getUpdateOrderRoutingKey(), orderDTO.id());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public boolean existStockOfProducts(Set<OrderItemDTO> itemsOrder) {
        boolean result = false;
        for (OrderItemDTO order : itemsOrder) {
            if (productRepository.existsById(order.productId())) {
                Product product = productRepository.findById(order.productId()).orElseThrow(() -> new ProductException("Product does not exist"));
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
