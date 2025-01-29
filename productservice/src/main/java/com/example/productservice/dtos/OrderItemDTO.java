package com.example.productservice.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude
public record OrderItemDTO(Long id, Long orderId, Long productId, Integer quantity) {
}
