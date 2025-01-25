package com.example.productservice.dtos;

import jakarta.validation.constraints.NotNull;

public record ItemOrder(@NotNull(message = "ProductId es requerido") Long id,
                        @NotNull(message = "Quantity es requerido") Integer quantity) {
}
