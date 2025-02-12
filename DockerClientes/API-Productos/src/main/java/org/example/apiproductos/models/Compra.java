package org.example.apiproductos.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "compras")
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra", nullable = false)
    private Integer id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "cliente")
    private String cliente;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "compra_producto",
            joinColumns = @JoinColumn(name = "id_compra"),
            inverseJoinColumns = @JoinColumn(name = "id_producto")
    )
    private List<Producto> productos = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
        for (Producto p : productos) {
            p.getCompras().add(this);
        }
    }

    @Override
    public String toString() {
        return "Compra{" +
                "id=" + id +
                ", cliente='" + cliente + '\'' +
                ", fecha=" + fecha +
                ", productos=" + productos.stream()
                .map(producto -> "{id=" + producto.getId() + ", nombre=" + producto.getNombre() + "}")
                .toList() +
                '}';
    }

}

