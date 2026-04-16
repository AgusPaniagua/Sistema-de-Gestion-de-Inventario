package com.inventario.gestion_inventario.dto;

public record  ProductoResponse(
        Long id,
        String nombre,
        Double precio,
        Integer cantidad,
        String categoriaNombre
) {
}
