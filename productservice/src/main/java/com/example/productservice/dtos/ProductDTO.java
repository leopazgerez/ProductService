package com.example.productservice.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//@NotEmpty: Valida que una colección, mapa, arreglo o cadena no esté vacío (aplicable a String, List, etc.).
//@NotNull: Valida que el objeto no sea nulo (aplicable a cualquier tipo).
//@NotBlank: Similar a @NotEmpty pero aplica solo a cadenas, verificando que no sea null y no esté en blanco (sin espacios).
public class ProductDTO {
    private Long id;
    @NotBlank(message = "name no puede esstar vacio o nulo")
    private String name;
    @NotBlank(message = "description no puede esstar vacio o nulo")
    private String description;
    @NotNull(message = "price no puede esstar vacio o nulo")
    private Double price;
    @NotNull(message = "stock no puede esstar vacio o nulo")
    private Integer stock;

    public ProductDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}


