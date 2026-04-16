package com.inventario.gestion_inventario.dto;

public record ProductoRequest(String nombre, Double precio, Integer cantidad, Long categoriaId) {
}
