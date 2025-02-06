package org.compras.model;

import java.util.List;

public class Compra {
    private Long id;
    private String usuario;
    private List<Producto> productos;

    public Compra(String usuario, List<Producto> productos) {
        this.usuario = usuario;
        this.productos = productos;
    }

    public Long getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public Compra(Long id, String usuario, List<Producto> productos) {
        this.id = id;
        this.usuario = usuario;
        this.productos = productos;
    }
}