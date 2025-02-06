package org.compras.model;

import java.util.List;

public class Compra {
    private Integer id;
    private String usuario;
    private List<Producto> productos;

    public Compra(String usuario, List<Producto> productos) {
        this.usuario = usuario;
        this.productos = productos;
    }

    public Integer getId() {
        return id;
    }

    public String getUsuario() {
        return usuario;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public Compra(Integer id, String usuario, List<Producto> productos) {
        this.id = id;
        this.usuario = usuario;
        this.productos = productos;
    }
}