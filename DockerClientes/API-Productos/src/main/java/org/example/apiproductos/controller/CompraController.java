package org.example.apiproductos.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.example.apiproductos.models.Compra;
import org.example.apiproductos.models.Producto;
import org.example.apiproductos.repository.CompraRepository;
import org.example.apiproductos.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CompraController {

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private ProductosRepository productoRepository;



    @PostMapping("/compra")
    public Compra agregarCompra(@RequestBody Compra compra) {
        if (compra.getFecha() == null) {
            compra.setFecha(LocalDate.now());
        }

        List<Producto> productosValidos = compra.getProductos().stream()
                .map(producto -> productoRepository.findById(producto.getId())
                        .orElse(null)) 
                .filter(Objects::nonNull)
                .toList();

        if (productosValidos.isEmpty()) {
            throw new RuntimeException("No se encontraron productos válidos para la compra.");
        }

        compra.setId(null);
        compra.setProductos(productosValidos);

        System.out.println(compra.toString());

        return compraRepository.save(compra);
    }

}
