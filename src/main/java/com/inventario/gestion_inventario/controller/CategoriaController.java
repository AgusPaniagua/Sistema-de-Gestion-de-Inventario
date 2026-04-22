package com.inventario.gestion_inventario.controller;

import com.inventario.gestion_inventario.dto.CategoriaRequest;
import com.inventario.gestion_inventario.model.Categoria;
import com.inventario.gestion_inventario.repository.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public List<Categoria> traerCategorias(){
        return categoriaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Categoria traerCategoriaPorId(@PathVariable Long id){
        return categoriaRepository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
    }

    @PostMapping
    public Categoria crearCategoria(@RequestBody CategoriaRequest categoriaDTO){
        Categoria nuevaCategoria = new Categoria();
        nuevaCategoria.setNombre(categoriaDTO.nombre());
        return categoriaRepository.save(nuevaCategoria);
    }

    @DeleteMapping("/{id}")
    public void eliminarCategoria(@PathVariable Long id){
        if(!categoriaRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Categoria modificarCategoria(@PathVariable Long id, @RequestBody CategoriaRequest categoriaDTO){
        Categoria categoriaExistente = categoriaRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
        categoriaExistente.setNombre(categoriaDTO.nombre());
        return categoriaRepository.save(categoriaExistente);
    }

}
