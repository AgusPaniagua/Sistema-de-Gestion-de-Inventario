package com.inventario.gestion_inventario.controller;

import com.inventario.gestion_inventario.model.Producto;
import com.inventario.gestion_inventario.repository.ProductoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    @GetMapping
    public List<Producto> obtenerTodos(){
        return productoRepository.findAll();
    }

    @PostMapping
    public Producto guardarProducto(@RequestBody Producto producto){
        return productoRepository.save(producto);
    }
    
    @GetMapping("/{id}")
    public Optional<Producto> buscarPorId(@PathVariable Long id){
        return productoRepository.findById(id);
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id){
        productoRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Producto modificarProducto(@PathVariable Long id, @RequestBody Producto producto){
        producto.setId(id);
        return productoRepository.save(producto);
    }

    @PostMapping("/{id}/vender")
    public Producto venderProducto(@PathVariable Long id, @RequestParam Integer cantidad){
        Producto productoVender = productoRepository.findById(id).orElseThrow();
        if (productoVender.getCantidad()>=cantidad){
            productoVender.setCantidad(productoVender.getCantidad()-cantidad);
            return productoRepository.save(productoVender);
        }else {
            throw new RuntimeException("No hay stock disponible");
        }
    }

    @PostMapping("/{id}/agregarStock")
    public Producto agregarStock(@PathVariable Long id, @RequestParam Integer cantidad){
        if(cantidad<=0){
            throw new RuntimeException("La cantidad a agregar debe ser mayor a cero");
        }
        Producto productoAgregarStock = productoRepository.findById(id).orElseThrow();
        productoAgregarStock.setCantidad(productoAgregarStock.getCantidad()+cantidad);
        return productoRepository.save(productoAgregarStock);
    }

}
