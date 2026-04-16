package com.inventario.gestion_inventario.repository;

import com.inventario.gestion_inventario.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
