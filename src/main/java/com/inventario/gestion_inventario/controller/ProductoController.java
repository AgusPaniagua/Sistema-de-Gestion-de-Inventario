package com.inventario.gestion_inventario.controller;

import com.inventario.gestion_inventario.dto.ProductoRequest;
import com.inventario.gestion_inventario.dto.ProductoResponse;
import com.inventario.gestion_inventario.model.Categoria;
import com.inventario.gestion_inventario.model.Producto;
import com.inventario.gestion_inventario.repository.CategoriaRepository;
import com.inventario.gestion_inventario.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    public ProductoController(ProductoRepository productoRepository, CategoriaRepository categoriaRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository=categoriaRepository;
    }
    @GetMapping
    public List<ProductoResponse> obtenerTodos(){
        return  productoRepository.findAll().stream().map(p-> new ProductoResponse(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getCantidad(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoria"
        )).toList();
    }

    @PostMapping
    public Producto crearProducto(@RequestBody ProductoRequest productoDTO){
        if(productoDTO.categoriaId()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Producto no creado");
        }
        Categoria categoriaEncontrada = categoriaRepository.findById(productoDTO.categoriaId()).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
        Producto nuevoProducto = new Producto();
        nuevoProducto.setNombre(productoDTO.nombre());
        nuevoProducto.setPrecio(productoDTO.precio());
        nuevoProducto.setCantidad(productoDTO.cantidad());
        nuevoProducto.setCategoria(categoriaEncontrada);

        return productoRepository.save(nuevoProducto);
    }
    
    @GetMapping("/{id}")
    public Producto buscarPorId(@PathVariable Long id){
        return productoRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Prodcuto no encontrado"));
    }

    @DeleteMapping("/{id}")
    public void eliminarProducto(@PathVariable Long id){
        if(!productoRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prodcuto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public ProductoResponse modificarProducto(@PathVariable Long id, @RequestBody ProductoRequest productoDTO){
        Producto productoExistente = productoRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        Categoria categoriaNueva = categoriaRepository.findById(productoDTO.categoriaId()).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada"));
        productoExistente.setNombre(productoDTO.nombre());
        productoExistente.setPrecio(productoDTO.precio());
        productoExistente.setCantidad(productoDTO.cantidad());
        productoExistente.setCategoria(categoriaNueva);
        Producto productoActualizado = productoRepository.save(productoExistente);
        return new ProductoResponse(
                productoActualizado.getId(),
                productoActualizado.getNombre(),
                productoActualizado.getPrecio(),
                productoActualizado.getCantidad(),
                productoActualizado.getCategoria().getNombre()
        );
    }

    @PostMapping("/{id}/vender")
    public Producto venderProducto(@PathVariable Long id, @RequestParam Integer cantidad){
        Producto productoVender = productoRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
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
        Producto productoAgregarStock = productoRepository.findById(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
        productoAgregarStock.setCantidad(productoAgregarStock.getCantidad()+cantidad);
        return productoRepository.save(productoAgregarStock);
    }

    @GetMapping("/categoria/id/{id}")
    public List<ProductoResponse> filtarPorIdDeCategoria(@PathVariable Long id){
        return productoRepository.findByCategoriaId(id).stream().map(p-> new ProductoResponse(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getCantidad(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoria"
        )).toList();
    }

    @GetMapping("/categoria/nombre/{nombre}")
    public List<ProductoResponse> filtrarPorNombreDeCategoria(@PathVariable String nombre){
        return productoRepository.findByCategoriaNombre(nombre).stream().map(p->new ProductoResponse(
                p.getId(),
                p.getNombre(),
                p.getPrecio(),
                p.getCantidad(),
                p.getCategoria() != null ? p.getCategoria().getNombre() : "Sin categoria"
        )).toList();
    }


}
