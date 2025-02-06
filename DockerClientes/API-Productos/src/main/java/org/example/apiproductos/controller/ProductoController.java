package org.example.apiproductos.controller;

import org.example.apiproductos.models.Producto;
import org.example.apiproductos.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ProductoController {
    @Autowired
    private ProductosRepository repositorio;

    // Obtener todos los productos
    @GetMapping("/productos")
    public List<Producto> obtenerProductos() {
        return repositorio.findAll();
    }

    // Agregar un producto
    @PostMapping("/producto")
    public Producto agregarProducto(@RequestBody Producto producto) {
        return repositorio.save(producto);
    }

    // Eliminar un producto por ID
    @DeleteMapping("/producto/{id}")
    public String eliminarProducto(@PathVariable Integer id) {
        if (repositorio.existsById(id)) {
            repositorio.deleteById(id);
            return "Producto eliminado con éxito.";
        } else {
            return "Producto no encontrado.";
        }
    }

    // Actualizar un producto por ID
    @PutMapping("/producto/{id}")
    public String actualizarProducto(@PathVariable Integer id, @RequestBody Producto productoActualizado) {
        Optional<Producto> productoOptional = repositorio.findById(id);
        if (productoOptional.isPresent()) {
            Producto producto = productoOptional.get();
            producto.setNombre(productoActualizado.getNombre());
            producto.setPrecio(productoActualizado.getPrecio());
            producto.setStock(productoActualizado.getStock());
            repositorio.save(producto);
            return "Producto actualizado correctamente.";
        } else {
            return "Producto no encontrado.";
        }
    }
}

