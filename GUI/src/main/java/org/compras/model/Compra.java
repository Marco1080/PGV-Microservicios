package org.compras.model;

import java.util.List;

public class Compra {
    private Integer id;
    private final String cliente;
    private final List<Producto> productos;

    public Compra(String cliente, List<Producto> productos) {
        this.cliente = cliente;
        this.productos = productos;
    }

    public Integer getId() {
        return id;
    }

    public String getCliente() {
        return cliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public Compra(Integer id, String cliente, List<Producto> productos) {
        this.id = id;
        this.cliente = cliente;
        this.productos = productos;
    }
}