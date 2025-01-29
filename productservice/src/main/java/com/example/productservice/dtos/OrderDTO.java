package com.example.productservice.dtos;

import java.util.HashSet;
import java.util.Set;

public record OrderDTO(Long id, Long userId, Set<OrderItemDTO> products) {
    public OrderDTO {
        // Para asegurar que la colección no sea null
        if (products == null) {
            products = new HashSet<>();
        }
    }
}
