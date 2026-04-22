package com.inventario.gestion_inventario.repository;

import com.inventario.gestion_inventario.model.Categoria;
import com.inventario.gestion_inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaId(Long id);
    List<Producto> findByCategoriaNombre(String nombre);
}
