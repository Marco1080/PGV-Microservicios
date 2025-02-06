package org.example.apiproductos.controller;

import org.example.apiproductos.models.Compra;
import org.example.apiproductos.repository.CompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CompraController {
    @Autowired
    private CompraRepository compraRepository;

    @PostMapping("/compra")
    public Compra agregarCompra(@RequestBody Compra compra) {
        return compraRepository.save(compra);
    }
}
