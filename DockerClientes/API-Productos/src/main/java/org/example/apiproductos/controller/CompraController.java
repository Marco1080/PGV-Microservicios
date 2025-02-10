package org.example.apiproductos.controller;

import org.example.apiproductos.models.Compra;
import org.example.apiproductos.models.Producto;
import org.example.apiproductos.repository.CompraRepository;
import org.example.apiproductos.repository.ProductosRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class CompraController {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProductosRepository productoRepository;



    @PostMapping("/compra")
    public Compra agregarCompra(@RequestBody Compra compra) {
        // Asegurar que la fecha no sea nula
        if (compra.getFecha() == null) {
            compra.setFecha(LocalDate.now());
        }

        // Filtrar productos existentes en la base de datos
        List<Producto> productosValidos = compra.getProductos().stream()
                .map(producto -> productoRepository.findById(producto.getId())
                        .orElse(null)) // Si el producto no existe, se pone null
                .filter(Objects::nonNull) // Se eliminan los nulls
                .toList();

        if (productosValidos.isEmpty()) {
            throw new RuntimeException("No se encontraron productos válidos para la compra.");
        }

        // Asegurar que la compra no tenga un ID previo
        compra.setId(null);
        compra.setProductos(productosValidos);

        System.out.println(compra.toString());

        return compraRepository.save(compra);
    }

}
